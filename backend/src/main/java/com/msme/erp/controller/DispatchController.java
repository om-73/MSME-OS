package com.msme.erp.controller;

import com.msme.erp.domain.DispatchRecord;
import com.msme.erp.service.DispatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dispatch")
public class DispatchController {

    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @GetMapping
    public ResponseEntity<List<DispatchRecord>> getDispatchQueue() {
        return ResponseEntity.ok(dispatchService.getDispatchQueue());
    }

    @PostMapping("/{id}/courier")
    public ResponseEntity<DispatchRecord> assignCourier(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        String vehicleNo = payload.get("vehicleNo");
        String courierName = payload.get("courierName");
        String trackingNumber = payload.get("trackingNumber");
        String invoiceNumber = payload.get("invoiceNumber");
        return ResponseEntity.ok(dispatchService.assignCourier(id, vehicleNo, courierName, trackingNumber, invoiceNumber));
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<DispatchRecord> verifyPackage(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> payload
    ) {
        boolean checklistPassed = payload.getOrDefault("checklistPassed", false);
        boolean barcodeVerified = payload.getOrDefault("barcodeVerified", false);
        return ResponseEntity.ok(dispatchService.verifyPackage(id, checklistPassed, barcodeVerified));
    }

    @PostMapping("/{id}/ship")
    public ResponseEntity<DispatchRecord> shipPackages(@PathVariable Long id) {
        return ResponseEntity.ok(dispatchService.shipPackages(id));
    }

    @PostMapping("/{id}/deliver")
    public ResponseEntity<DispatchRecord> deliverConfirm(@PathVariable Long id) {
        return ResponseEntity.ok(dispatchService.deliverConfirm(id));
    }
}
