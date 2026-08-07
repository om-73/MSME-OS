package com.msme.erp.dto;

import java.time.LocalDateTime;
import java.util.List;

public class WorkflowResponse {
    private String id;
    private String tenantId;
    private String name;
    private String description;
    private String industry;
    private Integer currentVersion;
    private String status;
    private String definitionJson;
    private List<StageResponse> stages;
    private List<EdgeResponse> edges;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WorkflowResponse() {}

    public WorkflowResponse(String id, String tenantId, String name, String description, String industry, Integer currentVersion, String status, String definitionJson, List<StageResponse> stages, List<EdgeResponse> edges, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.description = description;
        this.industry = industry;
        this.currentVersion = currentVersion;
        this.status = status;
        this.definitionJson = definitionJson;
        this.stages = stages;
        this.edges = edges;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static WorkflowResponseBuilder builder() { return new WorkflowResponseBuilder(); }

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
    public String getDefinitionJson() { return definitionJson; }
    public void setDefinitionJson(String definitionJson) { this.definitionJson = definitionJson; }
    public List<StageResponse> getStages() { return stages; }
    public void setStages(List<StageResponse> stages) { this.stages = stages; }
    public List<EdgeResponse> getEdges() { return edges; }
    public void setEdges(List<EdgeResponse> edges) { this.edges = edges; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class StageResponse {
        private String id;
        private String name;
        private String code;
        private String description;
        private Integer sequenceOrder;
        private String type;
        private String colorHex;
        private Integer estimatedSlaHours;
        private String checklistItems;
        private String departmentId;
        private String departmentName;

        public StageResponse() {}

        public StageResponse(String id, String name, String code, String description, Integer sequenceOrder, String type, String colorHex, Integer estimatedSlaHours, String checklistItems, String departmentId, String departmentName) {
            this.id = id;
            this.name = name;
            this.code = code;
            this.description = description;
            this.sequenceOrder = sequenceOrder;
            this.type = type;
            this.colorHex = colorHex;
            this.estimatedSlaHours = estimatedSlaHours;
            this.checklistItems = checklistItems;
            this.departmentId = departmentId;
            this.departmentName = departmentName;
        }

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
        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    }

    public static class EdgeResponse {
        private String id;
        private String sourceStageId;
        private String targetStageId;
        private String conditionExpression;

        public EdgeResponse() {}

        public EdgeResponse(String id, String sourceStageId, String targetStageId, String conditionExpression) {
            this.id = id;
            this.sourceStageId = sourceStageId;
            this.targetStageId = targetStageId;
            this.conditionExpression = conditionExpression;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getSourceStageId() { return sourceStageId; }
        public void setSourceStageId(String sourceStageId) { this.sourceStageId = sourceStageId; }
        public String getTargetStageId() { return targetStageId; }
        public void setTargetStageId(String targetStageId) { this.targetStageId = targetStageId; }
        public String getConditionExpression() { return conditionExpression; }
        public void setConditionExpression(String conditionExpression) { this.conditionExpression = conditionExpression; }
    }

    public static class WorkflowResponseBuilder {
        private String id;
        private String tenantId;
        private String name;
        private String description;
        private String industry;
        private Integer currentVersion;
        private String status;
        private String definitionJson;
        private List<StageResponse> stages;
        private List<EdgeResponse> edges;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public WorkflowResponseBuilder id(String id) { this.id = id; return this; }
        public WorkflowResponseBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public WorkflowResponseBuilder name(String name) { this.name = name; return this; }
        public WorkflowResponseBuilder description(String description) { this.description = description; return this; }
        public WorkflowResponseBuilder industry(String industry) { this.industry = industry; return this; }
        public WorkflowResponseBuilder currentVersion(Integer currentVersion) { this.currentVersion = currentVersion; return this; }
        public WorkflowResponseBuilder status(String status) { this.status = status; return this; }
        public WorkflowResponseBuilder definitionJson(String definitionJson) { this.definitionJson = definitionJson; return this; }
        public WorkflowResponseBuilder stages(List<StageResponse> stages) { this.stages = stages; return this; }
        public WorkflowResponseBuilder edges(List<EdgeResponse> edges) { this.edges = edges; return this; }
        public WorkflowResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public WorkflowResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public WorkflowResponse build() {
            return new WorkflowResponse(id, tenantId, name, description, industry, currentVersion, status, definitionJson, stages, edges, createdAt, updatedAt);
        }
    }
}
