package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_subscriptions")
public class WebhookSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String targetUrl;

    @Column(nullable = false)
    private String secretKey; // Used for HMAC-SHA256 signatures

    @Column(length = 1000, nullable = false)
    private String subscribedEvents; // order.created,production.stage.completed,qc.failed,inventory.low_stock

    private boolean active = true;
    private int timeoutMs = 5000;
    private int maxRetries = 3;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WebhookSubscription() {}

    public WebhookSubscription(Long id, String tenantId, String name, String targetUrl, String secretKey, String subscribedEvents, boolean active, int timeoutMs, int maxRetries, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.targetUrl = targetUrl;
        this.secretKey = secretKey;
        this.subscribedEvents = subscribedEvents;
        this.active = active;
        this.timeoutMs = timeoutMs;
        this.maxRetries = maxRetries;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static WebhookSubscriptionBuilder builder() {
        return new WebhookSubscriptionBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTargetUrl() { return targetUrl; }
    public void setTargetUrl(String targetUrl) { this.targetUrl = targetUrl; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getSubscribedEvents() { return subscribedEvents; }
    public void setSubscribedEvents(String subscribedEvents) { this.subscribedEvents = subscribedEvents; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public int getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static class WebhookSubscriptionBuilder {
        private Long id;
        private String tenantId;
        private String name;
        private String targetUrl;
        private String secretKey;
        private String subscribedEvents;
        private boolean active = true;
        private int timeoutMs = 5000;
        private int maxRetries = 3;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public WebhookSubscriptionBuilder id(Long id) { this.id = id; return this; }
        public WebhookSubscriptionBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public WebhookSubscriptionBuilder name(String name) { this.name = name; return this; }
        public WebhookSubscriptionBuilder targetUrl(String targetUrl) { this.targetUrl = targetUrl; return this; }
        public WebhookSubscriptionBuilder secretKey(String secretKey) { this.secretKey = secretKey; return this; }
        public WebhookSubscriptionBuilder subscribedEvents(String subscribedEvents) { this.subscribedEvents = subscribedEvents; return this; }
        public WebhookSubscriptionBuilder active(boolean active) { this.active = active; return this; }
        public WebhookSubscriptionBuilder timeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; return this; }
        public WebhookSubscriptionBuilder maxRetries(int maxRetries) { this.maxRetries = maxRetries; return this; }
        public WebhookSubscriptionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public WebhookSubscriptionBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public WebhookSubscription build() {
            return new WebhookSubscription(id, tenantId, name, targetUrl, secretKey, subscribedEvents, active, timeoutMs, maxRetries, createdAt, updatedAt);
        }
    }
}
