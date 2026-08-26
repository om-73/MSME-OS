package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restore_operations")
public class RestoreOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long backupRecordId;
    private String targetEnvironment;
    private String approvedBy;
    private String status; // PENDING, INTEGRITY_CHECK, EXECUTING, SUCCESS, FAILED
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;

    public RestoreOperation() {}
    public RestoreOperation(Long backupRecordId, String targetEnvironment, String approvedBy, String status, LocalDateTime initiatedAt, LocalDateTime completedAt) {
        this.backupRecordId = backupRecordId;
        this.targetEnvironment = targetEnvironment;
        this.approvedBy = approvedBy;
        this.status = status;
        this.initiatedAt = initiatedAt;
        this.completedAt = completedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBackupRecordId() { return backupRecordId; }
    public void setBackupRecordId(Long backupRecordId) { this.backupRecordId = backupRecordId; }
    public String getTargetEnvironment() { return targetEnvironment; }
    public void setTargetEnvironment(String targetEnvironment) { this.targetEnvironment = targetEnvironment; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getInitiatedAt() { return initiatedAt; }
    public void setInitiatedAt(LocalDateTime initiatedAt) { this.initiatedAt = initiatedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
