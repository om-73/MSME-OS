package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_versions", indexes = {
    @Index(name = "idx_workflow_versions_workflow", columnList = "workflowId")
})
public class WorkflowVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String workflowId;

    @Column(nullable = false)
    private Integer versionNumber;

    @Column(nullable = false)
    private String status = "DRAFT"; // DRAFT, PUBLISHED, ARCHIVED

    @Column(columnDefinition = "TEXT")
    private String definitionJson; // React Flow raw JSON representation for UI restoration

    private LocalDateTime createdAt;

    public WorkflowVersion() {}

    public WorkflowVersion(String id, String workflowId, Integer versionNumber, String status, String definitionJson, LocalDateTime createdAt) {
        this.id = id;
        this.workflowId = workflowId;
        this.versionNumber = versionNumber;
        this.status = status;
        this.definitionJson = definitionJson;
        this.createdAt = createdAt;
    }

    public static WorkflowVersionBuilder builder() { return new WorkflowVersionBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDefinitionJson() { return definitionJson; }
    public void setDefinitionJson(String definitionJson) { this.definitionJson = definitionJson; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class WorkflowVersionBuilder {
        private String id;
        private String workflowId;
        private Integer versionNumber;
        private String status = "DRAFT";
        private String definitionJson;

        public WorkflowVersionBuilder id(String id) { this.id = id; return this; }
        public WorkflowVersionBuilder workflowId(String workflowId) { this.workflowId = workflowId; return this; }
        public WorkflowVersionBuilder versionNumber(Integer versionNumber) { this.versionNumber = versionNumber; return this; }
        public WorkflowVersionBuilder status(String status) { this.status = status; return this; }
        public WorkflowVersionBuilder definitionJson(String definitionJson) { this.definitionJson = definitionJson; return this; }

        public WorkflowVersion build() {
            return new WorkflowVersion(id, workflowId, versionNumber, status, definitionJson, null);
        }
    }
}
