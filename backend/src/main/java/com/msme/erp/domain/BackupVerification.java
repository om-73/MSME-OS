package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "backup_verifications")
public class BackupVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long backupRecordId;
    private String verificationStatus; // PASSED, FAILED
    private String issuesFound;
    private LocalDateTime testedAt;

    public BackupVerification() {}
    public BackupVerification(Long backupRecordId, String verificationStatus, String issuesFound, LocalDateTime testedAt) {
        this.backupRecordId = backupRecordId;
        this.verificationStatus = verificationStatus;
        this.issuesFound = issuesFound;
        this.testedAt = testedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBackupRecordId() { return backupRecordId; }
    public void setBackupRecordId(Long backupRecordId) { this.backupRecordId = backupRecordId; }
    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
    public String getIssuesFound() { return issuesFound; }
    public void setIssuesFound(String issuesFound) { this.issuesFound = issuesFound; }
    public LocalDateTime getTestedAt() { return testedAt; }
    public void setTestedAt(LocalDateTime testedAt) { this.testedAt = testedAt; }
}
