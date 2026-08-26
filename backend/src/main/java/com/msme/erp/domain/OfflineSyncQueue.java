package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "offline_sync_queue", indexes = {
    @Index(name = "idx_offline_idempotency", columnList = "idempotencyKey", unique = true)
})
public class OfflineSyncQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private String actionType; // TASK_START, TASK_COMPLETE, REPORT_ISSUE, QC_INSPECT, STOCK_SCAN

    @Column(length = 2000, nullable = false)
    private String payloadJson;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, SYNCED, CONFLICT, FAILED

    private int attemptCount = 0;

    @Column(length = 1000)
    private String conflictReason;

    private LocalDateTime createdAt;
    private LocalDateTime syncedAt;

    public OfflineSyncQueue() {}

    public OfflineSyncQueue(Long id, String tenantId, String userId, String idempotencyKey, String actionType, String payloadJson, String status, int attemptCount, String conflictReason, LocalDateTime createdAt, LocalDateTime syncedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.idempotencyKey = idempotencyKey;
        this.actionType = actionType;
        this.payloadJson = payloadJson;
        this.status = status;
        this.attemptCount = attemptCount;
        this.conflictReason = conflictReason;
        this.createdAt = createdAt;
        this.syncedAt = syncedAt;
    }

    public static OfflineSyncQueueBuilder builder() {
        return new OfflineSyncQueueBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getAttemptCount() { return attemptCount; }
    public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }
    public String getConflictReason() { return conflictReason; }
    public void setConflictReason(String conflictReason) { this.conflictReason = conflictReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getSyncedAt() { return syncedAt; }
    public void setSyncedAt(LocalDateTime syncedAt) { this.syncedAt = syncedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class OfflineSyncQueueBuilder {
        private Long id;
        private String tenantId;
        private String userId;
        private String idempotencyKey;
        private String actionType;
        private String payloadJson;
        private String status = "PENDING";
        private int attemptCount = 0;
        private String conflictReason;
        private LocalDateTime createdAt;
        private LocalDateTime syncedAt;

        public OfflineSyncQueueBuilder id(Long id) { this.id = id; return this; }
        public OfflineSyncQueueBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public OfflineSyncQueueBuilder userId(String userId) { this.userId = userId; return this; }
        public OfflineSyncQueueBuilder idempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; return this; }
        public OfflineSyncQueueBuilder actionType(String actionType) { this.actionType = actionType; return this; }
        public OfflineSyncQueueBuilder payloadJson(String payloadJson) { this.payloadJson = payloadJson; return this; }
        public OfflineSyncQueueBuilder status(String status) { this.status = status; return this; }
        public OfflineSyncQueueBuilder attemptCount(int attemptCount) { this.attemptCount = attemptCount; return this; }
        public OfflineSyncQueueBuilder conflictReason(String conflictReason) { this.conflictReason = conflictReason; return this; }
        public OfflineSyncQueueBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public OfflineSyncQueueBuilder syncedAt(LocalDateTime syncedAt) { this.syncedAt = syncedAt; return this; }

        public OfflineSyncQueue build() {
            return new OfflineSyncQueue(id, tenantId, userId, idempotencyKey, actionType, payloadJson, status, attemptCount, conflictReason, createdAt, syncedAt);
        }
    }
}
