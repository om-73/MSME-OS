package com.msme.erp.dto;

import java.time.LocalDateTime;

public class QCRecordDto {
    private String id;
    private String stageName;
    private String inspectorName;
    private boolean passed;
    private String defectType;
    private Integer sampleSize;
    private Integer defectCount;
    private String remarks;
    private LocalDateTime createdAt;

    public QCRecordDto() {}

    public QCRecordDto(String id, String stageName, String inspectorName, boolean passed, String defectType, Integer sampleSize, Integer defectCount, String remarks, LocalDateTime createdAt) {
        this.id = id;
        this.stageName = stageName;
        this.inspectorName = inspectorName;
        this.passed = passed;
        this.defectType = defectType;
        this.sampleSize = sampleSize;
        this.defectCount = defectCount;
        this.remarks = remarks;
        this.createdAt = createdAt;
    }

    public static QCRecordDtoBuilder builder() { return new QCRecordDtoBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }
    public String getInspectorName() { return inspectorName; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class QCRecordDtoBuilder {
        private String id;
        private String stageName;
        private String inspectorName;
        private boolean passed;
        private String defectType;
        private Integer sampleSize;
        private Integer defectCount;
        private String remarks;
        private LocalDateTime createdAt;

        public QCRecordDtoBuilder id(String id) { this.id = id; return this; }
        public QCRecordDtoBuilder stageName(String stageName) { this.stageName = stageName; return this; }
        public QCRecordDtoBuilder inspectorName(String inspectorName) { this.inspectorName = inspectorName; return this; }
        public QCRecordDtoBuilder passed(boolean passed) { this.passed = passed; return this; }
        public QCRecordDtoBuilder defectType(String defectType) { this.defectType = defectType; return this; }
        public QCRecordDtoBuilder sampleSize(Integer sampleSize) { this.sampleSize = sampleSize; return this; }
        public QCRecordDtoBuilder defectCount(Integer defectCount) { this.defectCount = defectCount; return this; }
        public QCRecordDtoBuilder remarks(String remarks) { this.remarks = remarks; return this; }
        public QCRecordDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public QCRecordDto build() {
            return new QCRecordDto(id, stageName, inspectorName, passed, defectType, sampleSize, defectCount, remarks, createdAt);
        }
    }
}
