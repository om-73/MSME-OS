package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_deletion_jobs")
public class DataDeletionJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    private String approvedBy;
    private String status; // PENDING, HOLD_CHECK, EXPORTING, WIPING, COMPLETED, FAILED
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public DataDeletionJob() {}
    public DataDeletionJob(String tenantId, String approvedBy, String status, LocalDateTime startedAt, LocalDateTime completedAt) {
        this.tenantId = tenantId;
        this.approvedBy = approvedBy;
        this.status = status;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
