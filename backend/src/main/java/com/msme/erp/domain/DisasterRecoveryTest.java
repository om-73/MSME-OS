package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "disaster_recovery_tests")
public class DisasterRecoveryTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime testDate;
    private Long backupRecordId;
    private int measuredRTOMinutes;
    private int measuredRPOMinutes;
    private String status; // SUCCESS, FAILURE
    private String recordIssuesJson;

    public DisasterRecoveryTest() {}
    public DisasterRecoveryTest(LocalDateTime testDate, Long backupRecordId, int measuredRTOMinutes, int measuredRPOMinutes, String status, String recordIssuesJson) {
        this.testDate = testDate;
        this.backupRecordId = backupRecordId;
        this.measuredRTOMinutes = measuredRTOMinutes;
        this.measuredRPOMinutes = measuredRPOMinutes;
        this.status = status;
        this.recordIssuesJson = recordIssuesJson;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getTestDate() { return testDate; }
    public void setTestDate(LocalDateTime testDate) { this.testDate = testDate; }
    public Long getBackupRecordId() { return backupRecordId; }
    public void setBackupRecordId(Long backupRecordId) { this.backupRecordId = backupRecordId; }
    public int getMeasuredRTOMinutes() { return measuredRTOMinutes; }
    public void setMeasuredRTOMinutes(int measuredRTOMinutes) { this.measuredRTOMinutes = measuredRTOMinutes; }
    public int getMeasuredRPOMinutes() { return measuredRPOMinutes; }
    public void setMeasuredRPOMinutes(int measuredRPOMinutes) { this.measuredRPOMinutes = measuredRPOMinutes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRecordIssuesJson() { return recordIssuesJson; }
    public void setRecordIssuesJson(String recordIssuesJson) { this.recordIssuesJson = recordIssuesJson; }
}
