package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_delivery_logs", indexes = {
    @Index(name = "idx_wh_idempotency", columnList = "idempotencyKey", unique = true)
})
public class WebhookDeliveryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String targetUrl;

    private Integer responseStatusCode;

    @Column(length = 1000)
    private String responseSnippet;

    @Column(nullable = false)
    private String status; // QUEUED, DELIVERED, FAILED, RETRYING

    private int attemptCount = 1;
    private int maxRetries = 3;

    @Column(length = 1000)
    private String failureReason;

    private LocalDateTime nextRetryAt;
    private LocalDateTime timestamp;

    public WebhookDeliveryLog() {}

    public WebhookDeliveryLog(Long id, String tenantId, String idempotencyKey, String eventType, String targetUrl, Integer responseStatusCode, String responseSnippet, String status, int attemptCount, int maxRetries, String failureReason, LocalDateTime nextRetryAt, LocalDateTime timestamp) {
        this.id = id;
        this.tenantId = tenantId;
        this.idempotencyKey = idempotencyKey;
        this.eventType = eventType;
        this.targetUrl = targetUrl;
        this.responseStatusCode = responseStatusCode;
        this.responseSnippet = responseSnippet;
        this.status = status;
        this.attemptCount = attemptCount;
        this.maxRetries = maxRetries;
        this.failureReason = failureReason;
        this.nextRetryAt = nextRetryAt;
        this.timestamp = timestamp;
    }

    public static WebhookDeliveryLogBuilder builder() {
        return new WebhookDeliveryLogBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getTargetUrl() { return targetUrl; }
    public void setTargetUrl(String targetUrl) { this.targetUrl = targetUrl; }
    public Integer getResponseStatusCode() { return responseStatusCode; }
    public void setResponseStatusCode(Integer responseStatusCode) { this.responseStatusCode = responseStatusCode; }
    public String getResponseSnippet() { return responseSnippet; }
    public void setResponseSnippet(String responseSnippet) { this.responseSnippet = responseSnippet; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getAttemptCount() { return attemptCount; }
    public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public LocalDateTime getNextRetryAt() { return nextRetryAt; }
    public void setNextRetryAt(LocalDateTime nextRetryAt) { this.nextRetryAt = nextRetryAt; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public static class WebhookDeliveryLogBuilder {
        private Long id;
        private String tenantId;
        private String idempotencyKey;
        private String eventType;
        private String targetUrl;
        private Integer responseStatusCode;
        private String responseSnippet;
        private String status = "DELIVERED";
        private int attemptCount = 1;
        private int maxRetries = 3;
        private String failureReason;
        private LocalDateTime nextRetryAt;
        private LocalDateTime timestamp;

        public WebhookDeliveryLogBuilder id(Long id) { this.id = id; return this; }
        public WebhookDeliveryLogBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public WebhookDeliveryLogBuilder idempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; return this; }
        public WebhookDeliveryLogBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public WebhookDeliveryLogBuilder targetUrl(String targetUrl) { this.targetUrl = targetUrl; return this; }
        public WebhookDeliveryLogBuilder responseStatusCode(Integer responseStatusCode) { this.responseStatusCode = responseStatusCode; return this; }
        public WebhookDeliveryLogBuilder responseSnippet(String responseSnippet) { this.responseSnippet = responseSnippet; return this; }
        public WebhookDeliveryLogBuilder status(String status) { this.status = status; return this; }
        public WebhookDeliveryLogBuilder attemptCount(int attemptCount) { this.attemptCount = attemptCount; return this; }
        public WebhookDeliveryLogBuilder maxRetries(int maxRetries) { this.maxRetries = maxRetries; return this; }
        public WebhookDeliveryLogBuilder failureReason(String failureReason) { this.failureReason = failureReason; return this; }
        public WebhookDeliveryLogBuilder nextRetryAt(LocalDateTime nextRetryAt) { this.nextRetryAt = nextRetryAt; return this; }
        public WebhookDeliveryLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public WebhookDeliveryLog build() {
            return new WebhookDeliveryLog(id, tenantId, idempotencyKey, eventType, targetUrl, responseStatusCode, responseSnippet, status, attemptCount, maxRetries, failureReason, nextRetryAt, timestamp);
        }
    }
}
