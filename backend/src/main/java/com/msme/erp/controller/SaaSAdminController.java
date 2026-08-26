package com.msme.erp.controller;

import com.msme.erp.domain.BillingAuditLog;
import com.msme.erp.service.SaaSAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_FACTORY_OWNER')")
public class SaaSAdminController {

    private final SaaSAdminService adminService;

    public SaaSAdminController(SaaSAdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getPlatformOverview() {
        return ResponseEntity.ok(adminService.getPlatformOverview());
    }

    @GetMapping("/tenants")
    public ResponseEntity<List<Map<String, Object>>> getAllTenants() {
        return ResponseEntity.ok(adminService.getAllTenants());
    }

    @PostMapping("/tenants/{id}/suspend")
    public ResponseEntity<Map<String, Object>> suspendTenant(@PathVariable String id, @RequestBody Map<String, String> payload) {
        String reason = payload.getOrDefault("reason", "Administrative policy");
        return ResponseEntity.ok(adminService.suspendTenant(id, reason));
    }

    @PostMapping("/tenants/{id}/reactivate")
    public ResponseEntity<Map<String, Object>> reactivateTenant(@PathVariable String id) {
        return ResponseEntity.ok(adminService.reactivateTenant(id));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<BillingAuditLog>> getAuditLogs() {
        return ResponseEntity.ok(adminService.getAuditLogs());
    }
}
