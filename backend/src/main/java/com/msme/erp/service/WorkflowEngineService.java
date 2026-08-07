package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.dto.OrderDto;
import com.msme.erp.dto.WorkflowLogDto;
import com.msme.erp.repository.*;
import com.msme.erp.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkflowEngineService {

    private final ProductionWorkflowRepository productionWorkflowRepository;
    private final WorkflowVersionRepository versionRepository;
    private final WorkflowStageRepository stageRepository;
    private final WorkflowEdgeRepository edgeRepository;
    private final WorkflowLogRepository logRepository;
    private final ProductionOrderRepository orderRepository;
    private final DepartmentRepository departmentRepository;

    public WorkflowEngineService(ProductionWorkflowRepository productionWorkflowRepository,
                                 WorkflowVersionRepository versionRepository,
                                 WorkflowStageRepository stageRepository,
                                 WorkflowEdgeRepository edgeRepository,
                                 WorkflowLogRepository logRepository,
                                 ProductionOrderRepository orderRepository,
                                 DepartmentRepository departmentRepository) {
        this.productionWorkflowRepository = productionWorkflowRepository;
        this.versionRepository = versionRepository;
        this.stageRepository = stageRepository;
        this.edgeRepository = edgeRepository;
        this.logRepository = logRepository;
        this.orderRepository = orderRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public ProductionWorkflow assignWorkflowToOrder(String orderId, String workflowVersionId) {
        // Deactivate previous active workflow if any
        Optional<ProductionWorkflow> oldOpt = productionWorkflowRepository.findByOrderIdAndStatus(orderId, "ACTIVE");
        if (oldOpt.isPresent()) {
            ProductionWorkflow oldWf = oldOpt.get();
            oldWf.setStatus("SUSPENDED");
            productionWorkflowRepository.save(oldWf);
        }

        WorkflowVersion ver = versionRepository.findById(workflowVersionId)
                .orElseThrow(() -> new NoSuchElementException("Workflow version not found: " + workflowVersionId));

        List<WorkflowStage> stages = stageRepository.findByWorkflowVersionIdOrderBySequenceOrderAsc(ver.getId());
        WorkflowStage startStage = stages.stream()
                .filter(s -> "START".equalsIgnoreCase(s.getType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Workflow version does not contain a START node."));

        ProductionWorkflow pw = ProductionWorkflow.builder()
                .orderId(orderId)
                .workflowVersionId(workflowVersionId)
                .status("ACTIVE")
                .build();
        pw = productionWorkflowRepository.save(pw);

        // Update the order with current stage info
        ProductionOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));
        order.setCurrentStageId(startStage.getId());
        order.setCurrentStageName(startStage.getName());
        order.setCurrentStageSequence(startStage.getSequenceOrder());
        order.setStatus(OrderStatus.IN_PROGRESS);
        orderRepository.save(order);

        // Initialize first log entry
        String operatorId = getCurrentUserId();
        String operatorName = getCurrentUserFullName();
        logRepository.save(WorkflowLog.builder()
                .productionWorkflowId(pw.getId())
                .targetStageId(startStage.getId())
                .operatorId(operatorId)
                .operatorName(operatorName)
                .departmentId(startStage.getDepartmentId())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .durationSeconds(0L)
                .delaySeconds(0L)
                .status("COMPLETED")
                .remarks("Order initialized and entered workflow START node: " + startStage.getName())
                .build());

        return pw;
    }

    @Transactional
    public void moveOrderToStage(String orderId, String targetStageId, String remarks) {
        String tenantId = TenantContext.getCurrentTenant();
        ProductionOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));

        ProductionWorkflow pw = productionWorkflowRepository.findByOrderIdAndStatus(order.getId(), "ACTIVE")
                .orElseThrow(() -> new IllegalStateException("No active workflow version assigned to this order."));

        String currentStageId = order.getCurrentStageId();

        // Validate target stage exists within this workflow
        WorkflowStage targetStage = stageRepository.findById(targetStageId)
                .orElseThrow(() -> new NoSuchElementException("Target stage not found: " + targetStageId));

        if (!targetStage.getWorkflowVersionId().equals(pw.getWorkflowVersionId())) {
            throw new IllegalArgumentException("Target stage is not part of the active workflow.");
        }

        // Validate edge connection exists
        List<WorkflowEdge> edges = edgeRepository.findByWorkflowVersionIdAndSourceStageId(pw.getWorkflowVersionId(), currentStageId);
        boolean pathExists = edges.stream().anyMatch(e -> e.getTargetStageId().equals(targetStageId));
        if (!pathExists && currentStageId != null) {
            throw new IllegalArgumentException("Invalid transition. There is no link from '" + order.getCurrentStageName() + "' to '" + targetStage.getName() + "'.");
        }

        // Complete the previous active stage log
        List<WorkflowLog> logs = logRepository.findByProductionWorkflowIdOrderByStartTimeDesc(pw.getId());
        if (!logs.isEmpty()) {
            WorkflowLog currentLog = logs.stream()
                    .filter(l -> l.getEndTime() == null || l.getStartTime().equals(l.getEndTime())) // active log
                    .findFirst()
                    .orElse(logs.get(0));

            currentLog.setEndTime(LocalDateTime.now());
            long duration = ChronoUnit.SECONDS.between(currentLog.getStartTime(), currentLog.getEndTime());
            currentLog.setDurationSeconds(duration);

            // Compute delay if SLA was specified
            WorkflowStage currentStage = stageRepository.findById(currentStageId).orElse(null);
            if (currentStage != null && currentStage.getEstimatedSlaHours() != null) {
                long allowedSeconds = currentStage.getEstimatedSlaHours() * 3600L;
                long delay = Math.max(0, duration - allowedSeconds);
                currentLog.setDelaySeconds(delay);
            }
            logRepository.save(currentLog);
        }

        // Setup next stage
        order.setCurrentStageId(targetStage.getId());
        order.setCurrentStageName(targetStage.getName());
        order.setCurrentStageSequence(targetStage.getSequenceOrder());

        if ("END".equalsIgnoreCase(targetStage.getType())) {
            order.setStatus(OrderStatus.COMPLETED);
            pw.setStatus("COMPLETED");
            pw.setCompletedAt(LocalDateTime.now());
            productionWorkflowRepository.save(pw);
        } else {
            order.setStatus(OrderStatus.IN_PROGRESS);
        }
        orderRepository.save(order);

        // Start new stage log
        String operatorId = getCurrentUserId();
        String operatorName = getCurrentUserFullName();
        logRepository.save(WorkflowLog.builder()
                .productionWorkflowId(pw.getId())
                .sourceStageId(currentStageId)
                .targetStageId(targetStage.getId())
                .operatorId(operatorId)
                .operatorName(operatorName)
                .departmentId(targetStage.getDepartmentId())
                .startTime(LocalDateTime.now())
                .status("COMPLETED")
                .remarks(remarks)
                .build());
    }

    public List<WorkflowLogDto> getOrderWorkflowHistory(String orderId) {
        ProductionWorkflow pw = productionWorkflowRepository.findByOrderId(orderId)
                .orElse(null);
        if (pw == null) return Collections.emptyList();

        List<WorkflowLog> entityLogs = logRepository.findByProductionWorkflowIdOrderByStartTimeDesc(pw.getId());
        return entityLogs.stream().map(l -> {
            String sourceName = l.getSourceStageId() != null ?
                    stageRepository.findById(l.getSourceStageId()).map(WorkflowStage::getName).orElse("Start") : "Start";
            String targetName = stageRepository.findById(l.getTargetStageId()).map(WorkflowStage::getName).orElse("End");
            String deptName = l.getDepartmentId() != null ?
                    departmentRepository.findById(l.getDepartmentId()).map(Department::getName).orElse("Unassigned") : "Unassigned";

            return new WorkflowLogDto(
                    l.getId(), l.getSourceStageId(), sourceName, l.getTargetStageId(), targetName,
                    l.getOperatorId(), l.getOperatorName(), l.getDepartmentId(), deptName,
                    l.getStartTime(), l.getEndTime(), l.getDurationSeconds(), l.getDelaySeconds(),
                    l.getStatus(), l.getRemarks()
            );
        }).collect(Collectors.toList());
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            return p.getId();
        }
        return "system";
    }

    private String getCurrentUserFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            return p.getFullName();
        }
        return "System Operator";
    }
}
