package com.msme.erp.controller;

import com.msme.erp.domain.Subscription;
import com.msme.erp.domain.SubscriptionPlan;
import com.msme.erp.domain.TenantInvoice;
import com.msme.erp.service.SaaSBillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/billing")
public class SaaSBillingController {

    private final SaaSBillingService billingService;

    public SaaSBillingController(SaaSBillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/plans")
    public ResponseEntity<List<SubscriptionPlan>> getPlans() {
        return ResponseEntity.ok(billingService.getPlans());
    }

    @GetMapping("/subscription")
    public ResponseEntity<Subscription> getSubscription() {
        return ResponseEntity.ok(billingService.getTenantSubscription());
    }

    @PostMapping("/subscription/upgrade")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Subscription> upgradeSubscription(@RequestBody Map<String, String> payload) {
        String planKey = payload.get("planKey");
        String billingCycle = payload.getOrDefault("billingCycle", "MONTHLY");
        return ResponseEntity.ok(billingService.upgradeSubscription(planKey, billingCycle));
    }

    @PostMapping("/subscription/downgrade")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> downgradeSubscription(@RequestBody Map<String, String> payload) {
        String planKey = payload.get("planKey");
        return ResponseEntity.ok(billingService.downgradeSubscription(planKey));
    }

    @PostMapping("/subscription/cancel")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Subscription> cancelSubscription(@RequestBody Map<String, String> payload) {
        String reason = payload.getOrDefault("reason", "No longer required");
        return ResponseEntity.ok(billingService.cancelSubscription(reason));
    }

    @GetMapping("/usage")
    public ResponseEntity<Map<String, Object>> getUsageAndLimits() {
        return ResponseEntity.ok(billingService.getUsageAndLimits());
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<TenantInvoice>> getInvoices() {
        return ResponseEntity.ok(billingService.getTenantInvoices());
    }

    @PostMapping("/webhooks")
    public ResponseEntity<Map<String, Object>> handleWebhook(
            @RequestHeader(value = "Stripe-Signature", required = false) String signature,
            @RequestBody Map<String, Object> payload
    ) {
        return ResponseEntity.ok(billingService.processWebhook(signature, payload));
    }
}
