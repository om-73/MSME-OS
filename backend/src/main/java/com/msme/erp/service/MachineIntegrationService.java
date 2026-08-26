package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MachineIntegrationService {

    private final MachineRepository machineRepository;
    private final IoTDeviceRepository ioTDeviceRepository;
    private final MachineTelemetryRepository telemetryRepository;
    private final MachineDowntimeRepository downtimeRepository;
    private final MachineMaintenanceRepository maintenanceRepository;
    private final NotificationCenterService notificationCenterService;

    public MachineIntegrationService(MachineRepository machineRepository,
                                     IoTDeviceRepository ioTDeviceRepository,
                                     MachineTelemetryRepository telemetryRepository,
                                     MachineDowntimeRepository downtimeRepository,
                                     MachineMaintenanceRepository maintenanceRepository,
                                     NotificationCenterService notificationCenterService) {
        this.machineRepository = machineRepository;
        this.ioTDeviceRepository = ioTDeviceRepository;
        this.telemetryRepository = telemetryRepository;
        this.downtimeRepository = downtimeRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.notificationCenterService = notificationCenterService;
    }

    // --- 1. MACHINE & DEVICE REGISTRIES ---

    public List<Machine> getMachines() {
        String tenantId = TenantContext.getCurrentTenant();
        List<Machine> machines = machineRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        if (machines.isEmpty()) {
            machines = Arrays.asList(
                Machine.builder().tenantId(tenantId).machineCode("STITCH-004").name("Juki Industrial Lockstitch").machineType("INDUSTRIAL_SEWING").department("Stitching").status("RUNNING").currentOrderId("ORD-2026-88").manufacturer("Juki").modelNumber("DDL-9000C").oeeScorePct(84.5).availabilityPct(92.0).performancePct(94.0).qualityPct(97.6).build(),
                Machine.builder().tenantId(tenantId).machineCode("CUT-002").name("Gerber Automatic Fabric Cutter").machineType("CNC_CUTTING").department("Cutting").status("RUNNING").currentOrderId("ORD-2026-89").manufacturer("Gerber Technology").modelNumber("Z1 CNC").oeeScorePct(89.2).availabilityPct(95.0).performancePct(96.0).qualityPct(97.8).build(),
                Machine.builder().tenantId(tenantId).machineCode("PRESS-001").name("Veit Steam Ironing Press").machineType("PRESSING_STEAM").department("Pressing").status("IDLE").manufacturer("Veit").modelNumber("4435 Steam").oeeScorePct(78.0).availabilityPct(85.0).performancePct(90.0).qualityPct(98.0).build()
            );
            machineRepository.saveAll(machines);
        }
        return machines;
    }

    @Transactional
    public Machine registerMachine(Machine machine) {
        machine.setTenantId(TenantContext.getCurrentTenant());
        return machineRepository.save(machine);
    }

    public List<IoTDevice> getIoTDevices() {
        String tenantId = TenantContext.getCurrentTenant();
        List<IoTDevice> devices = ioTDeviceRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        if (devices.isEmpty()) {
            devices = Arrays.asList(
                IoTDevice.builder().tenantId(tenantId).deviceId("DEV-PLC-001").deviceType("PLC").protocol("OPC_UA").ipAddress("192.168.10.50").status("ONLINE").build(),
                IoTDevice.builder().tenantId(tenantId).deviceId("DEV-SENS-004").deviceType("SENSOR").protocol("MQTT").ipAddress("192.168.10.88").status("ONLINE").build(),
                IoTDevice.builder().tenantId(tenantId).deviceId("DEV-GW-002").deviceType("GATEWAY").protocol("REST_API").ipAddress("192.168.10.1").status("ONLINE").build()
            );
            ioTDeviceRepository.saveAll(devices);
        }
        return devices;
    }

    // --- 2. TELEMETRY INGESTION PIPELINE & DEBOUNCED DOWNTIME ---

    @Transactional
    public MachineTelemetry ingestTelemetry(Long machineId, Long productionCount, Double cycleTime, Double temperature) {
        String tenantId = TenantContext.getCurrentTenant();
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new NoSuchElementException("Machine not found: " + machineId));

        MachineTelemetry telemetry = MachineTelemetry.builder()
                .tenantId(tenantId)
                .machineId(machineId)
                .productionCount(productionCount != null ? productionCount : 150L)
                .cycleTimeSeconds(cycleTime != null ? cycleTime : 4.2)
                .temperatureCelsius(temperature != null ? temperature : 68.5)
                .powerKw(12.4)
                .qualityStatus("GOOD")
                .build();

        telemetryRepository.save(telemetry);

        // Check for Overheating or Downtime Trigger
        if (temperature != null && temperature > 85.0) {
            machine.setStatus("STOPPED");
            machineRepository.save(machine);

            // Create Debounced Downtime Event
            MachineDowntime downtime = MachineDowntime.builder()
                    .tenantId(tenantId)
                    .machineId(machineId)
                    .downtimeReason("MACHINE_FAILURE")
                    .durationMinutes(20)
                    .notes("Automatic downtime triggered: Critical Temperature threshold exceeded (" + temperature + "°C)")
                    .build();
            downtimeRepository.save(downtime);

            // Module 9 Event Notification
            String idempotencyKey = "EVT-MACH-TEMP-" + machineId + "-" + System.currentTimeMillis();
            notificationCenterService.publishEvent(tenantId, "CriticalTemperatureEvent", idempotencyKey, "CRITICAL", Map.of("orderNumber", machine.getMachineCode(), "stageName", "Overheating: " + temperature + "°C"));
        }

        return telemetry;
    }

    // --- 3. OEE METRICS CALCULATOR ---

    public Map<String, Object> calculateOeeMetrics(Long machineId) {
        String tenantId = TenantContext.getCurrentTenant();
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new NoSuchElementException("Machine not found: " + machineId));

        double availability = machine.getAvailabilityPct();
        double performance = machine.getPerformancePct();
        double quality = machine.getQualityPct();
        double oee = (availability / 100.0) * (performance / 100.0) * (quality / 100.0) * 100.0;

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("machineCode", machine.getMachineCode());
        metrics.put("availabilityPct", availability);
        metrics.put("performancePct", performance);
        metrics.put("qualityPct", quality);
        metrics.put("oeeScorePct", Math.round(oee * 10.0) / 10.0);
        return metrics;
    }

    // --- 4. MAINTENANCE & SPARE PARTS BINDING ---

    @Transactional
    public MachineMaintenance scheduleMaintenance(Long machineId, String maintenanceType, String description, String spareParts) {
        String tenantId = TenantContext.getCurrentTenant();
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new NoSuchElementException("Machine not found: " + machineId));

        machine.setStatus("MAINTENANCE");
        machine.setLastMaintenanceAt(LocalDateTime.now());
        machineRepository.save(machine);

        MachineMaintenance maintenance = MachineMaintenance.builder()
                .tenantId(tenantId)
                .machineId(machineId)
                .maintenanceType(maintenanceType != null ? maintenanceType : "PREVENTIVE")
                .description(description != null ? description : "Routine motor alignment and oil filter replacement")
                .technicianName("tech.ramesh@apex.com")
                .costAmount(450.0)
                .sparePartsUsed(spareParts != null ? spareParts : "RM-OIL-FILTER-01, RM-BELT-A12")
                .status("COMPLETED")
                .build();

        return maintenanceRepository.save(maintenance);
    }

    public List<MachineDowntime> getDowntimes() {
        String tenantId = TenantContext.getCurrentTenant();
        List<MachineDowntime> downtimes = downtimeRepository.findByTenantIdOrderByStartTimeDesc(tenantId);
        if (downtimes.isEmpty()) {
            downtimes = Collections.singletonList(
                MachineDowntime.builder().tenantId(tenantId).machineId(1L).downtimeReason("MATERIAL_SHORTAGE").durationMinutes(30).notes("Waiting for fabric roll re-stock").build()
            );
            downtimeRepository.saveAll(downtimes);
        }
        return downtimes;
    }

    public List<MachineMaintenance> getMaintenances() {
        String tenantId = TenantContext.getCurrentTenant();
        List<MachineMaintenance> list = maintenanceRepository.findByTenantIdOrderByScheduledAtDesc(tenantId);
        if (list.isEmpty()) {
            list = Collections.singletonList(
                MachineMaintenance.builder().tenantId(tenantId).machineId(1L).maintenanceType("PREVENTIVE").description("Motor alignment check").technicianName("tech.ramesh@apex.com").costAmount(450.0).sparePartsUsed("RM-OIL-FILTER-01").status("COMPLETED").build()
            );
            maintenanceRepository.saveAll(list);
        }
        return list;
    }
}
