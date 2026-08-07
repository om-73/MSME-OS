package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "qc_records")
public class QCRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderId;

    private String stageId;
    private String stageName;
    private String inspectorId;
    private String inspectorName;

    @Column(nullable = false)
    private boolean passed;

    private String defectType;
    private Integer sampleSize;
    private Integer defectCount;
    private String remarks;
    private LocalDateTime createdAt;

    public QCRecord() {}

    public QCRecord(String id, String tenantId, String orderId, String stageId, String stageName, String inspectorId, String inspectorName, boolean passed, String defectType, Integer sampleSize, Integer defectCount, String remarks, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.stageId = stageId;
        this.stageName = stageName;
        this.inspectorId = inspectorId;
        this.inspectorName = inspectorName;
        this.passed = passed;
        this.defectType = defectType;
        this.sampleSize = sampleSize;
        this.defectCount = defectCount;
        this.remarks = remarks;
        this.createdAt = createdAt;
    }

    public static QCRecordBuilder builder() { return new QCRecordBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getStageId() { return stageId; }
    public void setStageId(String stageId) { this.stageId = stageId; }
    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }
    public String getInspectorId() { return inspectorId; }
    public void setInspectorId(String inspectorId) { this.inspectorId = inspectorId; }
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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class QCRecordBuilder {
        private String id;
        private String tenantId;
        private String orderId;
        private String stageId;
        private String stageName;
        private String inspectorId;
        private String inspectorName;
        private boolean passed;
        private String defectType;
        private Integer sampleSize;
        private Integer defectCount;
        private String remarks;

        public QCRecordBuilder id(String id) { this.id = id; return this; }
        public QCRecordBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public QCRecordBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public QCRecordBuilder stageId(String stageId) { this.stageId = stageId; return this; }
        public QCRecordBuilder stageName(String stageName) { this.stageName = stageName; return this; }
        public QCRecordBuilder inspectorId(String inspectorId) { this.inspectorId = inspectorId; return this; }
        public QCRecordBuilder inspectorName(String inspectorName) { this.inspectorName = inspectorName; return this; }
        public QCRecordBuilder passed(boolean passed) { this.passed = passed; return this; }
        public QCRecordBuilder defectType(String defectType) { this.defectType = defectType; return this; }
        public QCRecordBuilder sampleSize(Integer sampleSize) { this.sampleSize = sampleSize; return this; }
        public QCRecordBuilder defectCount(Integer defectCount) { this.defectCount = defectCount; return this; }
        public QCRecordBuilder remarks(String remarks) { this.remarks = remarks; return this; }

        public QCRecord build() {
            return new QCRecord(id, tenantId, orderId, stageId, stageName, inspectorId, inspectorName, passed, defectType, sampleSize, defectCount, remarks, null);
        }
    }
}
