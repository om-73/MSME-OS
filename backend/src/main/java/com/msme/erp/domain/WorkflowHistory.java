package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_history", indexes = {
    @Index(name = "idx_workflow_hist_workflow", columnList = "workflowId")
})
public class WorkflowHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String workflowId;

    @Column(nullable = false)
    private String changeType; // CREATE, EDIT, PUBLISH, ARCHIVE, CLONE

    private String changedBy;

    private String changeSummary;

    private LocalDateTime timestamp;

    public WorkflowHistory() {}

    public WorkflowHistory(String id, String workflowId, String changeType, String changedBy, String changeSummary, LocalDateTime timestamp) {
        this.id = id;
        this.workflowId = workflowId;
        this.changeType = changeType;
        this.changedBy = changedBy;
        this.changeSummary = changeSummary;
        this.timestamp = timestamp;
    }

    public static WorkflowHistoryBuilder builder() { return new WorkflowHistoryBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    public String getChangeSummary() { return changeSummary; }
    public void setChangeSummary(String changeSummary) { this.changeSummary = changeSummary; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public static class WorkflowHistoryBuilder {
        private String id;
        private String workflowId;
        private String changeType;
        private String changedBy;
        private String changeSummary;

        public WorkflowHistoryBuilder id(String id) { this.id = id; return this; }
        public WorkflowHistoryBuilder workflowId(String workflowId) { this.workflowId = workflowId; return this; }
        public WorkflowHistoryBuilder changeType(String changeType) { this.changeType = changeType; return this; }
        public WorkflowHistoryBuilder changedBy(String changedBy) { this.changedBy = changedBy; return this; }
        public WorkflowHistoryBuilder changeSummary(String changeSummary) { this.changeSummary = changeSummary; return this; }

        public WorkflowHistory build() {
            return new WorkflowHistory(id, workflowId, changeType, changedBy, changeSummary, null);
        }
    }
}
