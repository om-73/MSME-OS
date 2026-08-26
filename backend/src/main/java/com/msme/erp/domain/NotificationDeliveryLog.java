package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_delivery_logs", indexes = {
    @Index(name = "idx_notif_tenant_user", columnList = "tenantId, recipientId"),
    @Index(name = "idx_notif_idempotency", columnList = "idempotencyKey", unique = true)
})
public class NotificationDeliveryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private String eventType; // STAGE_COMPLETED, QC_FAILED, REWORK_REQUESTED, LOW_STOCK, SHIPMENT_DISPATCHED, etc.

    @Column(nullable = false)
    private String recipientId; // User email, phone, or client ID

    private String recipientRole; // ROLE_OPERATOR, ROLE_FACTORY_OWNER, ROLE_BRAND_CLIENT, etc.

    @Column(nullable = false)
    private String channel; // IN_APP, PUSH, EMAIL, SMS, WHATSAPP, WEBHOOK

    @Column(nullable = false)
    private String priority; // LOW, NORMAL, HIGH, CRITICAL

    @Column(nullable = false)
    private String status; // QUEUED, PROCESSING, SENT, DELIVERED, READ, FAILED, RETRYING, FAILED_PERMANENTLY

    private int retryCount = 0;
    private int maxRetries = 3;

    private String providerName; // META_WHATSAPP, TWILIO, SENDGRID_SMTP, FCM_PUSH
    private String providerMessageId;

    @Column(length = 1000)
    private String subject;

    @Column(length = 2000)
    private String body;

    @Column(length = 1000)
    private String failureReason;

    private String fallbackChannel;
    private LocalDateTime readAt;
    private LocalDateTime timestamp;

    public NotificationDeliveryLog() {}

    public NotificationDeliveryLog(Long id, String tenantId, String idempotencyKey, String eventType, String recipientId, String recipientRole, String channel, String priority, String status, int retryCount, int maxRetries, String providerName, String providerMessageId, String subject, String body, String failureReason, String fallbackChannel, LocalDateTime readAt, LocalDateTime timestamp) {
        this.id = id;
        this.tenantId = tenantId;
        this.idempotencyKey = idempotencyKey;
        this.eventType = eventType;
        this.recipientId = recipientId;
        this.recipientRole = recipientRole;
        this.channel = channel;
        this.priority = priority;
        this.status = status;
        this.retryCount = retryCount;
        this.maxRetries = maxRetries;
        this.providerName = providerName;
        this.providerMessageId = providerMessageId;
        this.subject = subject;
        this.body = body;
        this.failureReason = failureReason;
        this.fallbackChannel = fallbackChannel;
        this.readAt = readAt;
        this.timestamp = timestamp;
    }

    public static NotificationDeliveryLogBuilder builder() {
        return new NotificationDeliveryLogBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }
    public String getRecipientRole() { return recipientRole; }
    public void setRecipientRole(String recipientRole) { this.recipientRole = recipientRole; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public String getProviderMessageId() { return providerMessageId; }
    public void setProviderMessageId(String providerMessageId) { this.providerMessageId = providerMessageId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public String getFallbackChannel() { return fallbackChannel; }
    public void setFallbackChannel(String fallbackChannel) { this.fallbackChannel = fallbackChannel; }
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public static class NotificationDeliveryLogBuilder {
        private Long id;
        private String tenantId;
        private String idempotencyKey;
        private String eventType;
        private String recipientId;
        private String recipientRole;
        private String channel;
        private String priority = "NORMAL";
        private String status = "SENT";
        private int retryCount = 0;
        private int maxRetries = 3;
        private String providerName;
        private String providerMessageId;
        private String subject;
        private String body;
        private String failureReason;
        private String fallbackChannel;
        private LocalDateTime readAt;
        private LocalDateTime timestamp;

        public NotificationDeliveryLogBuilder id(Long id) { this.id = id; return this; }
        public NotificationDeliveryLogBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public NotificationDeliveryLogBuilder idempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; return this; }
        public NotificationDeliveryLogBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public NotificationDeliveryLogBuilder recipientId(String recipientId) { this.recipientId = recipientId; return this; }
        public NotificationDeliveryLogBuilder recipientRole(String recipientRole) { this.recipientRole = recipientRole; return this; }
        public NotificationDeliveryLogBuilder channel(String channel) { this.channel = channel; return this; }
        public NotificationDeliveryLogBuilder priority(String priority) { this.priority = priority; return this; }
        public NotificationDeliveryLogBuilder status(String status) { this.status = status; return this; }
        public NotificationDeliveryLogBuilder retryCount(int retryCount) { this.retryCount = retryCount; return this; }
        public NotificationDeliveryLogBuilder maxRetries(int maxRetries) { this.maxRetries = maxRetries; return this; }
        public NotificationDeliveryLogBuilder providerName(String providerName) { this.providerName = providerName; return this; }
        public NotificationDeliveryLogBuilder providerMessageId(String providerMessageId) { this.providerMessageId = providerMessageId; return this; }
        public NotificationDeliveryLogBuilder subject(String subject) { this.subject = subject; return this; }
        public NotificationDeliveryLogBuilder body(String body) { this.body = body; return this; }
        public NotificationDeliveryLogBuilder failureReason(String failureReason) { this.failureReason = failureReason; return this; }
        public NotificationDeliveryLogBuilder fallbackChannel(String fallbackChannel) { this.fallbackChannel = fallbackChannel; return this; }
        public NotificationDeliveryLogBuilder readAt(LocalDateTime readAt) { this.readAt = readAt; return this; }
        public NotificationDeliveryLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public NotificationDeliveryLog build() {
            return new NotificationDeliveryLog(id, tenantId, idempotencyKey, eventType, recipientId, recipientRole, channel, priority, status, retryCount, maxRetries, providerName, providerMessageId, subject, body, failureReason, fallbackChannel, readAt, timestamp);
        }
    }
}
