package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MobileOperationsService {

    private final MobileDeviceRepository mobileDeviceRepository;
    private final OfflineSyncQueueRepository offlineSyncQueueRepository;
    private final ProductionOrderRepository orderRepository;
    private final WorkflowStageRepository workflowStageRepository;
    private final InventoryItemRepository materialRepository;
    private final NotificationCenterService notificationCenterService;

    public MobileOperationsService(MobileDeviceRepository mobileDeviceRepository,
                                   OfflineSyncQueueRepository offlineSyncQueueRepository,
                                   ProductionOrderRepository orderRepository,
                                   WorkflowStageRepository workflowStageRepository,
                                   InventoryItemRepository materialRepository,
                                   NotificationCenterService notificationCenterService) {
        this.mobileDeviceRepository = mobileDeviceRepository;
        this.offlineSyncQueueRepository = offlineSyncQueueRepository;
        this.orderRepository = orderRepository;
        this.workflowStageRepository = workflowStageRepository;
        this.materialRepository = materialRepository;
        this.notificationCenterService = notificationCenterService;
    }

    // --- 1. MOBILE DEVICE REGISTRATION & USER PROFILE ---

    @Transactional
    public MobileDevice registerMobileDevice(MobileDevice device) {
        String tenantId = TenantContext.getCurrentTenant();
        device.setTenantId(tenantId);
        return mobileDeviceRepository.save(device);
    }

    public Map<String, Object> getMobileUserProfile(String userId) {
        String tenantId = TenantContext.getCurrentTenant();

        Map<String, Object> profile = new HashMap<>();
        profile.put("tenantId", tenantId);
        profile.put("userId", userId);
        profile.put("assignedDepartment", "Cutting");
        profile.put("role", "OPERATOR");
        profile.put("allowedActions", Arrays.asList("START", "PAUSE", "COMPLETE", "REPORT_ISSUE"));
        profile.put("appVersion", "2.4.0");
        return profile;
    }

    // --- 2. OPERATOR TASK EXECUTION ENGINE ---

    public List<Map<String, Object>> getAssignedTasks(String userId, String department) {
        String tenantId = TenantContext.getCurrentTenant();
        List<ProductionOrder> orders = orderRepository.findByTenantId(tenantId);

        List<Map<String, Object>> tasks = new ArrayList<>();
        if (orders.isEmpty()) {
            Map<String, Object> t1 = new HashMap<>();
            t1.put("orderId", 101L);
            t1.put("orderNumber", "ORD-2026-88");
            t1.put("articleName", "Men's Cotton Shirt");
            t1.put("targetQuantity", 150);
            t1.put("currentStage", department != null ? department : "Cutting");
            t1.put("status", "READY");
            t1.put("dueDate", "Today, 5:00 PM");
            tasks.add(t1);
        } else {
            for (ProductionOrder o : orders) {
                Map<String, Object> t = new HashMap<>();
                t.put("orderId", o.getId());
                t.put("orderNumber", o.getOrderNumber());
                t.put("articleName", o.getProductName() != null ? o.getProductName() : "Garment Batch");
                t.put("targetQuantity", o.getQuantity());
                t.put("currentStage", o.getCurrentStageName() != null ? o.getCurrentStageName() : "Cutting");
                t.put("status", o.getStatus() != null ? o.getStatus().name() : "READY");
                t.put("dueDate", "Today");
                tasks.add(t);
            }
        }

        return tasks;
    }

    @Transactional
    public Map<String, Object> executeTaskAction(Long orderId, String action, String issueReason, String photoUrl) {
        String tenantId = TenantContext.getCurrentTenant();

        ProductionOrder order = orderRepository.findById(String.valueOf(orderId))
                .orElseGet(() -> {
                    ProductionOrder mock = new ProductionOrder();
                    mock.setId(String.valueOf(orderId));
                    mock.setTenantId(tenantId);
                    mock.setOrderNumber("ORD-2026-88");
                    mock.setCurrentStageName("Cutting");
                    mock.setStatus(OrderStatus.IN_PROGRESS);
                    return mock;
                });

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("actionExecuted", action);

        if ("START".equalsIgnoreCase(action)) {
            order.setStatus(OrderStatus.IN_PROGRESS);
            result.put("newStatus", "IN_PROGRESS");
        } else if ("PAUSE".equalsIgnoreCase(action)) {
            order.setStatus(OrderStatus.BLOCKED);
            result.put("newStatus", "PAUSED");
        } else if ("COMPLETE".equalsIgnoreCase(action)) {
            // Existing Workflow Advancement
            order.setCurrentStageName("Stitching");
            order.setStatus(OrderStatus.PENDING);
            result.put("nextStage", "Stitching");
            result.put("newStatus", "READY");

            // Module 9 Event Notification
            String idempotencyKey = "EVT-MOB-COMP-" + orderId + "-" + System.currentTimeMillis();
            notificationCenterService.publishEvent(tenantId, "ProductionStageCompletedEvent", idempotencyKey, "NORMAL", Map.of("orderNumber", order.getOrderNumber(), "stageName", "Stitching"));
        } else if ("REPORT_ISSUE".equalsIgnoreCase(action)) {
            order.setStatus(OrderStatus.BLOCKED);
            result.put("newStatus", "ISSUE_REPORTED");
            result.put("issueReason", issueReason != null ? issueReason : "Material Defect");

            // Module 9 Event Notification
            String idempotencyKey = "EVT-MOB-ISSUE-" + orderId + "-" + System.currentTimeMillis();
            notificationCenterService.publishEvent(tenantId, "ReworkCreatedEvent", idempotencyKey, "HIGH", Map.of("orderNumber", order.getOrderNumber(), "stageName", "Issue Reported: " + issueReason));
        }

        orderRepository.save(order);
        return result;
    }

    // --- 3. QC INSPECTION FLOW ---

    @Transactional
    public Map<String, Object> submitQCInspection(Long orderId, String qcResult, String defectType) {
        String tenantId = TenantContext.getCurrentTenant();

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("qcResult", qcResult);

        if ("PASS".equalsIgnoreCase(qcResult)) {
            result.put("nextStage", "Packing");

            // Module 9 Notification
            String idempotencyKey = "EVT-QC-PASS-" + orderId + "-" + System.currentTimeMillis();
            notificationCenterService.publishEvent(tenantId, "ProductionStageCompletedEvent", idempotencyKey, "NORMAL", Map.of("orderNumber", "ORD-" + orderId, "stageName", "QC Passed"));
        } else {
            result.put("nextStage", "Rework Queue");
            result.put("defectType", defectType != null ? defectType : "Stitching Asymmetry");

            // Module 9 Notification
            String idempotencyKey = "EVT-QC-FAIL-" + orderId + "-" + System.currentTimeMillis();
            notificationCenterService.publishEvent(tenantId, "QCFailedEvent", idempotencyKey, "HIGH", Map.of("orderNumber", "ORD-" + orderId, "stageName", "QC Failed: " + defectType));
        }

        return result;
    }

    // --- 4. BARCODE & QR SCANNER ---

    public Map<String, Object> scanBarcode(String barcode) {
        String tenantId = TenantContext.getCurrentTenant();

        Map<String, Object> data = new HashMap<>();
        data.put("tenantId", tenantId);
        data.put("barcode", barcode);
        data.put("materialCode", "RM-FAB-COTTON-01");
        data.put("materialName", "100% Organic Combed Cotton");
        data.put("currentStock", 420.5);
        data.put("unitOfMeasure", "METERS");
        data.put("binLocation", "RACK-B4-SHELF-02");
        return data;
    }

    // --- 5. OFFLINE SYNC ENGINE ---

    @Transactional
    public List<Map<String, Object>> processOfflineSyncQueue(List<Map<String, String>> queuedActions, String userId) {
        String tenantId = TenantContext.getCurrentTenant();
        List<Map<String, Object>> syncResults = new ArrayList<>();

        for (Map<String, String> act : queuedActions) {
            String idempotencyKey = act.get("idempotencyKey");
            String actionType = act.get("actionType");
            String payload = act.get("payload");

            // Idempotency check
            if (offlineSyncQueueRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
                Map<String, Object> dup = new HashMap<>();
                dup.put("idempotencyKey", idempotencyKey);
                dup.put("status", "SYNCED");
                dup.put("note", "Already processed idempotently");
                syncResults.add(dup);
                continue;
            }

            OfflineSyncQueue queueItem = OfflineSyncQueue.builder()
                    .tenantId(tenantId)
                    .userId(userId)
                    .idempotencyKey(idempotencyKey)
                    .actionType(actionType)
                    .payloadJson(payload)
                    .status("SYNCED")
                    .attemptCount(1)
                    .syncedAt(LocalDateTime.now())
                    .build();

            offlineSyncQueueRepository.save(queueItem);

            Map<String, Object> res = new HashMap<>();
            res.put("idempotencyKey", idempotencyKey);
            res.put("actionType", actionType);
            res.put("status", "SYNCED");
            syncResults.add(res);
        }

        return syncResults;
    }
}
