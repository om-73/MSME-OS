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
public class IntegrationPlatformServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private WebhookSubscriptionRepository webhookSubscriptionRepository;

    @Mock
    private WebhookDeliveryLogRepository webhookDeliveryLogRepository;

    @Mock
    private IntegrationConnectionRepository connectionRepository;

    @Mock
    private AutomationRuleRepository automationRuleRepository;

    @Mock
    private NotificationCenterService notificationCenterService;

    @InjectMocks
    private IntegrationPlatformService integrationService;

    @BeforeEach
    void setUp() {
        com.msme.erp.config.TenantContext.setCurrentTenant("apex-tenant-01");
    }

    @Test
    void testCreateApiKeyReturnsUnmaskedSecretOnceAndHashesForStorage() {
        when(apiKeyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Map<String, Object> result = integrationService.createApiKey("Partner Integration", "orders:read,production:read");

        assertNotNull(result);
        assertTrue(((String) result.get("secretKey")).startsWith("mfg_sk_"));
        assertTrue(((String) result.get("keyPrefix")).startsWith("mfg_live_"));
        assertEquals("orders:read,production:read", result.get("scopes"));
        verify(apiKeyRepository, times(1)).save(any(ApiKey.class));
    }

    @Test
    void testWebhookDispatchGeneratesHmacSignedLog() {
        WebhookSubscription sub = WebhookSubscription.builder()
                .id(1L)
                .tenantId("apex-tenant-01")
                .targetUrl("https://hooks.partner.com/events")
                .secretKey("whsec_secret_123")
                .subscribedEvents("order.created")
                .active(true)
                .build();

        when(webhookSubscriptionRepository.findByTenantIdAndActive("apex-tenant-01", true))
                .thenReturn(Collections.singletonList(sub));
        when(webhookDeliveryLogRepository.findByIdempotencyKey(any())).thenReturn(Optional.empty());
        when(webhookDeliveryLogRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        List<WebhookDeliveryLog> logs = integrationService.dispatchWebhook("order.created", Map.of("orderNumber", "ORD-1001"));

        assertEquals(1, logs.size());
        assertEquals("DELIVERED", logs.get(0).getStatus());
        assertEquals(200, logs.get(0).getResponseStatusCode());
    }

    @Test
    void testAutomationRuleEvaluationTriggersModule9NotificationAndWebhook() {
        AutomationRule rule = AutomationRule.builder()
                .id(5L)
                .tenantId("apex-tenant-01")
                .name("Low Stock Alert")
                .triggerEvent("inventory.low_stock")
                .actions("SEND_NOTIFICATION,SEND_WEBHOOK")
                .active(true)
                .executionCount(0)
                .build();

        when(automationRuleRepository.findByTenantIdAndTriggerEventAndActive("apex-tenant-01", "inventory.low_stock", true))
                .thenReturn(Collections.singletonList(rule));

        integrationService.evaluateAutomation("inventory.low_stock", Map.of("materialCode", "RM-TH-01"));

        assertEquals(1, rule.getExecutionCount());
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("AutomationTriggeredEvent"), any(), eq("NORMAL"), any());
    }
}
