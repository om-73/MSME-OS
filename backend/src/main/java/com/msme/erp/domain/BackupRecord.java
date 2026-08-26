package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "backup_records")
public class BackupRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String backupType; // FULL, INCREMENTAL, CONFIG
    private String filePath;
    private Long fileSize;
    private String checksumSHA256;
    private boolean encrypted;
    private String status; // COMPLETED, FAILED
    private Long durationMs;
    private LocalDateTime createdAt;

    public BackupRecord() {}
    public BackupRecord(String backupType, String filePath, Long fileSize, String checksumSHA256, boolean encrypted, String status, Long durationMs, LocalDateTime createdAt) {
        this.backupType = backupType;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.checksumSHA256 = checksumSHA256;
        this.encrypted = encrypted;
        this.status = status;
        this.durationMs = durationMs;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBackupType() { return backupType; }
    public void setBackupType(String backupType) { this.backupType = backupType; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getChecksumSHA256() { return checksumSHA256; }
    public void setChecksumSHA256(String checksumSHA256) { this.checksumSHA256 = checksumSHA256; }
    public boolean isEncrypted() { return encrypted; }
    public void setEncrypted(boolean encrypted) { this.encrypted = encrypted; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
