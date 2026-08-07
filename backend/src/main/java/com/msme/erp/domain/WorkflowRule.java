package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "workflow_rules", indexes = {
    @Index(name = "idx_workflow_rules_stage", columnList = "stageId")
})
public class WorkflowRule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String stageId;

    @Column(nullable = false)
    private String ruleType; // e.g. VALIDATION, TRIGGER

    @Column(nullable = false, columnDefinition = "TEXT")
    private String expression; // e.g. "qc_defect_rate < 0.05" or "fabric_qty > 0"

    private String errorMessage;

    public WorkflowRule() {}

    public WorkflowRule(String id, String stageId, String ruleType, String expression, String errorMessage) {
        this.id = id;
        this.stageId = stageId;
        this.ruleType = ruleType;
        this.expression = expression;
        this.errorMessage = errorMessage;
    }

    public static WorkflowRuleBuilder builder() { return new WorkflowRuleBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStageId() { return stageId; }
    public void setStageId(String stageId) { this.stageId = stageId; }
    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }
    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public static class WorkflowRuleBuilder {
        private String id;
        private String stageId;
        private String ruleType;
        private String expression;
        private String errorMessage;

        public WorkflowRuleBuilder id(String id) { this.id = id; return this; }
        public WorkflowRuleBuilder stageId(String stageId) { this.stageId = stageId; return this; }
        public WorkflowRuleBuilder ruleType(String ruleType) { this.ruleType = ruleType; return this; }
        public WorkflowRuleBuilder expression(String expression) { this.expression = expression; return this; }
        public WorkflowRuleBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }

        public WorkflowRule build() {
            return new WorkflowRule(id, stageId, ruleType, expression, errorMessage);
        }
    }
}
