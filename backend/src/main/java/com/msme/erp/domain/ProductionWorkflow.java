package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_workflows", indexes = {
    @Index(name = "idx_prod_workflows_order", columnList = "orderId"),
    @Index(name = "idx_prod_workflows_version", columnList = "workflowVersionId")
})
public class ProductionWorkflow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String workflowVersionId;

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, COMPLETED, SUSPENDED

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public ProductionWorkflow() {}

    public ProductionWorkflow(String id, String orderId, String workflowVersionId, String status, LocalDateTime startedAt, LocalDateTime completedAt) {
        this.id = id;
        this.orderId = orderId;
        this.workflowVersionId = workflowVersionId;
        this.status = status;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    public static ProductionWorkflowBuilder builder() { return new ProductionWorkflowBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getWorkflowVersionId() { return workflowVersionId; }
    public void setWorkflowVersionId(String workflowVersionId) { this.workflowVersionId = workflowVersionId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    @PrePersist
    protected void onCreate() {
        startedAt = LocalDateTime.now();
    }

    public static class ProductionWorkflowBuilder {
        private String id;
        private String orderId;
        private String workflowVersionId;
        private String status = "ACTIVE";
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;

        public ProductionWorkflowBuilder id(String id) { this.id = id; return this; }
        public ProductionWorkflowBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public ProductionWorkflowBuilder workflowVersionId(String workflowVersionId) { this.workflowVersionId = workflowVersionId; return this; }
        public ProductionWorkflowBuilder status(String status) { this.status = status; return this; }
        public ProductionWorkflowBuilder startedAt(LocalDateTime startedAt) { this.startedAt = startedAt; return this; }
        public ProductionWorkflowBuilder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public ProductionWorkflow build() {
            return new ProductionWorkflow(id, orderId, workflowVersionId, status, startedAt, completedAt);
        }
    }
}
