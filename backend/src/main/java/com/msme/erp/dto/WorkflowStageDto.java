package com.msme.erp.dto;

public class WorkflowStageDto {
    private String id;
    private String name;
    private String code;
    private String description;
    private String colorHex;
    private Integer sequenceOrder;
    private boolean approvalRequired;
    private Integer estimatedSlaHours;
    private String checklistItems;

    public WorkflowStageDto() {}

    public WorkflowStageDto(String id, String name, String code, String description, String colorHex, Integer sequenceOrder, boolean approvalRequired, Integer estimatedSlaHours, String checklistItems) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.colorHex = colorHex;
        this.sequenceOrder = sequenceOrder;
        this.approvalRequired = approvalRequired;
        this.estimatedSlaHours = estimatedSlaHours;
        this.checklistItems = checklistItems;
    }

    public static WorkflowStageDtoBuilder builder() { return new WorkflowStageDtoBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }
    public Integer getSequenceOrder() { return sequenceOrder; }
    public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }
    public boolean isApprovalRequired() { return approvalRequired; }
    public void setApprovalRequired(boolean approvalRequired) { this.approvalRequired = approvalRequired; }
    public Integer getEstimatedSlaHours() { return estimatedSlaHours; }
    public void setEstimatedSlaHours(Integer estimatedSlaHours) { this.estimatedSlaHours = estimatedSlaHours; }
    public String getChecklistItems() { return checklistItems; }
    public void setChecklistItems(String checklistItems) { this.checklistItems = checklistItems; }

    public static class WorkflowStageDtoBuilder {
        private String id;
        private String name;
        private String code;
        private String description;
        private String colorHex;
        private Integer sequenceOrder;
        private boolean approvalRequired;
        private Integer estimatedSlaHours;
        private String checklistItems;

        public WorkflowStageDtoBuilder id(String id) { this.id = id; return this; }
        public WorkflowStageDtoBuilder name(String name) { this.name = name; return this; }
        public WorkflowStageDtoBuilder code(String code) { this.code = code; return this; }
        public WorkflowStageDtoBuilder description(String description) { this.description = description; return this; }
        public WorkflowStageDtoBuilder colorHex(String colorHex) { this.colorHex = colorHex; return this; }
        public WorkflowStageDtoBuilder sequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; return this; }
        public WorkflowStageDtoBuilder approvalRequired(boolean approvalRequired) { this.approvalRequired = approvalRequired; return this; }
        public WorkflowStageDtoBuilder estimatedSlaHours(Integer estimatedSlaHours) { this.estimatedSlaHours = estimatedSlaHours; return this; }
        public WorkflowStageDtoBuilder checklistItems(String checklistItems) { this.checklistItems = checklistItems; return this; }

        public WorkflowStageDto build() {
            return new WorkflowStageDto(id, name, code, description, colorHex, sequenceOrder, approvalRequired, estimatedSlaHours, checklistItems);
        }
    }
}
