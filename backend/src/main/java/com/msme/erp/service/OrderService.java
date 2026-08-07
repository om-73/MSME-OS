package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.dto.*;
import com.msme.erp.repository.*;
import com.msme.erp.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ProductionOrderRepository orderRepository;
    private final BrandRepository brandRepository;
    private final WorkflowRepository workflowRepository;
    private final WorkflowVersionRepository versionRepository;
    private final WorkflowStageRepository stageRepository;
    private final OrderStageLogRepository logRepository;
    private final QCRecordRepository qcRepository;
    private final NotificationRepository notificationRepository;

    public OrderService(ProductionOrderRepository orderRepository, BrandRepository brandRepository,
                        WorkflowRepository workflowRepository, WorkflowVersionRepository versionRepository,
                        WorkflowStageRepository stageRepository, OrderStageLogRepository logRepository,
                        QCRecordRepository qcRepository, NotificationRepository notificationRepository) {
        this.orderRepository = orderRepository;
        this.brandRepository = brandRepository;
        this.workflowRepository = workflowRepository;
        this.versionRepository = versionRepository;
        this.stageRepository = stageRepository;
        this.logRepository = logRepository;
        this.qcRepository = qcRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<OrderDto> getAllOrders() {
        String tenantId = TenantContext.getCurrentTenant();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            boolean isBrandClient = p.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_BRAND_CLIENT"));
            if (isBrandClient && p.getBrandId() != null) {
                return getOrdersForBrand(p.getBrandId());
            }
        }
        List<ProductionOrder> orders = orderRepository.findByTenantId(tenantId);
        return orders.stream().map(this::mapToOrderDto).collect(Collectors.toList());
    }

    public List<OrderDto> getOrdersForBrand(String brandId) {
        String tenantId = TenantContext.getCurrentTenant();
        List<ProductionOrder> orders = orderRepository.findByTenantIdAndBrandId(tenantId, brandId);
        return orders.stream().map(this::mapToOrderDto).collect(Collectors.toList());
    }

    public OrderDto getOrderById(String id) {
        String tenantId = TenantContext.getCurrentTenant();
        ProductionOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        if (!order.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Access denied to order in different tenant");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            boolean isBrandClient = p.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_BRAND_CLIENT"));
            if (isBrandClient && !Objects.equals(order.getBrandId(), p.getBrandId())) {
                throw new IllegalArgumentException("Access denied. Client can only view their own brand orders.");
            }
        }
        return mapToOrderDto(order);
    }

    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {
        String tenantId = TenantContext.getCurrentTenant();
        String orderNumber = "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + (100 + new Random().nextInt(900));

        Workflow workflow = workflowRepository.findByTenantIdAndStatusAndDeletedFalse(tenantId, "PUBLISHED")
                .orElseGet(() -> {
                    List<Workflow> list = workflowRepository.findByTenantIdAndDeletedFalse(tenantId);
                    return list.isEmpty() ? null : list.get(0);
                });

        if (workflow == null) {
            throw new IllegalArgumentException("No workflow pipeline configured for this tenant.");
        }

        WorkflowVersion version = versionRepository.findByWorkflowIdAndVersionNumber(workflow.getId(), workflow.getCurrentVersion())
                .orElseThrow(() -> new IllegalArgumentException("Workflow active version missing"));

        List<WorkflowStage> stages = stageRepository.findByWorkflowVersionIdOrderBySequenceOrderAsc(version.getId());
        if (stages.isEmpty()) {
            throw new IllegalArgumentException("Pipeline has no stages configured");
        }

        WorkflowStage firstStage = stages.get(0);

        int totalSlaHours = stages.stream().mapToInt(s -> s.getEstimatedSlaHours() != null ? s.getEstimatedSlaHours() : 12).sum();
        LocalDateTime estimatedEta = LocalDateTime.now().plusHours(totalSlaHours);

        ProductionOrder order = ProductionOrder.builder()
                .tenantId(tenantId)
                .orderNumber(orderNumber)
                .brandId(request.getBrandId())
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .unit(request.getUnit() != null ? request.getUnit() : "pcs")
                .priority(request.getPriority() != null ? request.getPriority() : "MEDIUM")
                .status(OrderStatus.IN_PROGRESS)
                .currentStageId(firstStage.getId())
                .currentStageName(firstStage.getName())
                .currentStageSequence(firstStage.getSequenceOrder())
                .totalContractValue(request.getTotalContractValue() != null ? request.getTotalContractValue() : 0.0)
                .paymentStatus("PARTIAL")
                .targetCompletionDate(request.getTargetCompletionDate() != null ? request.getTargetCompletionDate() : estimatedEta)
                .estimatedDeliveryEta(estimatedEta)
                .notes(request.getNotes())
                .build();

        order = orderRepository.save(order);

        String operatorName = getCurrentUserFullName();
        OrderStageLog log = OrderStageLog.builder()
                .tenantId(tenantId)
                .orderId(order.getId())
                .stageId(firstStage.getId())
                .stageName(firstStage.getName())
                .operatorName(operatorName)
                .action("ORDER_CREATED")
                .notes("Order initialized and entered stage: " + firstStage.getName())
                .build();
        logRepository.save(log);

        return mapToOrderDto(order);
    }

    @Transactional
    public OrderDto transitionStage(String orderId, TransitionStageRequest request) {
        String tenantId = TenantContext.getCurrentTenant();
        ProductionOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        WorkflowStage targetStage = stageRepository.findById(request.getTargetStageId())
                .orElseThrow(() -> new IllegalArgumentException("Target stage not found: " + request.getTargetStageId()));

        String previousStageName = order.getCurrentStageName();
        order.setCurrentStageId(targetStage.getId());
        order.setCurrentStageName(targetStage.getName());
        order.setCurrentStageSequence(targetStage.getSequenceOrder());

        if ("DISPATCH".equalsIgnoreCase(targetStage.getCode()) || "Dispatch".equalsIgnoreCase(targetStage.getName())) {
            order.setStatus(OrderStatus.DISPATCHED);
            order.setActualDispatchDate(LocalDateTime.now());

            Notification dispatchNotif = Notification.builder()
                    .tenantId(tenantId)
                    .category(NotificationCategory.DISPATCH)
                    .title("Order Dispatched")
                    .message("Order " + order.getOrderNumber() + " (" + order.getProductName() + ") has been dispatched.")
                    .orderId(order.getId())
                    .orderNumber(order.getOrderNumber())
                    .build();
            notificationRepository.save(dispatchNotif);
        } else {
            order.setStatus(OrderStatus.IN_PROGRESS);
        }

        order = orderRepository.save(order);

        String operatorName = getCurrentUserFullName();
        OrderStageLog log = OrderStageLog.builder()
                .tenantId(tenantId)
                .orderId(order.getId())
                .stageId(targetStage.getId())
                .stageName(targetStage.getName())
                .operatorName(operatorName)
                .action("TRANSITIONED")
                .notes("Moved from " + previousStageName + " to " + targetStage.getName() + ". " + (request.getNotes() != null ? request.getNotes() : ""))
                .build();
        logRepository.save(log);

        return mapToOrderDto(order);
    }

    @Transactional
    public QCRecordDto submitQCOutcome(QCSubmitRequest request) {
        String tenantId = TenantContext.getCurrentTenant();
        ProductionOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + request.getOrderId()));

        String inspectorName = getCurrentUserFullName();

        QCRecord qc = QCRecord.builder()
                .tenantId(tenantId)
                .orderId(order.getId())
                .stageId(request.getStageId())
                .stageName(order.getCurrentStageName())
                .inspectorName(inspectorName)
                .passed(request.isPassed())
                .defectType(request.getDefectType())
                .sampleSize(request.getSampleSize() != null ? request.getSampleSize() : 50)
                .defectCount(request.getDefectCount() != null ? request.getDefectCount() : 0)
                .remarks(request.getRemarks())
                .build();

        qc = qcRepository.save(qc);

        if (!request.isPassed()) {
            order.setStatus(OrderStatus.BLOCKED);
            orderRepository.save(order);

            OrderStageLog log = OrderStageLog.builder()
                    .tenantId(tenantId)
                    .orderId(order.getId())
                    .stageId(request.getStageId())
                    .stageName(order.getCurrentStageName())
                    .operatorName(inspectorName)
                    .action("QC_FAILED")
                    .notes("QC Inspection Failed: " + request.getDefectType() + " (" + request.getDefectCount() + " defects). " + request.getRemarks())
                    .build();
            logRepository.save(log);

            Notification notif = Notification.builder()
                    .tenantId(tenantId)
                    .category(NotificationCategory.QC_FAILURE)
                    .title("QC Inspection Failure")
                    .message("Order " + order.getOrderNumber() + " failed QC check: " + request.getDefectType() + ". Order is now BLOCKED for rework.")
                    .orderId(order.getId())
                    .orderNumber(order.getOrderNumber())
                    .build();
            notificationRepository.save(notif);
        } else {
            OrderStageLog log = OrderStageLog.builder()
                    .tenantId(tenantId)
                    .orderId(order.getId())
                    .stageId(request.getStageId())
                    .stageName(order.getCurrentStageName())
                    .operatorName(inspectorName)
                    .action("QC_PASSED")
                    .notes("QC Inspection Passed successfully. " + (request.getRemarks() != null ? request.getRemarks() : ""))
                    .build();
            logRepository.save(log);
        }

        return mapToQCRecordDto(qc);
    }

    public OrderDto mapToOrderDto(ProductionOrder order) {
        String brandName = "Unassigned";
        if (order.getBrandId() != null) {
            Optional<Brand> b = brandRepository.findById(order.getBrandId());
            if (b.isPresent()) brandName = b.get().getName();
        }

        List<OrderStageLog> logs = logRepository.findByTenantIdAndOrderIdOrderByTimestampDesc(order.getTenantId(), order.getId());
        List<QCRecord> qcRecords = qcRepository.findByTenantIdAndOrderId(order.getTenantId(), order.getId());

        String color = "#3B82F6";
        if (order.getCurrentStageId() != null) {
            Optional<WorkflowStage> stg = stageRepository.findById(order.getCurrentStageId());
            if (stg.isPresent() && stg.get().getColorHex() != null) {
                color = stg.get().getColorHex();
            }
        }

        return OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .brandId(order.getBrandId())
                .brandName(brandName)
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .unit(order.getUnit())
                .priority(order.getPriority())
                .status(order.getStatus())
                .currentStageId(order.getCurrentStageId())
                .currentStageName(order.getCurrentStageName())
                .currentStageSequence(order.getCurrentStageSequence())
                .currentStageColor(color)
                .totalContractValue(order.getTotalContractValue())
                .paymentStatus(order.getPaymentStatus())
                .targetCompletionDate(order.getTargetCompletionDate())
                .estimatedDeliveryEta(order.getEstimatedDeliveryEta())
                .actualDispatchDate(order.getActualDispatchDate())
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .historyLogs(logs.stream().map(l -> OrderStageLogDto.builder()
                        .id(l.getId())
                        .stageId(l.getStageId())
                        .stageName(l.getStageName())
                        .operatorName(l.getOperatorName())
                        .action(l.getAction())
                        .notes(l.getNotes())
                        .timestamp(l.getTimestamp())
                        .build()).collect(Collectors.toList()))
                .qcRecords(qcRecords.stream().map(this::mapToQCRecordDto).collect(Collectors.toList()))
                .build();
    }

    private QCRecordDto mapToQCRecordDto(QCRecord qc) {
        return QCRecordDto.builder()
                .id(qc.getId())
                .stageName(qc.getStageName())
                .inspectorName(qc.getInspectorName())
                .passed(qc.isPassed())
                .defectType(qc.getDefectType())
                .sampleSize(qc.getSampleSize())
                .defectCount(qc.getDefectCount())
                .remarks(qc.getRemarks())
                .createdAt(qc.getCreatedAt())
                .build();
    }

    private String getCurrentUserFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            return p.getFullName();
        }
        return "System Operator";
    }
}
