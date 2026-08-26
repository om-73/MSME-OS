package com.msme.erp.controller;

import com.msme.erp.domain.MobileDevice;
import com.msme.erp.service.MobileOperationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mobile")
public class MobileOperationsController {

    private final MobileOperationsService mobileService;

    public MobileOperationsController(MobileOperationsService mobileService) {
        this.mobileService = mobileService;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMobileProfile(@RequestParam(required = false, defaultValue = "worker@apex.com") String userId) {
        return ResponseEntity.ok(mobileService.getMobileUserProfile(userId));
    }

    @PostMapping("/device/register")
    public ResponseEntity<MobileDevice> registerDevice(@RequestBody MobileDevice device) {
        return ResponseEntity.ok(mobileService.registerMobileDevice(device));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Map<String, Object>>> getAssignedTasks(@RequestParam(required = false, defaultValue = "worker@apex.com") String userId,
                                                                      @RequestParam(required = false, defaultValue = "Cutting") String department) {
        return ResponseEntity.ok(mobileService.getAssignedTasks(userId, department));
    }

    @PostMapping("/tasks/{id}/action")
    public ResponseEntity<Map<String, Object>> executeTaskAction(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String action = payload.getOrDefault("action", "START");
        String issueReason = payload.get("issueReason");
        String photoUrl = payload.get("photoUrl");
        return ResponseEntity.ok(mobileService.executeTaskAction(id, action, issueReason, photoUrl));
    }

    @PostMapping("/qc/inspect")
    public ResponseEntity<Map<String, Object>> submitQCInspection(@RequestBody Map<String, Object> payload) {
        Long orderId = Long.valueOf(payload.getOrDefault("orderId", 101).toString());
        String result = payload.getOrDefault("qcResult", "PASS").toString();
        String defectType = (String) payload.get("defectType");
        return ResponseEntity.ok(mobileService.submitQCInspection(orderId, result, defectType));
    }

    @GetMapping("/inventory/scan")
    public ResponseEntity<Map<String, Object>> scanBarcode(@RequestParam String barcode) {
        return ResponseEntity.ok(mobileService.scanBarcode(barcode));
    }

    @PostMapping("/sync")
    public ResponseEntity<List<Map<String, Object>>> processOfflineSync(@RequestBody List<Map<String, String>> queuedActions,
                                                                        @RequestParam(required = false, defaultValue = "worker@apex.com") String userId) {
        return ResponseEntity.ok(mobileService.processOfflineSyncQueue(queuedActions, userId));
    }
}
