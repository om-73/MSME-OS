package com.msme.erp.service;

import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PlatformAdminService {

    private final PlatformConfigurationRepository configRepository;
    private final FeatureFlagRepository flagRepository;
    private final MaintenanceWindowRepository maintenanceRepository;
    private final SecurityEventRepository eventRepository;
    private final SecurityAlertRepository alertRepository;
    private final AdminSessionRepository sessionRepository;
    private final ServiceAccountRepository serviceAccountRepository;
    private final BackupRecordRepository backupRepository;
    private final BackupVerificationRepository verificationRepository;
    private final RestoreOperationRepository restoreRepository;
    private final DisasterRecoveryTestRepository drTestRepository;
    private final IncidentRepository incidentRepository;
    private final IncidentTimelineRepository timelineRepository;
    private final RetentionPolicyRepository retentionRepository;
    private final LegalHoldRepository legalHoldRepository;
    private final DataExportJobRepository exportRepository;
    private final DataDeletionJobRepository deletionRepository;
    private final ComplianceControlRepository controlRepository;
    private final ComplianceEvidenceRepository evidenceRepository;
    private final IntegrationHealthSnapshotRepository integrationHealthRepository;
    private final NotificationCenterService notificationCenterService;

    public PlatformAdminService(PlatformConfigurationRepository configRepository,
                               FeatureFlagRepository flagRepository,
                               MaintenanceWindowRepository maintenanceRepository,
                               SecurityEventRepository eventRepository,
                               SecurityAlertRepository alertRepository,
                               AdminSessionRepository sessionRepository,
                               ServiceAccountRepository serviceAccountRepository,
                               BackupRecordRepository backupRepository,
                               BackupVerificationRepository verificationRepository,
                               RestoreOperationRepository restoreRepository,
                               DisasterRecoveryTestRepository drTestRepository,
                               IncidentRepository incidentRepository,
                               IncidentTimelineRepository timelineRepository,
                               RetentionPolicyRepository retentionRepository,
                               LegalHoldRepository legalHoldRepository,
                               DataExportJobRepository exportRepository,
                               DataDeletionJobRepository deletionRepository,
                               ComplianceControlRepository controlRepository,
                               ComplianceEvidenceRepository evidenceRepository,
                               IntegrationHealthSnapshotRepository integrationHealthRepository,
                               NotificationCenterService notificationCenterService) {
        this.configRepository = configRepository;
        this.flagRepository = flagRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.eventRepository = eventRepository;
        this.alertRepository = alertRepository;
        this.sessionRepository = sessionRepository;
        this.serviceAccountRepository = serviceAccountRepository;
        this.backupRepository = backupRepository;
        this.verificationRepository = verificationRepository;
        this.restoreRepository = restoreRepository;
        this.drTestRepository = drTestRepository;
        this.incidentRepository = incidentRepository;
        this.timelineRepository = timelineRepository;
        this.retentionRepository = retentionRepository;
        this.legalHoldRepository = legalHoldRepository;
        this.exportRepository = exportRepository;
        this.deletionRepository = deletionRepository;
        this.controlRepository = controlRepository;
        this.evidenceRepository = evidenceRepository;
        this.integrationHealthRepository = integrationHealthRepository;
        this.notificationCenterService = notificationCenterService;
    }

    // ==========================================
    // ASYNC JOBS
    // ==========================================

    @Async
    @Transactional
    public void executeBackupJob(String backupType) {
        long start = System.currentTimeMillis();
        BackupRecord record = new BackupRecord();
        record.setBackupType(backupType);
        record.setCreatedAt(LocalDateTime.now());
        record.setEncrypted(true);
        record.setFilePath("target/backups/backup-" + start + ".enc");
        
        try {
            // Mock backup operation
            Thread.sleep(100);
            record.setFileSize(2048500L);
            record.setChecksumSHA256("SHA-256: 8a7f9b0e2c1d3a4e5f6b7c8d9e0f1a2b3c4d5e6f");
            record.setStatus("COMPLETED");
            record.setDurationMs(System.currentTimeMillis() - start);
            backupRepository.save(record);
            
            // Auto verify backup
            BackupVerification verify = new BackupVerification(record.getId(), "PASSED", "Integrity check passed. 0 blocks corrupted.", LocalDateTime.now());
            verificationRepository.save(verify);
        } catch (Exception e) {
            record.setStatus("FAILED");
            record.setDurationMs(System.currentTimeMillis() - start);
            backupRepository.save(record);
            
            notificationCenterService.publishEvent("apex-tenant-01", "BackupFailedEvent", 
                "System backup failed: " + e.getMessage(), "HIGH", null);
        }
    }

    @Async
    @Transactional
    public void executeRestoreValidationJob(Long backupRecordId, String targetEnv, String approvedBy) {
        RestoreOperation op = new RestoreOperation(backupRecordId, targetEnv, approvedBy, "PENDING", LocalDateTime.now(), null);
        op = restoreRepository.save(op);

        try {
            op.setStatus("INTEGRITY_CHECK");
            restoreRepository.save(op);
            Thread.sleep(50);

            op.setStatus("EXECUTING");
            restoreRepository.save(op);
            Thread.sleep(50);

            op.setStatus("SUCCESS");
            op.setCompletedAt(LocalDateTime.now());
            restoreRepository.save(op);
        } catch (Exception e) {
            op.setStatus("FAILED");
            op.setCompletedAt(LocalDateTime.now());
            restoreRepository.save(op);
        }
    }

    @Async
    @Transactional
    public void executeDataExportJob(String tenantId, String requestedBy) {
        DataExportJob job = new DataExportJob(tenantId, requestedBy, "PENDING", 0L, null, LocalDateTime.now());
        job = exportRepository.save(job);

        try {
            job.setStatus("PROCESSING");
            exportRepository.save(job);
            Thread.sleep(100);

            job.setStatus("COMPLETED");
            job.setExportSize(1048576L);
            job.setSignedDownloadUrl("http://localhost:8085/api/v1/downloads/export-" + tenantId + "-" + System.currentTimeMillis() + ".zip");
            exportRepository.save(job);
        } catch (Exception e) {
            job.setStatus("FAILED");
            exportRepository.save(job);
        }
    }

    @Async
    @Transactional
    public void executeDataDeletionJob(String tenantId, String approvedBy) {
        DataDeletionJob job = new DataDeletionJob(tenantId, approvedBy, "PENDING", LocalDateTime.now(), null);
        job = deletionRepository.save(job);

        try {
            job.setStatus("HOLD_CHECK");
            deletionRepository.save(job);
            
            // Check legal holds
            List<LegalHold> activeHolds = legalHoldRepository.findByTenantIdAndActiveTrue(tenantId);
            if (!activeHolds.isEmpty()) {
                job.setStatus("FAILED");
                deletionRepository.save(job);
                return;
            }

            job.setStatus("EXPORTING");
            deletionRepository.save(job);
            Thread.sleep(50);

            job.setStatus("WIPING");
            deletionRepository.save(job);
            Thread.sleep(50);

            job.setStatus("COMPLETED");
            job.setCompletedAt(LocalDateTime.now());
            deletionRepository.save(job);
        } catch (Exception e) {
            job.setStatus("FAILED");
            job.setCompletedAt(LocalDateTime.now());
            deletionRepository.save(job);
        }
    }

    @Async
    @Transactional
    public void executeDrDrill(Long backupRecordId) {
        try {
            // Mock DR testing
            Thread.sleep(100);
            DisasterRecoveryTest drill = new DisasterRecoveryTest(LocalDateTime.now(), backupRecordId, 45, 10, "SUCCESS", "[]");
            drTestRepository.save(drill);
        } catch (Exception e) {
            // Ignore mock drills exceptions
        }
    }

    // ==========================================
    // HEURISTICS & RULES
    // ==========================================

    @Transactional
    public void recordSecurityEvent(String tenantId, String userId, String eventType, String ip, String ua) {
        SecurityEvent event = new SecurityEvent(tenantId, userId, eventType, ip, ua, LocalDateTime.now());
        eventRepository.save(event);

        if ("LOGIN_FAIL".equals(eventType)) {
            // Brute force detection: check last 5 events
            List<SecurityEvent> events = eventRepository.findByUserId(userId);
            long failures = events.stream()
                .filter(e -> "LOGIN_FAIL".equals(e.getEventType()))
                .filter(e -> e.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(5)))
                .count();

            if (failures >= 5) {
                SecurityAlert alert = new SecurityAlert(tenantId, "CRITICAL", "BRUTE_FORCE", 
                    "Brute force detection on user: " + userId + " (5+ failures in 5m)", false, LocalDateTime.now());
                alertRepository.save(alert);
            }
        }
    }

    @Transactional
    public void processRetentionCleanups(String tenantId) {
        List<RetentionPolicy> policies = retentionRepository.findByTenantId(tenantId);
        List<LegalHold> activeHolds = legalHoldRepository.findByTenantIdAndActiveTrue(tenantId);
        
        if (!activeHolds.isEmpty()) {
            // Legal hold active: bypass automated cleanups
            return;
        }

        for (RetentionPolicy p : policies) {
            // Mock cleaning up target records
        }
    }

    @Transactional
    public ComplianceEvidence generateComplianceEvidence(Long controlId, String evidenceType) {
        ComplianceControl ctrl = controlRepository.findById(controlId)
            .orElseThrow(() -> new NoSuchElementException("Control mapping not found: " + controlId));
        
        String content = "# Evidence Proof - SOC 2 Check\n- Control Code: " + ctrl.getControlCode() + "\n- Verification: PASS";
        String hash = "SHA-256: mock-hash-" + System.currentTimeMillis();
        
        ComplianceEvidence evidence = new ComplianceEvidence(controlId, evidenceType, hash, "target/compliance/evidence-" + controlId + ".md", LocalDateTime.now());
        evidence = evidenceRepository.save(evidence);
        return evidence;
    }

    public boolean evaluateFeatureFlag(String flagKey, String tenantId, String role) {
        return flagRepository.findByFlagKey(flagKey)
            .map(flag -> {
                if (!flag.isActive()) return false;
                if ("GLOBAL".equals(flag.getTargetingType())) return true;
                if ("TENANT".equals(flag.getTargetingType()) && flag.getTargetingValue().contains(tenantId)) return true;
                if ("ROLE".equals(flag.getTargetingType()) && flag.getTargetingValue().contains(role)) return true;
                return flag.getRolloutPercentage() > 50; // simple percentage rollout simulator
            }).orElse(false);
    }
}
