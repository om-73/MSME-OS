package com.msme.erp.service;

import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MachineIntegrationServiceTest {

    @Mock
    private MachineRepository machineRepository;

    @Mock
    private IoTDeviceRepository ioTDeviceRepository;

    @Mock
    private MachineTelemetryRepository telemetryRepository;

    @Mock
    private MachineDowntimeRepository downtimeRepository;

    @Mock
    private MachineMaintenanceRepository maintenanceRepository;

    @Mock
    private NotificationCenterService notificationCenterService;

    @InjectMocks
    private MachineIntegrationService machineService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testIngestTelemetryTriggersDowntimeAndNotificationOnOverheating() {
        Machine m = Machine.builder().id(4L).tenantId("apex-tenant-01").machineCode("STITCH-004").status("RUNNING").build();

        when(machineRepository.findById(4L)).thenReturn(Optional.of(m));
        when(telemetryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MachineTelemetry tel = machineService.ingestTelemetry(4L, 200L, 4.0, 92.5); // Overheating

        assertEquals("STOPPED", m.getStatus());
        verify(downtimeRepository, times(1)).save(any(MachineDowntime.class));
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("CriticalTemperatureEvent"), any(), eq("CRITICAL"), any());
    }

    @Test
    void testOeeMetricsCalculation() {
        Machine m = Machine.builder().id(4L).tenantId("apex-tenant-01").machineCode("STITCH-004").availabilityPct(92.0).performancePct(94.0).qualityPct(97.6).build();

        when(machineRepository.findById(4L)).thenReturn(Optional.of(m));

        Map<String, Object> oee = machineService.calculateOeeMetrics(4L);

        assertEquals(84.4, oee.get("oeeScorePct"));
    }

    @Test
    void testScheduleMaintenanceSetsMachineStatusToMaintenance() {
        Machine m = Machine.builder().id(4L).tenantId("apex-tenant-01").machineCode("STITCH-004").status("RUNNING").build();

        when(machineRepository.findById(4L)).thenReturn(Optional.of(m));
        when(maintenanceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MachineMaintenance maint = machineService.scheduleMaintenance(4L, "PREVENTIVE", "Oil change", "RM-OIL-FILTER-01");

        assertEquals("MAINTENANCE", m.getStatus());
        assertEquals("RM-OIL-FILTER-01", maint.getSparePartsUsed());
    }
}
