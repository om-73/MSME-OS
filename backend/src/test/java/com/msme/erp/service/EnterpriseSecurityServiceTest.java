package com.msme.erp.service;

import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnterpriseSecurityServiceTest {

    @Mock
    private CustomRoleRepository customRoleRepository;

    @Mock
    private DepartmentAccessRepository departmentAccessRepository;

    @Mock
    private UserSessionRepository userSessionRepository;

    @Mock
    private ApprovalRequestRepository approvalRequestRepository;

    @Mock
    private BreakGlassSessionRepository breakGlassSessionRepository;

    @Mock
    private SecurityPolicyRepository securityPolicyRepository;

    @Mock
    private ProductionAuditLogRepository auditLogRepository;

    @Mock
    private NotificationCenterService notificationCenterService;

    @InjectMocks
    private EnterpriseSecurityService securityService;

    @BeforeEach
    void setUp() {
        com.msme.erp.config.TenantContext.setCurrentTenant("apex-tenant-01");
    }

    @Test
    void testEvaluatePermissionDeniesDepartmentMismatch() {
        DepartmentAccess access = DepartmentAccess.builder()
                .tenantId("apex-tenant-01")
                .userId("worker@apex.com")
                .departmentName("Cutting")
                .accessLevel("READ_WRITE")
                .build();

        when(departmentAccessRepository.findByTenantIdAndUserId("apex-tenant-01", "worker@apex.com"))
                .thenReturn(Collections.singletonList(access));

        Map<String, Object> result = securityService.evaluatePermission("worker@apex.com", "production:update", "Stitching");

        assertEquals("DENY", result.get("decision"));
        assertTrue(((String) result.get("denialReason")).contains("Stitching"));
    }

    @Test
    void testApprovalExecutionFiresNotification() {
        Long reqId = 12L;
        ApprovalRequest req = ApprovalRequest.builder()
                .id(reqId)
                .tenantId("apex-tenant-01")
                .requestedBy("operator@apex.com")
                .title("Inventory Adjustment")
                .status("PENDING")
                .build();

        when(approvalRequestRepository.findById(reqId)).thenReturn(Optional.of(req));
        when(approvalRequestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ApprovalRequest approved = securityService.approveRequest(reqId, "factory_owner");

        assertEquals("APPROVED", approved.getStatus());
        assertEquals("factory_owner", approved.getApprovedBy());
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("ApprovalExecutedEvent"), any(), eq("HIGH"), any());
    }

    @Test
    void testTriggerBreakGlassCreatesEmergencySession() {
        when(breakGlassSessionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BreakGlassSession session = securityService.triggerBreakGlassAccess("Critical production halt resolution", "admin@apex.com");

        assertNotNull(session);
        assertEquals("admin@apex.com", session.getActorId());
        assertTrue(session.isActive());
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("BreakGlassTriggeredEvent"), any(), eq("CRITICAL"), any());
    }
}
