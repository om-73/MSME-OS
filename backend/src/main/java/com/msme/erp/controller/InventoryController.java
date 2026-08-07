package com.msme.erp.controller;

import com.msme.erp.dto.InventoryItemDto;
import com.msme.erp.dto.InventoryMovementDto;
import com.msme.erp.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemDto>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<InventoryItemDto>> getInventoryByCategory(@PathVariable String category) {
        return ResponseEntity.ok(inventoryService.getInventoryByCategory(category));
    }

    @GetMapping("/ledger")
    public ResponseEntity<List<InventoryMovementDto>> getLedger() {
        return ResponseEntity.ok(inventoryService.getLedger());
    }

    @GetMapping("/shortage")
    public ResponseEntity<List<InventoryItemDto>> getShortageReport() {
        return ResponseEntity.ok(inventoryService.getShortageReport());
    }

    @PostMapping("/receive")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_OPERATOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<InventoryItemDto> receiveStock(@RequestBody InventoryItemDto request) {
        return ResponseEntity.ok(inventoryService.receiveStock(request));
    }

    @PostMapping("/adjust")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<InventoryItemDto> adjustStock(@RequestBody Map<String, Object> payload) {
        String itemId = (String) payload.get("itemId");
        Double quantity = Double.valueOf(payload.get("quantity").toString());
        String movementType = (String) payload.get("movementType");
        String remarks = (String) payload.getOrDefault("remarks", "");
        return ResponseEntity.ok(inventoryService.adjustStock(itemId, quantity, movementType, remarks));
    }
}
