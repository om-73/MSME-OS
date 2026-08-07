package com.msme.erp.controller;

import com.msme.erp.dto.InventoryAuditDto;
import com.msme.erp.service.InventoryAuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventory/audit")
public class InventoryAuditController {

    private final InventoryAuditService auditService;

    public InventoryAuditController(InventoryAuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryAuditDto>> getAudits() {
        return ResponseEntity.ok(auditService.getAudits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryAuditDto> getAuditDetails(@PathVariable String id) {
        return ResponseEntity.ok(auditService.getAuditDetails(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<InventoryAuditDto> startAudit(@RequestBody Map<String, String> payload) {
        String auditName = payload.get("auditName");
        return ResponseEntity.ok(auditService.startAudit(auditName));
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_OPERATOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<InventoryAuditDto> submitCounts(@PathVariable String id, @RequestBody Map<String, Double> counts) {
        return ResponseEntity.ok(auditService.submitCounts(id, counts));
    }

    @PostMapping("/{id}/reconcile")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<InventoryAuditDto> reconcileAudit(@PathVariable String id) {
        return ResponseEntity.ok(auditService.reconcileAudit(id));
    }
}
