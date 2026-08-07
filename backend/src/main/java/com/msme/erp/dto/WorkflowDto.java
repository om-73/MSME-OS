package com.msme.erp.dto;

import java.util.List;

public class WorkflowDto {
    private String id;
    private String name;
    private String description;
    private boolean defaultPipeline;
    private List<WorkflowStageDto> stages;

    public WorkflowDto() {}

    public WorkflowDto(String id, String name, String description, boolean defaultPipeline, List<WorkflowStageDto> stages) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.defaultPipeline = defaultPipeline;
        this.stages = stages;
    }

    public static WorkflowDtoBuilder builder() { return new WorkflowDtoBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isDefaultPipeline() { return defaultPipeline; }
    public void setDefaultPipeline(boolean defaultPipeline) { this.defaultPipeline = defaultPipeline; }
    public List<WorkflowStageDto> getStages() { return stages; }
    public void setStages(List<WorkflowStageDto> stages) { this.stages = stages; }

    public static class WorkflowDtoBuilder {
        private String id;
        private String name;
        private String description;
        private boolean defaultPipeline;
        private List<WorkflowStageDto> stages;

        public WorkflowDtoBuilder id(String id) { this.id = id; return this; }
        public WorkflowDtoBuilder name(String name) { this.name = name; return this; }
        public WorkflowDtoBuilder description(String description) { this.description = description; return this; }
        public WorkflowDtoBuilder defaultPipeline(boolean defaultPipeline) { this.defaultPipeline = defaultPipeline; return this; }
        public WorkflowDtoBuilder stages(List<WorkflowStageDto> stages) { this.stages = stages; return this; }

        public WorkflowDto build() {
            return new WorkflowDto(id, name, description, defaultPipeline, stages);
        }
    }
}
