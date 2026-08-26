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
public class SaaSBillingServiceTest {

    @Mock
    private SubscriptionPlanRepository planRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private TenantInvoiceRepository invoiceRepository;

    @Mock
    private BillingAuditLogRepository auditLogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductionOrderRepository orderRepository;

    @Mock
    private NotificationCenterService notificationCenterService;

    @InjectMocks
    private SaaSBillingService billingService;

    private SubscriptionPlan starterPlan;
    private SubscriptionPlan proPlan;

    @BeforeEach
    void setUp() {
        starterPlan = SubscriptionPlan.builder()
                .planKey("STARTER")
                .name("Starter Factory")
                .monthlyPrice(99.0)
                .annualPrice(990.0)
                .maxUsers(10)
                .maxActiveOrders(50)
                .build();

        proPlan = SubscriptionPlan.builder()
                .planKey("PROFESSIONAL")
                .name("Professional Manufacturer")
                .monthlyPrice(299.0)
                .annualPrice(2990.0)
                .maxUsers(50)
                .maxActiveOrders(500)
                .build();
    }

    @Test
    void testUpgradeSubscriptionGeneratesInvoiceAndFiresModule9Event() {
        Subscription existingSub = Subscription.builder()
                .tenantId("apex-tenant-01")
                .planKey("STARTER")
                .status("TRIAL")
                .build();

        when(subscriptionRepository.findByTenantId(any())).thenReturn(Optional.of(existingSub));
        when(planRepository.findByPlanKey("PROFESSIONAL")).thenReturn(Optional.of(proPlan));
        when(subscriptionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Subscription upgraded = billingService.upgradeSubscription("PROFESSIONAL", "MONTHLY");

        assertEquals("PROFESSIONAL", upgraded.getPlanKey());
        assertEquals("ACTIVE", upgraded.getStatus());
        verify(invoiceRepository, times(1)).save(any(TenantInvoice.class));
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("SubscriptionUpgradedEvent"), any(), eq("HIGH"), any());
    }

    @Test
    void testDowngradeFailsWhenUsageExceedsTargetPlanLimits() {
        Subscription existingSub = Subscription.builder()
                .tenantId("apex-tenant-01")
                .planKey("PROFESSIONAL")
                .status("ACTIVE")
                .build();

        when(subscriptionRepository.findByTenantId(any())).thenReturn(Optional.of(existingSub));
        when(planRepository.findByPlanKey("STARTER")).thenReturn(Optional.of(starterPlan));
        when(planRepository.findAll()).thenReturn(Arrays.asList(starterPlan, proPlan));

        // Mock 15 active users (exceeds Starter limit of 10)
        List<User> activeUsers = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            activeUsers.add(new User());
        }
        when(userRepository.findByTenantId(any())).thenReturn(activeUsers);

        Map<String, Object> result = billingService.downgradeSubscription("STARTER");

        assertFalse((Boolean) result.get("success"));
        assertTrue((Boolean) result.get("requiresResolution"));
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void testPaymentFailedWebhookEntersGracePeriod() {
        Subscription existingSub = Subscription.builder()
                .tenantId("apex-tenant-01")
                .planKey("PROFESSIONAL")
                .status("ACTIVE")
                .build();

        when(subscriptionRepository.findByTenantId(any())).thenReturn(Optional.of(existingSub));

        Map<String, Object> webhookPayload = new HashMap<>();
        webhookPayload.put("type", "payment_intent.payment_failed");
        webhookPayload.put("tenantId", "apex-tenant-01");

        Map<String, Object> response = billingService.processWebhook("sig-123", webhookPayload);

        assertTrue((Boolean) response.get("processed"));
        assertEquals("GRACE_PERIOD", existingSub.getStatus());
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("PaymentFailedEvent"), any(), eq("CRITICAL"), any());
    }
}
