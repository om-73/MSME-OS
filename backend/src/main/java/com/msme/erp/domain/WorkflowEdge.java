package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "workflow_edges", indexes = {
    @Index(name = "idx_workflow_edges_version", columnList = "workflowVersionId")
})
public class WorkflowEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String workflowVersionId;

    @Column(nullable = false)
    private String sourceStageId;

    @Column(nullable = false)
    private String targetStageId;

    private String conditionExpression; // e.g. "qc_passed == false" for decision nodes

    public WorkflowEdge() {}

    public WorkflowEdge(String id, String workflowVersionId, String sourceStageId, String targetStageId, String conditionExpression) {
        this.id = id;
        this.workflowVersionId = workflowVersionId;
        this.sourceStageId = sourceStageId;
        this.targetStageId = targetStageId;
        this.conditionExpression = conditionExpression;
    }

    public static WorkflowEdgeBuilder builder() { return new WorkflowEdgeBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkflowVersionId() { return workflowVersionId; }
    public void setWorkflowVersionId(String workflowVersionId) { this.workflowVersionId = workflowVersionId; }
    public String getSourceStageId() { return sourceStageId; }
    public void setSourceStageId(String sourceStageId) { this.sourceStageId = sourceStageId; }
    public String getTargetStageId() { return targetStageId; }
    public void setTargetStageId(String targetStageId) { this.targetStageId = targetStageId; }
    public String getConditionExpression() { return conditionExpression; }
    public void setConditionExpression(String conditionExpression) { this.conditionExpression = conditionExpression; }

    public static class WorkflowEdgeBuilder {
        private String id;
        private String workflowVersionId;
        private String sourceStageId;
        private String targetStageId;
        private String conditionExpression;

        public WorkflowEdgeBuilder id(String id) { this.id = id; return this; }
        public WorkflowEdgeBuilder workflowVersionId(String workflowVersionId) { this.workflowVersionId = workflowVersionId; return this; }
        public WorkflowEdgeBuilder sourceStageId(String sourceStageId) { this.sourceStageId = sourceStageId; return this; }
        public WorkflowEdgeBuilder targetStageId(String targetStageId) { this.targetStageId = targetStageId; return this; }
        public WorkflowEdgeBuilder conditionExpression(String conditionExpression) { this.conditionExpression = conditionExpression; return this; }

        public WorkflowEdge build() {
            return new WorkflowEdge(id, workflowVersionId, sourceStageId, targetStageId, conditionExpression);
        }
    }
}
