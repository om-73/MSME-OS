package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workflows", indexes = {
    @Index(name = "idx_workflows_tenant", columnList = "tenantId"),
    @Index(name = "idx_workflows_industry", columnList = "industry")
})
public class Workflow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String industry; // e.g. Garments, Furniture, Packaging, Jewelry, Plastic, Textile, Printing

    private Integer currentVersion = 1;

    private String status = "DRAFT"; // DRAFT, PUBLISHED, ARCHIVED

    private boolean deleted = false;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Workflow() {}

    public Workflow(String id, String tenantId, String name, String description, String industry, Integer currentVersion, String status, boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.description = description;
        this.industry = industry;
        this.currentVersion = currentVersion;
        this.status = status;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static WorkflowBuilder builder() { return new WorkflowBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public Integer getCurrentVersion() { return currentVersion; }
    public void setCurrentVersion(Integer currentVersion) { this.currentVersion = currentVersion; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
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

    public static class WorkflowBuilder {
        private String id;
        private String tenantId;
        private String name;
        private String description;
        private String industry;
        private Integer currentVersion = 1;
        private String status = "DRAFT";
        private boolean deleted = false;

        public WorkflowBuilder id(String id) { this.id = id; return this; }
        public WorkflowBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public WorkflowBuilder name(String name) { this.name = name; return this; }
        public WorkflowBuilder description(String description) { this.description = description; return this; }
        public WorkflowBuilder industry(String industry) { this.industry = industry; return this; }
        public WorkflowBuilder currentVersion(Integer currentVersion) { this.currentVersion = currentVersion; return this; }
        public WorkflowBuilder status(String status) { this.status = status; return this; }
        public WorkflowBuilder deleted(boolean deleted) { this.deleted = deleted; return this; }

        public Workflow build() {
            return new Workflow(id, tenantId, name, description, industry, currentVersion, status, deleted, null, null);
        }
    }
}
