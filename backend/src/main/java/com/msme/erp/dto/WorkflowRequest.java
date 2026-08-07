package com.msme.erp.dto;

import java.util.List;

public class WorkflowRequest {
    private String name;
    private String description;
    private String industry;
    private String definitionJson;
    private List<StageRequest> stages;
    private List<EdgeRequest> edges;

    public WorkflowRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getDefinitionJson() { return definitionJson; }
    public void setDefinitionJson(String definitionJson) { this.definitionJson = definitionJson; }
    public List<StageRequest> getStages() { return stages; }
    public void setStages(List<StageRequest> stages) { this.stages = stages; }
    public List<EdgeRequest> getEdges() { return edges; }
    public void setEdges(List<EdgeRequest> edges) { this.edges = edges; }

    public static class StageRequest {
        private String id; // Optional client-generated UUID/React Flow Node ID
        private String name;
        private String code;
        private String description;
        private Integer sequenceOrder;
        private String type; // START, NORMAL, QC, APPROVAL, DECISION, END
        private String colorHex;
        private Integer estimatedSlaHours;
        private String checklistItems;
        private String departmentId;

        public StageRequest() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
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
    }

    public static class EdgeRequest {
        private String sourceStageId;
        private String targetStageId;
        private String conditionExpression;

        public EdgeRequest() {}

        public String getSourceStageId() { return sourceStageId; }
        public void setSourceStageId(String sourceStageId) { this.sourceStageId = sourceStageId; }
        public String getTargetStageId() { return targetStageId; }
        public void setTargetStageId(String targetStageId) { this.targetStageId = targetStageId; }
        public String getConditionExpression() { return conditionExpression; }
        public void setConditionExpression(String conditionExpression) { this.conditionExpression = conditionExpression; }
    }
}
