package com.msme.erp.dto;

public class TransitionStageRequest {
    private String targetStageId;
    private String notes;

    public TransitionStageRequest() {}
    public TransitionStageRequest(String targetStageId, String notes) {
        this.targetStageId = targetStageId;
        this.notes = notes;
    }

    public String getTargetStageId() { return targetStageId; }
    public void setTargetStageId(String targetStageId) { this.targetStageId = targetStageId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
