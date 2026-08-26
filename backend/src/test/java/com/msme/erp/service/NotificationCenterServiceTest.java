package com.msme.erp.service;

import com.msme.erp.domain.NotificationDeliveryLog;
import com.msme.erp.domain.NotificationPreference;
import com.msme.erp.domain.NotificationTemplate;
import com.msme.erp.repository.NotificationDeliveryLogRepository;
import com.msme.erp.repository.NotificationPreferenceRepository;
import com.msme.erp.repository.NotificationTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationCenterServiceTest {

    @Mock
    private NotificationDeliveryLogRepository deliveryLogRepository;

    @Mock
    private NotificationTemplateRepository templateRepository;

    @Mock
    private NotificationPreferenceRepository preferenceRepository;

    @InjectMocks
    private NotificationCenterService notificationCenterService;

    private String tenantId = "apex-tenant-01";

    @BeforeEach
    void setUp() {
    }

    @Test
    void testIdempotencyPreventsDuplicateNotifications() {
        String key = "EVT-IDEMPOTENT-1001";
        NotificationDeliveryLog existing = NotificationDeliveryLog.builder()
                .tenantId(tenantId)
                .idempotencyKey(key)
                .eventType("STAGE_COMPLETED")
                .recipientId("manager@apex.com")
                .channel("IN_APP")
                .status("SENT")
                .build();

        when(deliveryLogRepository.findByIdempotencyKey(key)).thenReturn(Optional.of(existing));

        List<NotificationDeliveryLog> logs = notificationCenterService.publishEvent(tenantId, "STAGE_COMPLETED", key, "NORMAL", new HashMap<>());

        assertEquals(1, logs.size());
        assertEquals(existing, logs.get(0));
        verify(deliveryLogRepository, never()).save(any());
    }

    @Test
    void testStageCompletedRoutingToNextDepartmentManager() {
        String key = "EVT-STAGE-9011";
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderNumber", "ORD-2026-90");
        payload.put("stageName", "Cutting");
        payload.put("nextManagerEmail", "stitching.mgr@apex.com");

        when(deliveryLogRepository.findByIdempotencyKey(key)).thenReturn(Optional.empty());
        when(deliveryLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<NotificationDeliveryLog> logs = notificationCenterService.publishEvent(tenantId, "STAGE_COMPLETED", key, "NORMAL", payload);

        assertFalse(logs.isEmpty());
        NotificationDeliveryLog log = logs.get(0);
        assertEquals("stitching.mgr@apex.com", log.getRecipientId());
        assertEquals("NORMAL", log.getPriority());
    }

    @Test
    void testQCFailureTriggersReworkRoutingAndWhatsAppChannel() {
        String key = "EVT-QC-FAIL-77";
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderNumber", "ORD-2026-99");
        payload.put("qcLeadEmail", "qc.lead@apex.com");

        when(deliveryLogRepository.findByIdempotencyKey(key)).thenReturn(Optional.empty());
        when(deliveryLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<NotificationDeliveryLog> logs = notificationCenterService.publishEvent(tenantId, "QC_FAILED", key, "HIGH", payload);

        assertTrue(logs.stream().anyMatch(l -> "qc.lead@apex.com".equals(l.getRecipientId()) && "WHATSAPP".equals(l.getChannel())));
    }

    @Test
    void testClientPrivacySanitizesInternalWorkerDetails() {
        String key = "EVT-DISPATCH-55";
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderNumber", "ORD-2026-100");
        payload.put("clientEmail", "client@brand.com");
        payload.put("workerName", "Internal Operator 42");

        when(deliveryLogRepository.findByIdempotencyKey(key)).thenReturn(Optional.empty());
        when(deliveryLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<NotificationDeliveryLog> logs = notificationCenterService.publishEvent(tenantId, "SHIPMENT_DISPATCHED", key, "NORMAL", payload);

        Optional<NotificationDeliveryLog> clientLog = logs.stream().filter(l -> "ROLE_BRAND_CLIENT".equals(l.getRecipientRole())).findFirst();
        assertTrue(clientLog.isPresent());
        assertFalse(clientLog.get().getBody().contains("Internal Operator 42"));
    }

    @Test
    void testRetryMechanismIncrementsRetryCount() {
        Long logId = 101L;
        NotificationDeliveryLog log = NotificationDeliveryLog.builder()
                .id(logId)
                .tenantId(tenantId)
                .status("FAILED")
                .retryCount(1)
                .maxRetries(3)
                .build();

        when(deliveryLogRepository.findById(logId)).thenReturn(Optional.of(log));
        when(deliveryLogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        NotificationDeliveryLog retried = notificationCenterService.retryDelivery(logId);

        assertEquals("SENT", retried.getStatus());
        assertEquals(2, retried.getRetryCount());
    }
}
