package com.msme.erp.controller;

import com.msme.erp.domain.PurchaseOrder;
import com.msme.erp.service.ProcurementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/procurement/orders")
public class ProcurementController {

    private final ProcurementService procurementService;

    public ProcurementController(ProcurementService procurementService) {
        this.procurementService = procurementService;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrders() {
        return ResponseEntity.ok(procurementService.getPurchaseOrders());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody Map<String, Object> payload) {
        String vendorName = (String) payload.get("vendorName");
        List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");
        return ResponseEntity.ok(procurementService.createPurchaseOrder(vendorName, items));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<PurchaseOrder> approvePurchaseOrder(@PathVariable Long id) {
        return ResponseEntity.ok(procurementService.approvePurchaseOrder(id));
    }

    @PostMapping("/{id}/receive")
    public ResponseEntity<PurchaseOrder> receiveStockPartial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload
    ) {
        Map<String, Double> receipts = (Map<String, Double>) payload.get("receipts");
        String invoiceNumber = (String) payload.getOrDefault("invoiceNumber", "");
        return ResponseEntity.ok(procurementService.receiveStockPartial(id, receipts, invoiceNumber));
    }
}
