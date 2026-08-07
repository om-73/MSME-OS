package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "workflow_stages", indexes = {
    @Index(name = "idx_workflow_stages_version", columnList = "workflowVersionId")
})
public class WorkflowStage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String workflowVersionId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    private String description;

    @Column(nullable = false)
    private Integer sequenceOrder;

    @Column(nullable = false)
    private String type = "NORMAL"; // START, NORMAL, QC, APPROVAL, DECISION, END

    private String colorHex = "#3B82F6";

    private Integer estimatedSlaHours;

    @Column(columnDefinition = "TEXT")
    private String checklistItems;

    private String departmentId;

    public WorkflowStage() {}

    public WorkflowStage(String id, String workflowVersionId, String name, String code, String description, Integer sequenceOrder, String type, String colorHex, Integer estimatedSlaHours, String checklistItems, String departmentId) {
        this.id = id;
        this.workflowVersionId = workflowVersionId;
        this.name = name;
        this.code = code;
        this.description = description;
        this.sequenceOrder = sequenceOrder;
        this.type = type;
        this.colorHex = colorHex;
        this.estimatedSlaHours = estimatedSlaHours;
        this.checklistItems = checklistItems;
        this.departmentId = departmentId;
    }

    public static WorkflowStageBuilder builder() { return new WorkflowStageBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkflowVersionId() { return workflowVersionId; }
    public void setWorkflowVersionId(String workflowVersionId) { this.workflowVersionId = workflowVersionId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getSequenceOrder() { return sequenceOrder; }
    public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }
    public Integer getEstimatedSlaHours() { return estimatedSlaHours; }
    public void setEstimatedSlaHours(Integer estimatedSlaHours) { this.estimatedSlaHours = estimatedSlaHours; }
    public String getChecklistItems() { return checklistItems; }
    public void setChecklistItems(String checklistItems) { this.checklistItems = checklistItems; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public static class WorkflowStageBuilder {
        private String id;
        private String workflowVersionId;
        private String name;
        private String code;
        private String description;
        private Integer sequenceOrder;
        private String type = "NORMAL";
        private String colorHex = "#3B82F6";
        private Integer estimatedSlaHours;
        private String checklistItems;
        private String departmentId;

        public WorkflowStageBuilder id(String id) { this.id = id; return this; }
        public WorkflowStageBuilder workflowVersionId(String workflowVersionId) { this.workflowVersionId = workflowVersionId; return this; }
        public WorkflowStageBuilder name(String name) { this.name = name; return this; }
        public WorkflowStageBuilder code(String code) { this.code = code; return this; }
        public WorkflowStageBuilder description(String description) { this.description = description; return this; }
        public WorkflowStageBuilder sequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; return this; }
        public WorkflowStageBuilder type(String type) { this.type = type; return this; }
        public WorkflowStageBuilder colorHex(String colorHex) { this.colorHex = colorHex; return this; }
        public WorkflowStageBuilder estimatedSlaHours(Integer estimatedSlaHours) { this.estimatedSlaHours = estimatedSlaHours; return this; }
        public WorkflowStageBuilder checklistItems(String checklistItems) { this.checklistItems = checklistItems; return this; }
        public WorkflowStageBuilder departmentId(String departmentId) { this.departmentId = departmentId; return this; }

        public WorkflowStage build() {
            return new WorkflowStage(id, workflowVersionId, name, code, description, sequenceOrder, type, colorHex, estimatedSlaHours, checklistItems, departmentId);
        }
    }
}
