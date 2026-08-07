package com.msme.erp.dto;

public class QCSubmitRequest {
    private String orderId;
    private String stageId;
    private boolean passed;
    private String defectType;
    private Integer sampleSize;
    private Integer defectCount;
    private String remarks;

    public QCSubmitRequest() {}

    public QCSubmitRequest(String orderId, String stageId, boolean passed, String defectType, Integer sampleSize, Integer defectCount, String remarks) {
        this.orderId = orderId;
        this.stageId = stageId;
        this.passed = passed;
        this.defectType = defectType;
        this.sampleSize = sampleSize;
        this.defectCount = defectCount;
        this.remarks = remarks;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getStageId() { return stageId; }
    public void setStageId(String stageId) { this.stageId = stageId; }
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
    public String getDefectType() { return defectType; }
    public void setDefectType(String defectType) { this.defectType = defectType; }
    public Integer getSampleSize() { return sampleSize; }
    public void setSampleSize(Integer sampleSize) { this.sampleSize = sampleSize; }
    public Integer getDefectCount() { return defectCount; }
    public void setDefectCount(Integer defectCount) { this.defectCount = defectCount; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
