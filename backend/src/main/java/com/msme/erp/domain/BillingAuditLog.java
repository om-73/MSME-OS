package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "billing_audit_logs")
public class BillingAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String actorId;

    @Column(nullable = false)
    private String action; // PLAN_UPGRADED, PLAN_DOWNGRADED, PLAN_CANCELLED, TENANT_SUSPENDED, TENANT_REACTIVATED, FEATURE_OVERRIDE

    private String previousState;
    private String newState;

    @Column(length = 1000)
    private String remarks;

    private LocalDateTime timestamp;

    public BillingAuditLog() {}

    public BillingAuditLog(Long id, String tenantId, String actorId, String action, String previousState, String newState, String remarks, LocalDateTime timestamp) {
        this.id = id;
        this.tenantId = tenantId;
        this.actorId = actorId;
        this.action = action;
        this.previousState = previousState;
        this.newState = newState;
        this.remarks = remarks;
        this.timestamp = timestamp;
    }

    public static BillingAuditLogBuilder builder() {
        return new BillingAuditLogBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getActorId() { return actorId; }
    public void setActorId(String actorId) { this.actorId = actorId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getPreviousState() { return previousState; }
    public void setPreviousState(String previousState) { this.previousState = previousState; }
    public String getNewState() { return newState; }
    public void setNewState(String newState) { this.newState = newState; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public static class BillingAuditLogBuilder {
        private Long id;
        private String tenantId;
        private String actorId;
        private String action;
        private String previousState;
        private String newState;
        private String remarks;
        private LocalDateTime timestamp;

        public BillingAuditLogBuilder id(Long id) { this.id = id; return this; }
        public BillingAuditLogBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public BillingAuditLogBuilder actorId(String actorId) { this.actorId = actorId; return this; }
        public BillingAuditLogBuilder action(String action) { this.action = action; return this; }
        public BillingAuditLogBuilder previousState(String previousState) { this.previousState = previousState; return this; }
        public BillingAuditLogBuilder newState(String newState) { this.newState = newState; return this; }
        public BillingAuditLogBuilder remarks(String remarks) { this.remarks = remarks; return this; }
        public BillingAuditLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public BillingAuditLog build() {
            return new BillingAuditLog(id, tenantId, actorId, action, previousState, newState, remarks, timestamp);
        }
    }
}
