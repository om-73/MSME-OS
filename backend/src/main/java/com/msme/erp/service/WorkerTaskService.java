package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class WorkerTaskService {

    private final WorkerTaskRepository workerTaskRepository;
    private final ProductionAuditLogRepository auditLogRepository;
    private final ProductionOrderRepository orderRepository;
    private final WorkflowStageRepository stageRepository;
    private final WorkflowEdgeRepository edgeRepository;
    private final WorkflowEngineService workflowEngineService;
    private final NotificationService notificationService;
    private final DispatchRecordRepository dispatchRecordRepository;

    public WorkerTaskService(WorkerTaskRepository workerTaskRepository,
                             ProductionAuditLogRepository auditLogRepository,
                             ProductionOrderRepository orderRepository,
                             WorkflowStageRepository stageRepository,
                             WorkflowEdgeRepository edgeRepository,
                             WorkflowEngineService workflowEngineService,
                             NotificationService notificationService,
                             DispatchRecordRepository dispatchRecordRepository) {
        this.workerTaskRepository = workerTaskRepository;
        this.auditLogRepository = auditLogRepository;
        this.orderRepository = orderRepository;
        this.stageRepository = stageRepository;
        this.edgeRepository = edgeRepository;
        this.workflowEngineService = workflowEngineService;
        this.notificationService = notificationService;
        this.dispatchRecordRepository = dispatchRecordRepository;
    }

    public List<WorkerTask> getAllTasks() {
        return workerTaskRepository.findByTenantId(TenantContext.getCurrentTenant());
    }

    public List<WorkerTask> getTasksForWorker(String workerId) {
        return workerTaskRepository.findByTenantIdAndAssignedWorkerId(TenantContext.getCurrentTenant(), workerId);
    }

    @Transactional
    public WorkerTask assignTask(Long taskId, String workerId, String workerName, String managerName) {
        String tenantId = TenantContext.getCurrentTenant();
        WorkerTask task = workerTaskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));

        String prevStatus = task.getStatus();
        task.setStatus("ASSIGNED");
        task.setAssignedWorkerId(workerId);
        task.setAssignedWorkerName(workerName);
        task = workerTaskRepository.save(task);

        // Audit Log
        auditLogRepository.save(ProductionAuditLog.builder()
                .tenantId(tenantId)
                .orderId(task.getOrderId())
                .stageId(task.getStageId())
                .stageName(task.getStageName())
                .operatorId("manager")
                .operatorName(managerName)
                .previousState(prevStatus)
                .newState("ASSIGNED")
                .reason("Assigned to worker: " + workerName)
                .device("Manager Dashboard")
                .timestamp(LocalDateTime.now())
                .build());

        // Notify worker
        notificationService.createNotification(
                "Task Assigned",
                "New task assigned for " + task.getProductName() + " (Stage: " + task.getStageName() + ")",
                "notice",
                task.getOrderNumber()
        );

        return task;
    }

    @Transactional
    public WorkerTask startTask(Long taskId, String operatorName) {
        String tenantId = TenantContext.getCurrentTenant();
        WorkerTask task = workerTaskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));

        String prevStatus = task.getStatus();
        task.setStatus("IN_PROGRESS");
        task.setStartTime(LocalDateTime.now());
        task = workerTaskRepository.save(task);

        auditLogRepository.save(ProductionAuditLog.builder()
                .tenantId(tenantId)
                .orderId(task.getOrderId())
                .stageId(task.getStageId())
                .stageName(task.getStageName())
                .operatorId(task.getAssignedWorkerId())
                .operatorName(operatorName)
                .previousState(prevStatus)
                .newState("IN_PROGRESS")
                .reason("Task started by operator")
                .device("Worker App Mobile")
                .timestamp(LocalDateTime.now())
                .build());

        return task;
    }

    @Transactional
    public WorkerTask pauseTask(Long taskId, String operatorName) {
        String tenantId = TenantContext.getCurrentTenant();
        WorkerTask task = workerTaskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));

        String prevStatus = task.getStatus();
        task.setStatus("PAUSED");
        task = workerTaskRepository.save(task);

        auditLogRepository.save(ProductionAuditLog.builder()
                .tenantId(tenantId)
                .orderId(task.getOrderId())
                .stageId(task.getStageId())
                .stageName(task.getStageName())
                .operatorId(task.getAssignedWorkerId())
                .operatorName(operatorName)
                .previousState(prevStatus)
                .newState("PAUSED")
                .reason("Task paused by operator")
                .device("Worker App Mobile")
                .timestamp(LocalDateTime.now())
                .build());

        return task;
    }

    @Transactional
    public WorkerTask completeTask(Long taskId, String remarks, String photoUrl, String operatorName) {
        String tenantId = TenantContext.getCurrentTenant();
        WorkerTask task = workerTaskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));

        String prevStatus = task.getStatus();
        task.setStatus("COMPLETED");
        task.setEndTime(LocalDateTime.now());
        task.setRemarks(remarks);
        task.setPhotoUrl(photoUrl);
        task = workerTaskRepository.save(task);

        // Audit Log
        auditLogRepository.save(ProductionAuditLog.builder()
                .tenantId(tenantId)
                .orderId(task.getOrderId())
                .stageId(task.getStageId())
                .stageName(task.getStageName())
                .operatorId(task.getAssignedWorkerId())
                .operatorName(operatorName)
                .previousState(prevStatus)
                .newState("COMPLETED")
                .reason("Task completed by operator. Remarks: " + remarks)
                .device("Worker App Mobile")
                .timestamp(LocalDateTime.now())
                .build());

        // Automatic stage movement logic
        ProductionOrder order = orderRepository.findById(task.getOrderId())
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + task.getOrderId()));

        // Resolve next stage in active workflow
        List<WorkflowEdge> edges = edgeRepository.findByWorkflowVersionIdAndSourceStageId(
                order.getCurrentStageId(), 
                order.getCurrentStageId()
        ); // Wait! Let's query version edges by current version id:
        // We can find version ID from stage definition
        WorkflowStage currentStage = stageRepository.findById(order.getCurrentStageId()).orElse(null);
        if (currentStage != null) {
            List<WorkflowEdge> versionEdges = edgeRepository.findByWorkflowVersionIdAndSourceStageId(
                    currentStage.getWorkflowVersionId(), 
                    order.getCurrentStageId()
            );

            if (!versionEdges.isEmpty()) {
                String nextStageId = versionEdges.get(0).getTargetStageId();
                WorkflowStage nextStage = stageRepository.findById(nextStageId).orElse(null);
                if (nextStage != null) {
                    // Update order stage automatically
                    workflowEngineService.moveOrderToStage(order.getId(), nextStageId, "Completed stage " + task.getStageName() + " (Auto queue transit)");
                    
                    // Create new WorkerTask for the next stage
                    WorkerTask nextTask = WorkerTask.builder()
                            .tenantId(tenantId)
                            .orderId(order.getId())
                            .orderNumber(order.getOrderNumber())
                            .productName(order.getProductName())
                            .stageId(nextStageId)
                            .stageName(nextStage.getName())
                            .status("PENDING")
                            .pausedTimeSeconds(0L)
                            .build();
                    workerTaskRepository.save(nextTask);

                    // Alert notification
                    notificationService.createNotification(
                            "Next Stage Queue Ready",
                            "Batch " + order.getOrderNumber() + " moved to " + nextStage.getName(),
                            "notice",
                            order.getOrderNumber()
                    );
                }
            } else {
                // No outgoing edges means it reached the final stage (Packing/Dispatch)
                order.setStatus(OrderStatus.COMPLETED);
                orderRepository.save(order);

                // Automate Dispatch Queue creation
                DispatchRecord dispatch = DispatchRecord.builder()
                        .tenantId(tenantId)
                        .orderId(order.getId())
                        .orderNumber(order.getOrderNumber())
                        .productName(order.getProductName())
                        .status("READY")
                        .checklistPassed(false)
                        .barcodeVerified(false)
                        .build();
                dispatchRecordRepository.save(dispatch);

                notificationService.createNotification(
                        "Order Completed & Ready for Dispatch",
                        "Order " + order.getOrderNumber() + " completed production. Added to dispatch waybills.",
                        "notice",
                        order.getOrderNumber()
                );
              
                // Terminate active workflow sequence
                Optional<ProductionWorkflow> pwOpt = orderRepository.findById(order.getId())
                        .flatMap(o -> Optional.ofNullable(o.getCurrentStageId()))
                        .flatMap(stageId -> stageRepository.findById(stageId))
                        .flatMap(stage -> Optional.of(stage.getWorkflowVersionId()))
                        .flatMap(verId -> Optional.of(ProductionWorkflow.builder().orderId(order.getId()).workflowVersionId(verId).status("COMPLETED").build()));
                // Completed!
            }
        }

        return task;
    }

    @Transactional
    public WorkerTask reportIssue(Long taskId, String issueType, String remarks, String operatorName) {
        String tenantId = TenantContext.getCurrentTenant();
        WorkerTask task = workerTaskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));

        String prevStatus = task.getStatus();
        task.setStatus("BLOCKED");
        task.setRemarks("Blocked: " + issueType + ". Detail: " + remarks);
        task = workerTaskRepository.save(task);

        // Block parent order
        ProductionOrder order = orderRepository.findById(task.getOrderId()).orElse(null);
        if (order != null) {
            order.setStatus(OrderStatus.BLOCKED);
            orderRepository.save(order);
        }

        auditLogRepository.save(ProductionAuditLog.builder()
                .tenantId(tenantId)
                .orderId(task.getOrderId())
                .stageId(task.getStageId())
                .stageName(task.getStageName())
                .operatorId(task.getAssignedWorkerId())
                .operatorName(operatorName)
                .previousState(prevStatus)
                .newState("BLOCKED")
                .reason("Issue reported: " + issueType + ". Details: " + remarks)
                .device("Worker App Mobile")
                .timestamp(LocalDateTime.now())
                .build());

        // Send alert
        notificationService.createNotification(
                "Production Blocked",
                "Issue reported in " + task.getStageName() + " (" + issueType + "): " + remarks,
                "warning",
                task.getOrderNumber()
        );

        return task;
    }

    @Transactional
    public void triggerRework(String orderId, String targetStageId, String reason, String managerName) {
        String tenantId = TenantContext.getCurrentTenant();
        ProductionOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));

        WorkflowStage targetStage = stageRepository.findById(targetStageId)
                .orElseThrow(() -> new NoSuchElementException("Stage not found: " + targetStageId));

        String prevStageName = order.getCurrentStageName();

        // 1. Mark existing active tasks as REWORK_REQUIRED
        List<WorkerTask> activeTasks = workerTaskRepository.findByTenantIdAndOrderId(tenantId, orderId);
        activeTasks.stream()
                .filter(t -> !"COMPLETED".equals(t.getStatus()))
                .forEach(t -> {
                    t.setStatus("REWORK_REQUIRED");
                    workerTaskRepository.save(t);
                });

        // 2. Set order stage backward
        order.setCurrentStageId(targetStageId);
        order.setCurrentStageName(targetStage.getName());
        order.setCurrentStageSequence(targetStage.getSequenceOrder());
        order.setStatus(OrderStatus.IN_PROGRESS);
        orderRepository.save(order);

        // 3. Create new WorkerTask for target rework stage
        WorkerTask reworkTask = WorkerTask.builder()
                .tenantId(tenantId)
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .productName(order.getProductName())
                .stageId(targetStageId)
                .stageName(targetStage.getName())
                .status("PENDING")
                .remarks("QC Rework ordered by manager. Reason: " + reason)
                .pausedTimeSeconds(0L)
                .build();
        workerTaskRepository.save(reworkTask);

        // 4. Log audit transition
        auditLogRepository.save(ProductionAuditLog.builder()
                .tenantId(tenantId)
                .orderId(orderId)
                .stageId(targetStageId)
                .stageName(targetStage.getName())
                .operatorId("manager")
                .operatorName(managerName)
                .previousState(prevStageName)
                .newState("REWORK_REQUIRED")
                .reason("QC Failed: Sent backwards to " + targetStage.getName() + ". Reason: " + reason)
                .device("Manager Dashboard")
                .timestamp(LocalDateTime.now())
                .build());

        // Notify department
        notificationService.createNotification(
                "Rework Ordered",
                "Batch " + order.getOrderNumber() + " returned to " + targetStage.getName() + " for correction.",
                "warning",
                order.getOrderNumber()
        );
    }
}
