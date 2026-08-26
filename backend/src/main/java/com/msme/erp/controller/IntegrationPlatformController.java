package com.msme.erp.controller;

import com.msme.erp.domain.*;
import com.msme.erp.service.IntegrationPlatformService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/integration")
public class IntegrationPlatformController {

    private final IntegrationPlatformService integrationService;

    public IntegrationPlatformController(IntegrationPlatformService integrationService) {
        this.integrationService = integrationService;
    }

    @GetMapping("/api-keys")
    public ResponseEntity<List<ApiKey>> getApiKeys() {
        return ResponseEntity.ok(integrationService.getApiKeys());
    }

    @PostMapping("/api-keys")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> createApiKey(@RequestBody Map<String, String> payload) {
        String name = payload.getOrDefault("name", "New Integration API Key");
        String scopes = payload.getOrDefault("scopes", "orders:read,production:read,inventory:read");
        return ResponseEntity.ok(integrationService.createApiKey(name, scopes));
    }

    @PostMapping("/api-keys/{id}/revoke")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiKey> revokeApiKey(@PathVariable Long id) {
        return ResponseEntity.ok(integrationService.revokeApiKey(id));
    }

    @GetMapping("/webhooks")
    public ResponseEntity<List<WebhookSubscription>> getWebhooks() {
        return ResponseEntity.ok(integrationService.getWebhookSubscriptions());
    }

    @PostMapping("/webhooks")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<WebhookSubscription> saveWebhook(@RequestBody WebhookSubscription sub) {
        return ResponseEntity.ok(integrationService.saveWebhookSubscription(sub));
    }

    @PostMapping("/webhooks/dispatch")
    public ResponseEntity<List<WebhookDeliveryLog>> dispatchWebhook(@RequestBody Map<String, Object> payload) {
        String eventType = (String) payload.getOrDefault("eventType", "order.created");
        Map<String, Object> data = (Map<String, Object>) payload.getOrDefault("data", payload);
        return ResponseEntity.ok(integrationService.dispatchWebhook(eventType, data));
    }

    @GetMapping("/webhooks/logs")
    public ResponseEntity<List<WebhookDeliveryLog>> getWebhookLogs() {
        return ResponseEntity.ok(integrationService.getWebhookDeliveryLogs());
    }

    @PostMapping("/webhooks/logs/{id}/retry")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<WebhookDeliveryLog> retryWebhookDelivery(@PathVariable Long id) {
        return ResponseEntity.ok(integrationService.retryWebhookDelivery(id));
    }

    @GetMapping("/connections")
    public ResponseEntity<List<IntegrationConnection>> getMarketplaceConnections() {
        return ResponseEntity.ok(integrationService.getMarketplaceConnections());
    }

    @PostMapping("/connections/{providerKey}/toggle")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<IntegrationConnection> toggleConnection(@PathVariable String providerKey, @RequestBody Map<String, String> payload) {
        String newStatus = payload.getOrDefault("status", "HEALTHY");
        return ResponseEntity.ok(integrationService.toggleConnectionStatus(providerKey, newStatus));
    }

    @GetMapping("/automation-rules")
    public ResponseEntity<List<AutomationRule>> getAutomationRules() {
        return ResponseEntity.ok(integrationService.getAutomationRules());
    }

    @PostMapping("/automation-rules")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<AutomationRule> saveAutomationRule(@RequestBody AutomationRule rule) {
        return ResponseEntity.ok(integrationService.saveAutomationRule(rule));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealthMetrics() {
        return ResponseEntity.ok(integrationService.getHealthMetrics());
    }
}
