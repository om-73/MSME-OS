package com.msme.erp.controller;

import com.msme.erp.domain.Vendor;
import com.msme.erp.service.ProcurementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/procurement/vendors")
public class VendorController {

    private final ProcurementService procurementService;

    public VendorController(ProcurementService procurementService) {
        this.procurementService = procurementService;
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return ResponseEntity.ok(procurementService.getAllVendors());
    }

    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String code = payload.get("code");
        String email = payload.get("email");
        String phone = payload.get("phone");
        String address = payload.get("address");
        return ResponseEntity.ok(procurementService.createVendor(name, code, email, phone, address));
    }
}
