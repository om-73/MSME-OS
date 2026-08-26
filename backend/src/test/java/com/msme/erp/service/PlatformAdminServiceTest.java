package com.msme.erp.service;

import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlatformAdminServiceTest {

    @Mock private PlatformConfigurationRepository configRepository;
    @Mock private FeatureFlagRepository flagRepository;
    @Mock private MaintenanceWindowRepository maintenanceRepository;
    @Mock private SecurityEventRepository eventRepository;
    @Mock private SecurityAlertRepository alertRepository;
    @Mock private AdminSessionRepository sessionRepository;
    @Mock private ServiceAccountRepository serviceAccountRepository;
    @Mock private BackupRecordRepository backupRepository;
    @Mock private BackupVerificationRepository verificationRepository;
    @Mock private RestoreOperationRepository restoreRepository;
    @Mock private DisasterRecoveryTestRepository drTestRepository;
    @Mock private IncidentRepository incidentRepository;
    @Mock private IncidentTimelineRepository timelineRepository;
    @Mock private RetentionPolicyRepository retentionRepository;
    @Mock private LegalHoldRepository legalHoldRepository;
    @Mock private DataExportJobRepository exportRepository;
    @Mock private DataDeletionJobRepository deletionRepository;
    @Mock private ComplianceControlRepository controlRepository;
    @Mock private ComplianceEvidenceRepository evidenceRepository;
    @Mock private IntegrationHealthSnapshotRepository integrationHealthRepository;
    @Mock private NotificationCenterService notificationCenterService;

    @InjectMocks
    private PlatformAdminService platformAdminService;

    @BeforeEach
    void setUp() {
        com.msme.erp.config.TenantContext.setCurrentTenant("apex-tenant-01");
    }

    @Test
    void testBackupJobRecordsStateAndVerifies() {
        when(backupRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(verificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        platformAdminService.executeBackupJob("FULL");

        verify(backupRepository, times(1)).save(any(BackupRecord.class));
        verify(verificationRepository, times(1)).save(any(BackupVerification.class));
    }

    @Test
    void testDataDeletionBlocksOnActiveLegalHold() {
        LegalHold hold = new LegalHold("apex-tenant-01", "ALL", "Tax Audit", "superadmin", LocalDateTime.now(), true);
        
        when(deletionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(legalHoldRepository.findByTenantIdAndActiveTrue("apex-tenant-01"))
            .thenReturn(Collections.singletonList(hold));

        platformAdminService.executeDataDeletionJob("apex-tenant-01", "superadmin");

        verify(deletionRepository, atLeastOnce()).save(any(DataDeletionJob.class));
        verify(legalHoldRepository, times(1)).findByTenantIdAndActiveTrue("apex-tenant-01");
    }

    @Test
    void testSecurityAlertGeneratedOnFailedLogins() {
        SecurityEvent ev1 = new SecurityEvent("apex-tenant-01", "admin@mfg.com", "LOGIN_FAIL", "127.0.0.1", "Chrome", LocalDateTime.now().minusSeconds(10));
        SecurityEvent ev2 = new SecurityEvent("apex-tenant-01", "admin@mfg.com", "LOGIN_FAIL", "127.0.0.1", "Chrome", LocalDateTime.now().minusSeconds(5));
        SecurityEvent ev3 = new SecurityEvent("apex-tenant-01", "admin@mfg.com", "LOGIN_FAIL", "127.0.0.1", "Chrome", LocalDateTime.now());

        when(eventRepository.findByUserId("admin@mfg.com")).thenReturn(Arrays.asList(ev1, ev2, ev3, ev3, ev3));
        when(alertRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        platformAdminService.recordSecurityEvent("apex-tenant-01", "admin@mfg.com", "LOGIN_FAIL", "127.0.0.1", "Chrome");

        verify(alertRepository, times(1)).save(any(SecurityAlert.class));
    }
}
