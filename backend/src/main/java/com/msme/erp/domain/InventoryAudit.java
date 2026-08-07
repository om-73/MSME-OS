package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_audits")
public class InventoryAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String auditName;

    @Column(nullable = false)
    private String status; // DRAFT, COMPLETED

    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public InventoryAudit() {}

    public InventoryAudit(String id, String tenantId, String auditName, String status, String createdBy, 
                          LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.auditName = auditName;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public static InventoryAuditBuilder builder() {
        return new InventoryAuditBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getAuditName() { return auditName; }
    public void setAuditName(String auditName) { this.auditName = auditName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public static class InventoryAuditBuilder {
        private String id;
        private String tenantId;
        private String auditName;
        private String status;
        private String createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public InventoryAuditBuilder id(String id) { this.id = id; return this; }
        public InventoryAuditBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public InventoryAuditBuilder auditName(String auditName) { this.auditName = auditName; return this; }
        public InventoryAuditBuilder status(String status) { this.status = status; return this; }
        public InventoryAuditBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public InventoryAuditBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public InventoryAuditBuilder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public InventoryAudit build() {
            return new InventoryAudit(id, tenantId, auditName, status, createdBy, createdAt, completedAt);
        }
    }
}
