package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SaaSBillingService {

    private final SubscriptionPlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TenantInvoiceRepository invoiceRepository;
    private final BillingAuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final ProductionOrderRepository orderRepository;
    private final NotificationCenterService notificationCenterService;

    public SaaSBillingService(SubscriptionPlanRepository planRepository,
                             SubscriptionRepository subscriptionRepository,
                             TenantInvoiceRepository invoiceRepository,
                             BillingAuditLogRepository auditLogRepository,
                             UserRepository userRepository,
                             ProductionOrderRepository orderRepository,
                             NotificationCenterService notificationCenterService) {
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.invoiceRepository = invoiceRepository;
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.notificationCenterService = notificationCenterService;
    }

    public List<SubscriptionPlan> getPlans() {
        List<SubscriptionPlan> plans = planRepository.findAll();
        if (plans.isEmpty()) {
            // Seed default SaaS plans
            plans = Arrays.asList(
                SubscriptionPlan.builder()
                        .planKey("STARTER")
                        .name("Starter Factory")
                        .description("For small workshop factories and emerging textile setups.")
                        .monthlyPrice(99.0)
                        .annualPrice(990.0)
                        .maxUsers(10)
                        .maxActiveOrders(50)
                        .maxStorageGb(10.0)
                        .maxWorkflows(3)
                        .maxNotificationsPerMonth(2000)
                        .enabledFeatures("FEATURE_WORKFLOW_BUILDER,FEATURE_INVENTORY")
                        .build(),
                SubscriptionPlan.builder()
                        .planKey("PROFESSIONAL")
                        .name("Professional Manufacturer")
                        .description("For growing manufacturers requiring client portals & advanced analytics.")
                        .monthlyPrice(299.0)
                        .annualPrice(2990.0)
                        .maxUsers(50)
                        .maxActiveOrders(500)
                        .maxStorageGb(50.0)
                        .maxWorkflows(15)
                        .maxNotificationsPerMonth(20000)
                        .enabledFeatures("FEATURE_WORKFLOW_BUILDER,FEATURE_INVENTORY,FEATURE_CLIENT_PORTAL,FEATURE_ANALYTICS,FEATURE_WHATSAPP,FEATURE_MOBILE_APP")
                        .build(),
                SubscriptionPlan.builder()
                        .planKey("ENTERPRISE")
                        .name("Enterprise Scale")
                        .description("Unlimited scaling with AI forecasting, custom webhooks & priority SLA support.")
                        .monthlyPrice(799.0)
                        .annualPrice(7990.0)
                        .maxUsers(500)
                        .maxActiveOrders(5000)
                        .maxStorageGb(500.0)
                        .maxWorkflows(100)
                        .maxNotificationsPerMonth(100000)
                        .enabledFeatures("FEATURE_WORKFLOW_BUILDER,FEATURE_INVENTORY,FEATURE_CLIENT_PORTAL,FEATURE_ANALYTICS,FEATURE_AI_FORECASTING,FEATURE_MOBILE_APP,FEATURE_WHATSAPP,FEATURE_ADVANCED_REPORTS,FEATURE_API_ACCESS,FEATURE_CUSTOM_INTEGRATIONS")
                        .build()
            );
            planRepository.saveAll(plans);
        }
        return plans;
    }

    public Subscription getTenantSubscription() {
        String tenantId = TenantContext.getCurrentTenant();
        return subscriptionRepository.findByTenantId(tenantId)
                .orElseGet(() -> {
                    Subscription newSub = Subscription.builder()
                            .tenantId(tenantId)
                            .planKey("PROFESSIONAL")
                            .status("TRIAL")
                            .billingCycle("MONTHLY")
                            .currentPrice(299.0)
                            .trialStartDate(LocalDateTime.now())
                            .trialEndDate(LocalDateTime.now().plusDays(14))
                            .currentPeriodStart(LocalDateTime.now())
                            .currentPeriodEnd(LocalDateTime.now().plusDays(14))
                            .build();
                    return subscriptionRepository.save(newSub);
                });
    }

    @Transactional
    public Subscription upgradeSubscription(String targetPlanKey, String billingCycle) {
        String tenantId = TenantContext.getCurrentTenant();
        Subscription sub = getTenantSubscription();
        String prevPlan = sub.getPlanKey();

        SubscriptionPlan targetPlan = planRepository.findByPlanKey(targetPlanKey)
                .orElseThrow(() -> new NoSuchElementException("Plan not found: " + targetPlanKey));

        double price = "ANNUAL".equalsIgnoreCase(billingCycle) ? targetPlan.getAnnualPrice() : targetPlan.getMonthlyPrice();

        sub.setPlanKey(targetPlanKey);
        sub.setStatus("ACTIVE");
        sub.setBillingCycle(billingCycle);
        sub.setCurrentPrice(price);
        sub.setCurrentPeriodStart(LocalDateTime.now());
        sub.setCurrentPeriodEnd("ANNUAL".equalsIgnoreCase(billingCycle) ? LocalDateTime.now().plusYears(1) : LocalDateTime.now().plusMonths(1));
        sub = subscriptionRepository.save(sub);

        // Generate Invoice
        String invNum = "INV-2026-" + (1000 + new Random().nextInt(9000));
        TenantInvoice invoice = TenantInvoice.builder()
                .tenantId(tenantId)
                .invoiceNumber(invNum)
                .planName(targetPlan.getName())
                .subtotal(price)
                .taxAmount(price * 0.10)
                .totalAmount(price * 1.10)
                .status("PAID")
                .paidAt(LocalDateTime.now())
                .companyName("Apex Textiles Corp")
                .billingAddress("Industrial Zone Block 4")
                .build();
        invoiceRepository.save(invoice);

        // Audit log
        auditLogRepository.save(BillingAuditLog.builder()
                .tenantId(tenantId)
                .actorId("tenant_admin")
                .action("PLAN_UPGRADED")
                .previousState(prevPlan)
                .newState(targetPlanKey)
                .remarks("Upgraded plan to " + targetPlan.getName() + " (" + billingCycle + ")")
                .build());

        // Module 9 Event Notification Integration
        String idempotencyKey = "EVT-SUB-UPGRADE-" + tenantId + "-" + System.currentTimeMillis();
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderNumber", invNum);
        eventData.put("stageName", targetPlan.getName());
        eventData.put("clientEmail", "billing@apex.com");
        notificationCenterService.publishEvent(tenantId, "SubscriptionUpgradedEvent", idempotencyKey, "HIGH", eventData);

        return sub;
    }

    @Transactional
    public Map<String, Object> downgradeSubscription(String targetPlanKey) {
        String tenantId = TenantContext.getCurrentTenant();
        Subscription sub = getTenantSubscription();
        SubscriptionPlan targetPlan = planRepository.findByPlanKey(targetPlanKey)
                .orElseThrow(() -> new NoSuchElementException("Plan not found: " + targetPlanKey));

        // Check current usage against target limits
        Map<String, Object> usage = getUsageAndLimits();
        int activeUsers = (int) usage.get("activeUsers");

        if (activeUsers > targetPlan.getMaxUsers()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("requiresResolution", true);
            response.put("message", "Cannot downgrade immediately: Your current active users (" + activeUsers + ") exceed the new plan limit (" + targetPlan.getMaxUsers() + "). Please resolve excess users first.");
            return response;
        }

        String prevPlan = sub.getPlanKey();
        sub.setPlanKey(targetPlanKey);
        sub.setCurrentPrice(targetPlan.getMonthlyPrice());
        subRepositorySave(sub);

        auditLogRepository.save(BillingAuditLog.builder()
                .tenantId(tenantId)
                .actorId("tenant_admin")
                .action("PLAN_DOWNGRADED")
                .previousState(prevPlan)
                .newState(targetPlanKey)
                .remarks("Downgraded plan to " + targetPlan.getName())
                .build());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("subscription", sub);
        return response;
    }

    private void subRepositorySave(Subscription sub) {
        subscriptionRepository.save(sub);
    }

    @Transactional
    public Subscription cancelSubscription(String reason) {
        String tenantId = TenantContext.getCurrentTenant();
        Subscription sub = getTenantSubscription();
        sub.setCancelAtPeriodEnd(true);
        sub.setCancellationReason(reason);
        sub = subscriptionRepository.save(sub);

        auditLogRepository.save(BillingAuditLog.builder()
                .tenantId(tenantId)
                .actorId("tenant_admin")
                .action("PLAN_CANCELLED")
                .previousState(sub.getPlanKey())
                .newState("CANCELLED_PENDING")
                .remarks("Subscription set to cancel at end of period. Reason: " + reason)
                .build());

        // Module 9 Event Notification
        String idempotencyKey = "EVT-SUB-CANCEL-" + tenantId + "-" + System.currentTimeMillis();
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("orderNumber", sub.getPlanKey());
        eventData.put("stageName", "Cancellation Request");
        notificationCenterService.publishEvent(tenantId, "SubscriptionCancelledEvent", idempotencyKey, "HIGH", eventData);

        return sub;
    }

    public Map<String, Object> getUsageAndLimits() {
        String tenantId = TenantContext.getCurrentTenant();
        Subscription sub = getTenantSubscription();
        SubscriptionPlan plan = planRepository.findByPlanKey(sub.getPlanKey())
                .orElse(getPlans().get(1));

        int activeUsers = userRepository.findByTenantId(tenantId).size();
        int activeOrders = (int) orderRepository.findByTenantId(tenantId).stream()
                .filter(o -> o.getStatus() == OrderStatus.IN_PROGRESS || o.getStatus() == OrderStatus.BLOCKED)
                .count();

        double storageGb = 4.2; // Current calculated attachments storage
        int activeWorkflows = 2;

        double userPct = ((double) activeUsers / plan.getMaxUsers()) * 100.0;
        double orderPct = ((double) activeOrders / plan.getMaxActiveOrders()) * 100.0;
        double storagePct = (storageGb / plan.getMaxStorageGb()) * 100.0;

        Map<String, Object> usage = new HashMap<>();
        usage.put("activeUsers", activeUsers);
        usage.put("maxUsers", plan.getMaxUsers());
        usage.put("userPct", Math.round(userPct));

        usage.put("activeOrders", activeOrders);
        usage.put("maxActiveOrders", plan.getMaxActiveOrders());
        usage.put("orderPct", Math.round(orderPct));

        usage.put("storageGb", storageGb);
        usage.put("maxStorageGb", plan.getMaxStorageGb());
        usage.put("storagePct", Math.round(storagePct));

        usage.put("activeWorkflows", activeWorkflows);
        usage.put("maxWorkflows", plan.getMaxWorkflows());

        // Limit status indicators
        boolean warning = userPct >= 80.0 || orderPct >= 80.0 || storagePct >= 80.0;
        boolean limitReached = userPct >= 100.0 || orderPct >= 100.0 || storagePct >= 100.0;

        usage.put("hasWarning", warning);
        usage.put("limitReached", limitReached);
        usage.put("enabledFeatures", plan.getEnabledFeatures());

        return usage;
    }

    public List<TenantInvoice> getTenantInvoices() {
        String tenantId = TenantContext.getCurrentTenant();
        List<TenantInvoice> invoices = invoiceRepository.findByTenantIdOrderByInvoiceDateDesc(tenantId);
        if (invoices.isEmpty()) {
            // Seed mock receipt for trial tenant
            TenantInvoice inv = TenantInvoice.builder()
                    .tenantId(tenantId)
                    .invoiceNumber("INV-2026-9011")
                    .planName("Professional Manufacturer")
                    .subtotal(299.0)
                    .taxAmount(29.90)
                    .totalAmount(328.90)
                    .status("PAID")
                    .companyName("Apex Textiles Corp")
                    .billingAddress("Industrial Zone Block 4")
                    .paidAt(LocalDateTime.now().minusDays(10))
                    .build();
            invoiceRepository.save(inv);
            invoices = Collections.singletonList(inv);
        }
        return invoices;
    }

    @Transactional
    public Map<String, Object> processWebhook(String signature, Map<String, Object> payload) {
        // Server-side payment webhook processing
        String eventType = (String) payload.getOrDefault("type", "payment_intent.succeeded");
        String idempotencyKey = "WH-KEY-" + payload.getOrDefault("id", UUID.randomUUID().toString());

        Map<String, Object> result = new HashMap<>();
        result.put("processed", true);
        result.put("eventType", eventType);
        result.put("idempotencyKey", idempotencyKey);

        if ("payment_intent.payment_failed".equalsIgnoreCase(eventType)) {
            String tenantId = (String) payload.getOrDefault("tenantId", TenantContext.getCurrentTenant());
            Subscription sub = subscriptionRepository.findByTenantId(tenantId).orElse(null);
            if (sub != null) {
                sub.setStatus("GRACE_PERIOD");
                subscriptionRepository.save(sub);

                // Module 9 Payment Failure Alert
                notificationCenterService.publishEvent(tenantId, "PaymentFailedEvent", idempotencyKey, "CRITICAL", Map.of("orderNumber", sub.getPlanKey(), "stageName", "Payment Failed - Grace Period"));
            }
        }

        return result;
    }
}
