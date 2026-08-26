package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "retention_policies")
public class RetentionPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    
    @Column(nullable = false)
    private String targetRecordType; // AUDIT_LOG, INVOICE, PRODUCTION_RECORD, TEMP_FILE
    
    private int retentionPeriodDays;

    public RetentionPolicy() {}
    public RetentionPolicy(String tenantId, String targetRecordType, int retentionPeriodDays) {
        this.tenantId = tenantId;
        this.targetRecordType = targetRecordType;
        this.retentionPeriodDays = retentionPeriodDays;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getTargetRecordType() { return targetRecordType; }
    public void setTargetRecordType(String targetRecordType) { this.targetRecordType = targetRecordType; }
    public int getRetentionPeriodDays() { return retentionPeriodDays; }
    public void setRetentionPeriodDays(int retentionPeriodDays) { this.retentionPeriodDays = retentionPeriodDays; }
}
