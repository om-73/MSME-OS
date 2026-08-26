package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_export_jobs")
public class DataExportJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    private String requestedBy;
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED
    private Long exportSize;
    private String signedDownloadUrl;
    private LocalDateTime createdAt;

    public DataExportJob() {}
    public DataExportJob(String tenantId, String requestedBy, String status, Long exportSize, String signedDownloadUrl, LocalDateTime createdAt) {
        this.tenantId = tenantId;
        this.requestedBy = requestedBy;
        this.status = status;
        this.exportSize = exportSize;
        this.signedDownloadUrl = signedDownloadUrl;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getExportSize() { return exportSize; }
    public void setExportSize(Long exportSize) { this.exportSize = exportSize; }
    public String getSignedDownloadUrl() { return signedDownloadUrl; }
    public void setSignedDownloadUrl(String signedDownloadUrl) { this.signedDownloadUrl = signedDownloadUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
