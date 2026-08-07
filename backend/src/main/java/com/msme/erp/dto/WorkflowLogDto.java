package com.msme.erp.dto;

import java.time.LocalDateTime;

public class WorkflowLogDto {
    private String id;
    private String sourceStageId;
    private String sourceStageName;
    private String targetStageId;
    private String targetStageName;
    private String operatorId;
    private String operatorName;
    private String departmentId;
    private String departmentName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationSeconds;
    private Long delaySeconds;
    private String status;
    private String remarks;

    public WorkflowLogDto() {}

    public WorkflowLogDto(String id, String sourceStageId, String sourceStageName, String targetStageId, String targetStageName, String operatorId, String operatorName, String departmentId, String departmentName, LocalDateTime startTime, LocalDateTime endTime, Long durationSeconds, Long delaySeconds, String status, String remarks) {
        this.id = id;
        this.sourceStageId = sourceStageId;
        this.sourceStageName = sourceStageName;
        this.targetStageId = targetStageId;
        this.targetStageName = targetStageName;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationSeconds = durationSeconds;
        this.delaySeconds = delaySeconds;
        this.status = status;
        this.remarks = remarks;
    }

    public static WorkflowLogDtoBuilder builder() { return new WorkflowLogDtoBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSourceStageId() { return sourceStageId; }
    public void setSourceStageId(String sourceStageId) { this.sourceStageId = sourceStageId; }
    public String getSourceStageName() { return sourceStageName; }
    public void setSourceStageName(String sourceStageName) { this.sourceStageName = sourceStageName; }
    public String getTargetStageId() { return targetStageId; }
    public void setTargetStageId(String targetStageId) { this.targetStageId = targetStageId; }
    public String getTargetStageName() { return targetStageName; }
    public void setTargetStageName(String targetStageName) { this.targetStageName = targetStageName; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Long durationSeconds) { this.durationSeconds = durationSeconds; }
    public Long getDelaySeconds() { return delaySeconds; }
    public void setDelaySeconds(Long delaySeconds) { this.delaySeconds = delaySeconds; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public static class WorkflowLogDtoBuilder {
        private String id;
        private String sourceStageId;
        private String sourceStageName;
        private String targetStageId;
        private String targetStageName;
        private String operatorId;
        private String operatorName;
        private String departmentId;
        private String departmentName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Long durationSeconds;
        private Long delaySeconds;
        private String status;
        private String remarks;

        public WorkflowLogDtoBuilder id(String id) { this.id = id; return this; }
        public WorkflowLogDtoBuilder sourceStageId(String sourceStageId) { this.sourceStageId = sourceStageId; return this; }
        public WorkflowLogDtoBuilder sourceStageName(String sourceStageName) { this.sourceStageName = sourceStageName; return this; }
        public WorkflowLogDtoBuilder targetStageId(String targetStageId) { this.targetStageId = targetStageId; return this; }
        public WorkflowLogDtoBuilder targetStageName(String targetStageName) { this.targetStageName = targetStageName; return this; }
        public WorkflowLogDtoBuilder operatorId(String operatorId) { this.operatorId = operatorId; return this; }
        public WorkflowLogDtoBuilder operatorName(String operatorName) { this.operatorName = operatorName; return this; }
        public WorkflowLogDtoBuilder departmentId(String departmentId) { this.departmentId = departmentId; return this; }
        public WorkflowLogDtoBuilder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public WorkflowLogDtoBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public WorkflowLogDtoBuilder endTime(LocalDateTime endTime) { this.endTime = endTime; return this; }
        public WorkflowLogDtoBuilder durationSeconds(Long durationSeconds) { this.durationSeconds = durationSeconds; return this; }
        public WorkflowLogDtoBuilder delaySeconds(Long delaySeconds) { this.delaySeconds = delaySeconds; return this; }
        public WorkflowLogDtoBuilder status(String status) { this.status = status; return this; }
        public WorkflowLogDtoBuilder remarks(String remarks) { this.remarks = remarks; return this; }

        public WorkflowLogDto build() {
            return new WorkflowLogDto(id, sourceStageId, sourceStageName, targetStageId, targetStageName, operatorId, operatorName, departmentId, departmentName, startTime, endTime, durationSeconds, delaySeconds, status, remarks);
        }
    }
}
