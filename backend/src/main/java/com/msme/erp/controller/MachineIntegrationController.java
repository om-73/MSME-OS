package com.msme.erp.controller;

import com.msme.erp.domain.*;
import com.msme.erp.service.MachineIntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/machines")
public class MachineIntegrationController {

    private final MachineIntegrationService machineService;

    public MachineIntegrationController(MachineIntegrationService machineService) {
        this.machineService = machineService;
    }

    @GetMapping
    public ResponseEntity<List<Machine>> getMachines() {
        return ResponseEntity.ok(machineService.getMachines());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Machine> registerMachine(@RequestBody Machine machine) {
        return ResponseEntity.ok(machineService.registerMachine(machine));
    }

    @GetMapping("/devices")
    public ResponseEntity<List<IoTDevice>> getIoTDevices() {
        return ResponseEntity.ok(machineService.getIoTDevices());
    }

    @PostMapping("/{id}/telemetry")
    public ResponseEntity<MachineTelemetry> ingestTelemetry(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Long productionCount = payload.get("productionCount") != null ? Long.valueOf(payload.get("productionCount").toString()) : 150L;
        Double cycleTime = payload.get("cycleTimeSeconds") != null ? Double.valueOf(payload.get("cycleTimeSeconds").toString()) : 4.2;
        Double temperature = payload.get("temperatureCelsius") != null ? Double.valueOf(payload.get("temperatureCelsius").toString()) : 68.5;
        return ResponseEntity.ok(machineService.ingestTelemetry(id, productionCount, cycleTime, temperature));
    }

    @GetMapping("/{id}/oee")
    public ResponseEntity<Map<String, Object>> getOeeMetrics(@PathVariable Long id) {
        return ResponseEntity.ok(machineService.calculateOeeMetrics(id));
    }

    @PostMapping("/{id}/maintenance")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<MachineMaintenance> scheduleMaintenance(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String type = payload.getOrDefault("maintenanceType", "PREVENTIVE");
        String description = payload.getOrDefault("description", "Routine maintenance and oil change");
        String spareParts = payload.get("sparePartsUsed");
        return ResponseEntity.ok(machineService.scheduleMaintenance(id, type, description, spareParts));
    }

    @GetMapping("/downtime")
    public ResponseEntity<List<MachineDowntime>> getDowntimes() {
        return ResponseEntity.ok(machineService.getDowntimes());
    }

    @GetMapping("/maintenance")
    public ResponseEntity<List<MachineMaintenance>> getMaintenances() {
        return ResponseEntity.ok(machineService.getMaintenances());
    }
}
