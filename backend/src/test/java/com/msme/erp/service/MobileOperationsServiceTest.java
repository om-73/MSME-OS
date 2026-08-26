package com.msme.erp.service;

import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MobileOperationsServiceTest {

    @Mock
    private MobileDeviceRepository mobileDeviceRepository;

    @Mock
    private OfflineSyncQueueRepository offlineSyncQueueRepository;

    @Mock
    private ProductionOrderRepository orderRepository;

    @Mock
    private WorkflowStageRepository workflowStageRepository;

    @Mock
    private InventoryItemRepository materialRepository;

    @Mock
    private NotificationCenterService notificationCenterService;

    @InjectMocks
    private MobileOperationsService mobileService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testExecuteTaskActionCompleteAdvancesWorkflowStageAndFiresNotification() {
        ProductionOrder order = new ProductionOrder();
        order.setId("101");
        order.setOrderNumber("ORD-2026-88");
        order.setCurrentStageName("Cutting");
        order.setStatus(OrderStatus.IN_PROGRESS);

        when(orderRepository.findById("101")).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Map<String, Object> result = mobileService.executeTaskAction(101L, "COMPLETE", null, null);

        assertEquals("Stitching", result.get("nextStage"));
        assertEquals("READY", result.get("newStatus"));
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("ProductionStageCompletedEvent"), any(), eq("NORMAL"), any());
    }

    @Test
    void testSubmitQCInspectionFailFiresQCFailedNotification() {
        Map<String, Object> result = mobileService.submitQCInspection(101L, "FAIL", "Stitching Asymmetry");

        assertEquals("Rework Queue", result.get("nextStage"));
        assertEquals("Stitching Asymmetry", result.get("defectType"));
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("QCFailedEvent"), any(), eq("HIGH"), any());
    }

    @Test
    void testProcessOfflineSyncQueueProcessesActionsIdempotently() {
        Map<String, String> act = Map.of(
                "idempotencyKey", "OFF-1001",
                "actionType", "TASK_COMPLETE",
                "payload", "{\"orderId\": 101}"
        );

        when(offlineSyncQueueRepository.findByIdempotencyKey("OFF-1001")).thenReturn(Optional.empty());

        List<Map<String, Object>> results = mobileService.processOfflineSyncQueue(Collections.singletonList(act), "worker@apex.com");

        assertEquals(1, results.size());
        assertEquals("SYNCED", results.get(0).get("status"));
        verify(offlineSyncQueueRepository, times(1)).save(any());
    }
}
