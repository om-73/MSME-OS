package com.msme.erp.dto;

import java.time.LocalDateTime;

public class OrderStageLogDto {
    private String id;
    private String stageId;
    private String stageName;
    private String operatorName;
    private String action;
    private String notes;
    private LocalDateTime timestamp;

    public OrderStageLogDto() {}

    public OrderStageLogDto(String id, String stageId, String stageName, String operatorName, String action, String notes, LocalDateTime timestamp) {
        this.id = id;
        this.stageId = stageId;
        this.stageName = stageName;
        this.operatorName = operatorName;
        this.action = action;
        this.notes = notes;
        this.timestamp = timestamp;
    }

    public static OrderStageLogDtoBuilder builder() { return new OrderStageLogDtoBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStageId() { return stageId; }
    public void setStageId(String stageId) { this.stageId = stageId; }
    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class OrderStageLogDtoBuilder {
        private String id;
        private String stageId;
        private String stageName;
        private String operatorName;
        private String action;
        private String notes;
        private LocalDateTime timestamp;

        public OrderStageLogDtoBuilder id(String id) { this.id = id; return this; }
        public OrderStageLogDtoBuilder stageId(String stageId) { this.stageId = stageId; return this; }
        public OrderStageLogDtoBuilder stageName(String stageName) { this.stageName = stageName; return this; }
        public OrderStageLogDtoBuilder operatorName(String operatorName) { this.operatorName = operatorName; return this; }
        public OrderStageLogDtoBuilder action(String action) { this.action = action; return this; }
        public OrderStageLogDtoBuilder notes(String notes) { this.notes = notes; return this; }
        public OrderStageLogDtoBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public OrderStageLogDto build() {
            return new OrderStageLogDto(id, stageId, stageName, operatorName, action, notes, timestamp);
        }
    }
}
