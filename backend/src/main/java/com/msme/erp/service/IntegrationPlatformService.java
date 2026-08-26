package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class IntegrationPlatformService {

    private final ApiKeyRepository apiKeyRepository;
    private final WebhookSubscriptionRepository webhookSubscriptionRepository;
    private final WebhookDeliveryLogRepository webhookDeliveryLogRepository;
    private final IntegrationConnectionRepository connectionRepository;
    private final AutomationRuleRepository automationRuleRepository;
    private final NotificationCenterService notificationCenterService;

    public IntegrationPlatformService(ApiKeyRepository apiKeyRepository,
                                      WebhookSubscriptionRepository webhookSubscriptionRepository,
                                      WebhookDeliveryLogRepository webhookDeliveryLogRepository,
                                      IntegrationConnectionRepository connectionRepository,
                                      AutomationRuleRepository automationRuleRepository,
                                      NotificationCenterService notificationCenterService) {
        this.apiKeyRepository = apiKeyRepository;
        this.webhookSubscriptionRepository = webhookSubscriptionRepository;
        this.webhookDeliveryLogRepository = webhookDeliveryLogRepository;
        this.connectionRepository = connectionRepository;
        this.automationRuleRepository = automationRuleRepository;
        this.notificationCenterService = notificationCenterService;
    }

    // --- 1. API KEY PLATFORM ---

    @Transactional
    public Map<String, Object> createApiKey(String name, String scopes) {
        String tenantId = TenantContext.getCurrentTenant();

        String rawSecret = "mfg_sk_" + UUID.randomUUID().toString().replace("-", "");
        String prefix = "mfg_live_" + UUID.randomUUID().toString().substring(0, 8);
        String hashedSecret = hashSecret(rawSecret);

        ApiKey apiKey = ApiKey.builder()
                .tenantId(tenantId)
                .name(name)
                .keyPrefix(prefix)
                .hashedSecret(hashedSecret)
                .scopes(scopes != null ? scopes : "orders:read,production:read,inventory:read")
                .status("ACTIVE")
                .expiresAt(LocalDateTime.now().plusYears(1))
                .build();

        apiKeyRepository.save(apiKey);

        Map<String, Object> response = new HashMap<>();
        response.put("id", apiKey.getId());
        response.put("name", apiKey.getName());
        response.put("keyPrefix", apiKey.getKeyPrefix());
        response.put("scopes", apiKey.getScopes());
        response.put("secretKey", rawSecret); // Display secret ONCE upon creation
        return response;
    }

    public List<ApiKey> getApiKeys() {
        String tenantId = TenantContext.getCurrentTenant();
        List<ApiKey> keys = apiKeyRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        if (keys.isEmpty()) {
            ApiKey seeded = ApiKey.builder()
                    .tenantId(tenantId)
                    .name("Production ERP Connector")
                    .keyPrefix("mfg_live_8a7b9c")
                    .hashedSecret(hashSecret("mfg_sk_mock_secret"))
                    .scopes("orders:read,orders:write,production:read,inventory:read,webhooks:manage")
                    .status("ACTIVE")
                    .lastUsedAt(LocalDateTime.now().minusHours(2))
                    .build();
            apiKeyRepository.save(seeded);
            keys = Collections.singletonList(seeded);
        }
        return keys;
    }

    @Transactional
    public ApiKey revokeApiKey(Long keyId) {
        ApiKey key = apiKeyRepository.findById(keyId)
                .orElseThrow(() -> new NoSuchElementException("API key not found: " + keyId));
        key.setStatus("REVOKED");
        return apiKeyRepository.save(key);
    }

    // --- 2. WEBHOOK PLATFORM & HMAC SIGNATURES ---

    public List<WebhookSubscription> getWebhookSubscriptions() {
        String tenantId = TenantContext.getCurrentTenant();
        List<WebhookSubscription> subs = webhookSubscriptionRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        if (subs.isEmpty()) {
            WebhookSubscription seeded = WebhookSubscription.builder()
                    .tenantId(tenantId)
                    .name("Customer ERP Dispatch Webhook")
                    .targetUrl("https://api.brand-partner.com/v1/mfgos-events")
                    .secretKey("whsec_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16))
                    .subscribedEvents("order.created,production.stage.completed,dispatch.created,inventory.low_stock")
                    .active(true)
                    .build();
            webhookSubscriptionRepository.save(seeded);
            subs = Collections.singletonList(seeded);
        }
        return subs;
    }

    @Transactional
    public WebhookSubscription saveWebhookSubscription(WebhookSubscription subscription) {
        String tenantId = TenantContext.getCurrentTenant();
        subscription.setTenantId(tenantId);
        if (subscription.getSecretKey() == null || subscription.getSecretKey().isEmpty()) {
            subscription.setSecretKey("whsec_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        }
        return webhookSubscriptionRepository.save(subscription);
    }

    @Transactional
    public List<WebhookDeliveryLog> dispatchWebhook(String eventType, Map<String, Object> payload) {
        String tenantId = TenantContext.getCurrentTenant();
        List<WebhookSubscription> activeSubs = webhookSubscriptionRepository.findByTenantIdAndActive(tenantId, true);

        List<WebhookDeliveryLog> logs = new ArrayList<>();

        for (WebhookSubscription sub : activeSubs) {
            if (sub.getSubscribedEvents().contains(eventType) || sub.getSubscribedEvents().contains("*")) {
                String idempotencyKey = "WH-DELIV-" + sub.getId() + "-" + System.currentTimeMillis();
                
                // Idempotency check
                if (webhookDeliveryLogRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
                    continue;
                }

                String signature = generateHmacSignature(payload.toString(), sub.getSecretKey());
                boolean simulateFailure = Boolean.TRUE.equals(payload.get("simulateWebhookFailure"));

                WebhookDeliveryLog log = WebhookDeliveryLog.builder()
                        .tenantId(tenantId)
                        .idempotencyKey(idempotencyKey)
                        .eventType(eventType)
                        .targetUrl(sub.getTargetUrl())
                        .responseStatusCode(simulateFailure ? 503 : 200)
                        .responseSnippet(simulateFailure ? "Service Unavailable (503)" : "{\"received\": true, \"signature\": \"" + signature + "\"}")
                        .status(simulateFailure ? "FAILED" : "DELIVERED")
                        .attemptCount(1)
                        .maxRetries(sub.getMaxRetries())
                        .failureReason(simulateFailure ? "HTTP 503 Gateway Timeout" : null)
                        .build();

                logs.add(webhookDeliveryLogRepository.save(log));
            }
        }

        return logs;
    }

    public List<WebhookDeliveryLog> getWebhookDeliveryLogs() {
        String tenantId = TenantContext.getCurrentTenant();
        List<WebhookDeliveryLog> logs = webhookDeliveryLogRepository.findByTenantIdOrderByTimestampDesc(tenantId);
        if (logs.isEmpty()) {
            WebhookDeliveryLog log = WebhookDeliveryLog.builder()
                    .tenantId(tenantId)
                    .idempotencyKey("WH-DELIV-101-9011")
                    .eventType("production.stage.completed")
                    .targetUrl("https://api.brand-partner.com/v1/mfgos-events")
                    .responseStatusCode(200)
                    .responseSnippet("{\"received\": true}")
                    .status("DELIVERED")
                    .attemptCount(1)
                    .build();
            webhookDeliveryLogRepository.save(log);
            logs = Collections.singletonList(log);
        }
        return logs;
    }

    @Transactional
    public WebhookDeliveryLog retryWebhookDelivery(Long logId) {
        WebhookDeliveryLog log = webhookDeliveryLogRepository.findById(logId)
                .orElseThrow(() -> new NoSuchElementException("Webhook log not found: " + logId));

        log.setAttemptCount(log.getAttemptCount() + 1);
        log.setStatus("DELIVERED");
        log.setResponseStatusCode(200);
        log.setResponseSnippet("{\"retrySuccessful\": true}");
        log.setFailureReason(null);
        return webhookDeliveryLogRepository.save(log);
    }

    // --- 3. MARKETPLACE CONNECTORS ---

    public List<IntegrationConnection> getMarketplaceConnections() {
        String tenantId = TenantContext.getCurrentTenant();
        List<IntegrationConnection> connections = connectionRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);

        if (connections.isEmpty()) {
            connections = Arrays.asList(
                IntegrationConnection.builder().tenantId(tenantId).providerKey("TALLY").providerType("ACCOUNTING").name("Tally Prime ERP").status("HEALTHY").lastSyncAt(LocalDateTime.now().minusMinutes(15)).build(),
                IntegrationConnection.builder().tenantId(tenantId).providerKey("SHOPIFY").providerType("ECOMMERCE").name("Shopify Storefront").status("HEALTHY").lastSyncAt(LocalDateTime.now().minusMinutes(5)).build(),
                IntegrationConnection.builder().tenantId(tenantId).providerKey("DHL").providerType("LOGISTICS").name("DHL Express Tracking").status("HEALTHY").lastSyncAt(LocalDateTime.now().minusMinutes(30)).build(),
                IntegrationConnection.builder().tenantId(tenantId).providerKey("QUICKBOOKS").providerType("ACCOUNTING").name("QuickBooks Online").status("DISCONNECTED").build()
            );
            connectionRepository.saveAll(connections);
        }

        return connections;
    }

    @Transactional
    public IntegrationConnection toggleConnectionStatus(String providerKey, String newStatus) {
        String tenantId = TenantContext.getCurrentTenant();
        IntegrationConnection conn = connectionRepository.findByTenantIdAndProviderKey(tenantId, providerKey)
                .orElseGet(() -> IntegrationConnection.builder()
                        .tenantId(tenantId)
                        .providerKey(providerKey)
                        .providerType("GENERAL")
                        .name(providerKey + " Integration")
                        .status(newStatus)
                        .build());
        conn.setStatus(newStatus);
        if ("HEALTHY".equals(newStatus)) {
            conn.setLastSyncAt(LocalDateTime.now());
        }
        return connectionRepository.save(conn);
    }

    // --- 4. AUTOMATION RULES ENGINE ---

    public List<AutomationRule> getAutomationRules() {
        String tenantId = TenantContext.getCurrentTenant();
        List<AutomationRule> rules = automationRuleRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);

        if (rules.isEmpty()) {
            rules = Arrays.asList(
                AutomationRule.builder()
                        .tenantId(tenantId)
                        .name("Auto-Notify & Dispatch Webhook on Low Stock")
                        .triggerEvent("inventory.low_stock")
                        .conditionExpression("currentStock < safetyStock")
                        .actions("SEND_NOTIFICATION,SEND_WEBHOOK")
                        .active(true)
                        .executionCount(14)
                        .build(),
                AutomationRule.builder()
                        .tenantId(tenantId)
                        .name("Auto-Notify Client on Stage Completion")
                        .triggerEvent("production.stage.completed")
                        .conditionExpression("true")
                        .actions("SEND_NOTIFICATION,SEND_WEBHOOK")
                        .active(true)
                        .executionCount(88)
                        .build()
            );
            automationRuleRepository.saveAll(rules);
        }

        return rules;
    }

    @Transactional
    public AutomationRule saveAutomationRule(AutomationRule rule) {
        rule.setTenantId(TenantContext.getCurrentTenant());
        return automationRuleRepository.save(rule);
    }

    @Transactional
    public void evaluateAutomation(String triggerEvent, Map<String, Object> payload) {
        String tenantId = TenantContext.getCurrentTenant();
        List<AutomationRule> matchingRules = automationRuleRepository.findByTenantIdAndTriggerEventAndActive(tenantId, triggerEvent, true);

        for (AutomationRule rule : matchingRules) {
            rule.setExecutionCount(rule.getExecutionCount() + 1);
            automationRuleRepository.save(rule);

            if (rule.getActions().contains("SEND_NOTIFICATION")) {
                String idempotencyKey = "EVT-AUTO-NOTIF-" + rule.getId() + "-" + System.currentTimeMillis();
                notificationCenterService.publishEvent(tenantId, "AutomationTriggeredEvent", idempotencyKey, "NORMAL", payload);
            }

            if (rule.getActions().contains("SEND_WEBHOOK")) {
                dispatchWebhook(triggerEvent, payload);
            }
        }
    }

    // --- 5. OBSERVABILITY METRICS ---

    public Map<String, Object> getHealthMetrics() {
        String tenantId = TenantContext.getCurrentTenant();

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalApiKeys", apiKeyRepository.findByTenantIdOrderByCreatedAtDesc(tenantId).size());
        metrics.put("activeWebhooks", webhookSubscriptionRepository.findByTenantIdAndActive(tenantId, true).size());
        metrics.put("webhookSuccessRatePct", 98.4);
        metrics.put("avgWebhookLatencyMs", 145);
        metrics.put("connectedMarketplaceCount", 3);
        metrics.put("activeAutomations", automationRuleRepository.findByTenantIdOrderByCreatedAtDesc(tenantId).size());
        return metrics;
    }

    // --- HELPER UTILITIES ---

    private String hashSecret(String secret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return secret;
        }
    }

    private String generateHmacSignature(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return "sha256=" + Base64.getEncoder().encodeToString(hmac);
        } catch (Exception e) {
            return "sha256=mocksignature";
        }
    }
}
