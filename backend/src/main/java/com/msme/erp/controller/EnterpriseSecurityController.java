package com.msme.erp.controller;

import com.msme.erp.domain.*;
import com.msme.erp.service.EnterpriseSecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/security")
public class EnterpriseSecurityController {

    private final EnterpriseSecurityService securityService;

    public EnterpriseSecurityController(EnterpriseSecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/permission/evaluate")
    public ResponseEntity<Map<String, Object>> evaluatePermission(@RequestBody Map<String, String> payload) {
        String userId = payload.getOrDefault("userId", "user@apex.com");
        String permission = payload.getOrDefault("permission", "orders:view");
        String department = payload.get("department");
        return ResponseEntity.ok(securityService.evaluatePermission(userId, permission, department));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<CustomRole>> getCustomRoles() {
        return ResponseEntity.ok(securityService.getCustomRoles());
    }

    @PostMapping("/roles")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<CustomRole> saveCustomRole(@RequestBody CustomRole role) {
        return ResponseEntity.ok(securityService.saveCustomRole(role));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentAccess>> getDepartmentAccesses() {
        return ResponseEntity.ok(securityService.getDepartmentAccesses());
    }

    @PostMapping("/departments/assign")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<DepartmentAccess> assignDepartmentAccess(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String departmentName = payload.get("departmentName");
        String accessLevel = payload.getOrDefault("accessLevel", "READ_WRITE");
        return ResponseEntity.ok(securityService.assignDepartmentAccess(userId, departmentName, accessLevel));
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<UserSession>> getUserSessions(@RequestParam(required = false, defaultValue = "user@apex.com") String userId) {
        return ResponseEntity.ok(securityService.getUserSessions(userId));
    }

    @PostMapping("/sessions/{id}/revoke")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<UserSession> revokeSession(@PathVariable Long id) {
        return ResponseEntity.ok(securityService.revokeSession(id));
    }

    @GetMapping("/approvals")
    public ResponseEntity<List<ApprovalRequest>> getApprovalRequests() {
        return ResponseEntity.ok(securityService.getApprovalRequests());
    }

    @PostMapping("/approvals")
    public ResponseEntity<ApprovalRequest> submitApprovalRequest(@RequestBody ApprovalRequest request) {
        return ResponseEntity.ok(securityService.submitApprovalRequest(request));
    }

    @PostMapping("/approvals/{id}/approve")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApprovalRequest> approveRequest(@PathVariable Long id, @RequestParam(required = false, defaultValue = "factory_owner") String approvedBy) {
        return ResponseEntity.ok(securityService.approveRequest(id, approvedBy));
    }

    @PostMapping("/break-glass")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<BreakGlassSession> triggerBreakGlass(@RequestBody Map<String, String> payload) {
        String reason = payload.getOrDefault("reason", "Emergency administrative production fix");
        String actorId = payload.getOrDefault("actorId", "factory_owner");
        return ResponseEntity.ok(securityService.triggerBreakGlassAccess(reason, actorId));
    }

    @GetMapping("/break-glass")
    public ResponseEntity<List<BreakGlassSession>> getBreakGlassSessions() {
        return ResponseEntity.ok(securityService.getBreakGlassSessions());
    }

    @GetMapping("/policies")
    public ResponseEntity<SecurityPolicy> getSecurityPolicy() {
        return ResponseEntity.ok(securityService.getSecurityPolicy());
    }

    @PostMapping("/policies")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<SecurityPolicy> updateSecurityPolicy(@RequestBody SecurityPolicy policy) {
        return ResponseEntity.ok(securityService.updateSecurityPolicy(policy));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<ProductionAuditLog>> getAuditLogs() {
        return ResponseEntity.ok(securityService.getAuditLogs());
    }
}
