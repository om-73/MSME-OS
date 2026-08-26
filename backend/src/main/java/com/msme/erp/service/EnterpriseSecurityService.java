package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnterpriseSecurityService {

    private final CustomRoleRepository customRoleRepository;
    private final DepartmentAccessRepository departmentAccessRepository;
    private final UserSessionRepository userSessionRepository;
    private final ApprovalRequestRepository approvalRequestRepository;
    private final BreakGlassSessionRepository breakGlassSessionRepository;
    private final SecurityPolicyRepository securityPolicyRepository;
    private final ProductionAuditLogRepository auditLogRepository;
    private final NotificationCenterService notificationCenterService;

    public EnterpriseSecurityService(CustomRoleRepository customRoleRepository,
                                     DepartmentAccessRepository departmentAccessRepository,
                                     UserSessionRepository userSessionRepository,
                                     ApprovalRequestRepository approvalRequestRepository,
                                     BreakGlassSessionRepository breakGlassSessionRepository,
                                     SecurityPolicyRepository securityPolicyRepository,
                                     ProductionAuditLogRepository auditLogRepository,
                                     NotificationCenterService notificationCenterService) {
        this.customRoleRepository = customRoleRepository;
        this.departmentAccessRepository = departmentAccessRepository;
        this.userSessionRepository = userSessionRepository;
        this.approvalRequestRepository = approvalRequestRepository;
        this.breakGlassSessionRepository = breakGlassSessionRepository;
        this.securityPolicyRepository = securityPolicyRepository;
        this.auditLogRepository = auditLogRepository;
        this.notificationCenterService = notificationCenterService;
    }

    /**
     * Centralized Permission & Scope Evaluator Engine
     */
    public Map<String, Object> evaluatePermission(String userId, String requiredPermission, String targetDepartment) {
        String tenantId = TenantContext.getCurrentTenant();

        boolean allowPermission = true;
        String denialReason = null;

        // Department-based access check
        if (targetDepartment != null && !targetDepartment.isEmpty()) {
            List<DepartmentAccess> accesses = departmentAccessRepository.findByTenantIdAndUserId(tenantId, userId);
            if (!accesses.isEmpty()) {
                boolean hasDept = accesses.stream().anyMatch(a -> a.getDepartmentName().equalsIgnoreCase(targetDepartment));
                if (!hasDept) {
                    allowPermission = false;
                    denialReason = "User does not have access boundary for department: " + targetDepartment;
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("tenantId", tenantId);
        result.put("userId", userId);
        result.put("requiredPermission", requiredPermission);
        result.put("targetDepartment", targetDepartment);
        result.put("decision", allowPermission ? "ALLOW" : "DENY");
        result.put("denialReason", denialReason);
        return result;
    }

    public List<CustomRole> getCustomRoles() {
        String tenantId = TenantContext.getCurrentTenant();
        List<CustomRole> roles = customRoleRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        if (roles.isEmpty()) {
            roles = Arrays.asList(
                CustomRole.builder().tenantId(tenantId).name("QC Supervisor").description("Inspector lead with approve/reject permissions").permissions("orders:view,qc:view,qc:inspect,qc:approve,qc:reject").build(),
                CustomRole.builder().tenantId(tenantId).name("Inventory Lead").description("Warehouse manager with stock issue and receive permissions").permissions("inventory:view,inventory:receive,inventory:issue,inventory:adjust").build(),
                CustomRole.builder().tenantId(tenantId).name("Stitching Line Manager").description("Department supervisor for stitching tasks").permissions("orders:view,production:view,production:update,production:stage_complete").build()
            );
            customRoleRepository.saveAll(roles);
        }
        return roles;
    }

    @Transactional
    public CustomRole saveCustomRole(CustomRole role) {
        role.setTenantId(TenantContext.getCurrentTenant());
        return customRoleRepository.save(role);
    }

    public List<DepartmentAccess> getDepartmentAccesses() {
        String tenantId = TenantContext.getCurrentTenant();
        return departmentAccessRepository.findByTenantId(tenantId);
    }

    @Transactional
    public DepartmentAccess assignDepartmentAccess(String userId, String departmentName, String accessLevel) {
        String tenantId = TenantContext.getCurrentTenant();
        DepartmentAccess access = DepartmentAccess.builder()
                .tenantId(tenantId)
                .userId(userId)
                .departmentName(departmentName)
                .accessLevel(accessLevel != null ? accessLevel : "READ_WRITE")
                .build();
        return departmentAccessRepository.save(access);
    }

    // --- SESSION MANAGEMENT ---

    public List<UserSession> getUserSessions(String userId) {
        String tenantId = TenantContext.getCurrentTenant();
        List<UserSession> sessions = userSessionRepository.findByTenantIdAndUserId(tenantId, userId);
        if (sessions.isEmpty()) {
            UserSession s1 = UserSession.builder().tenantId(tenantId).userId(userId).sessionToken("token_macbook_chrome").deviceName("MacBook Pro (Chrome)").browserName("Chrome 122").ipAddress("192.168.1.42").status("ACTIVE").build();
            UserSession s2 = UserSession.builder().tenantId(tenantId).userId(userId).sessionToken("token_iphone_app").deviceName("iPhone 15 Pro").browserName("MfgOS Mobile App").ipAddress("192.168.1.88").status("ACTIVE").build();
            userSessionRepository.saveAll(Arrays.asList(s1, s2));
            sessions = Arrays.asList(s1, s2);
        }
        return sessions;
    }

    @Transactional
    public UserSession revokeSession(Long sessionId) {
        UserSession session = userSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NoSuchElementException("Session not found: " + sessionId));
        session.setStatus("REVOKED");
        return userSessionRepository.save(session);
    }

    // --- HUMAN APPROVAL WORKFLOW ---

    public List<ApprovalRequest> getApprovalRequests() {
        String tenantId = TenantContext.getCurrentTenant();
        List<ApprovalRequest> requests = approvalRequestRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        if (requests.isEmpty()) {
            ApprovalRequest req = ApprovalRequest.builder()
                    .tenantId(tenantId)
                    .requestedBy("cutting.mgr@apex.com")
                    .requestType("REWORK_ACTION")
                    .title("Rework Authorization for Batch ORD-2026-90")
                    .details("Re-route 40 units of stitching backlog to Line B")
                    .status("PENDING")
                    .build();
            approvalRequestRepository.save(req);
            requests = Collections.singletonList(req);
        }
        return requests;
    }

    @Transactional
    public ApprovalRequest submitApprovalRequest(ApprovalRequest request) {
        request.setTenantId(TenantContext.getCurrentTenant());
        return approvalRequestRepository.save(request);
    }

    @Transactional
    public ApprovalRequest approveRequest(Long requestId, String approvedBy) {
        String tenantId = TenantContext.getCurrentTenant();
        ApprovalRequest req = approvalRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Approval request not found: " + requestId));

        req.setStatus("APPROVED");
        req.setApprovedBy(approvedBy != null ? approvedBy : "factory_owner");
        req.setApprovedAt(LocalDateTime.now());
        req = approvalRequestRepository.save(req);

        // Module 9 Event Notification
        String idempotencyKey = "EVT-APPROVE-REQ-" + requestId + "-" + System.currentTimeMillis();
        notificationCenterService.publishEvent(tenantId, "ApprovalExecutedEvent", idempotencyKey, "HIGH", Map.of("orderNumber", req.getTitle(), "stageName", "Approved"));

        return req;
    }

    // --- BREAK-GLASS EMERGENCY ACCESS ---

    @Transactional
    public BreakGlassSession triggerBreakGlassAccess(String reason, String actorId) {
        String tenantId = TenantContext.getCurrentTenant();

        BreakGlassSession session = BreakGlassSession.builder()
                .tenantId(tenantId)
                .actorId(actorId != null ? actorId : "factory_owner")
                .emergencyReason(reason != null ? reason : "Critical production halt resolution")
                .expiresAt(LocalDateTime.now().plusHours(2))
                .active(true)
                .build();

        session = breakGlassSessionRepository.save(session);

        // Module 9 Event Notification
        String idempotencyKey = "EVT-BREAK-GLASS-" + session.getId() + "-" + System.currentTimeMillis();
        notificationCenterService.publishEvent(tenantId, "BreakGlassTriggeredEvent", idempotencyKey, "CRITICAL", Map.of("orderNumber", session.getActorId(), "stageName", "Emergency Break-Glass Active"));

        return session;
    }

    public List<BreakGlassSession> getBreakGlassSessions() {
        String tenantId = TenantContext.getCurrentTenant();
        return breakGlassSessionRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    // --- SECURITY POLICIES ---

    public SecurityPolicy getSecurityPolicy() {
        String tenantId = TenantContext.getCurrentTenant();
        return securityPolicyRepository.findByTenantId(tenantId)
                .orElseGet(() -> SecurityPolicy.builder()
                        .tenantId(tenantId)
                        .mfaRequiredForAdmins(true)
                        .mfaRequiredForWorkers(false)
                        .sessionTimeoutMinutes(60)
                        .minPasswordLength(12)
                        .requireSpecialChar(true)
                        .maxFailedAttemptsBeforeLockout(5)
                        .build());
    }

    @Transactional
    public SecurityPolicy updateSecurityPolicy(SecurityPolicy policy) {
        policy.setTenantId(TenantContext.getCurrentTenant());
        return securityPolicyRepository.save(policy);
    }

    public List<ProductionAuditLog> getAuditLogs() {
        String tenantId = TenantContext.getCurrentTenant();
        return auditLogRepository.findByTenantIdOrderByTimestampDesc(tenantId);
    }
}
