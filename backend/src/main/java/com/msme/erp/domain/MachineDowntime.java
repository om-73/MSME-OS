package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "machine_downtime")
public class MachineDowntime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private Long machineId;

    @Column(nullable = false)
    private String downtimeReason; // MACHINE_FAILURE, MATERIAL_SHORTAGE, OPERATOR_UNAVAILABLE, POWER_FAILURE

    private int durationMinutes = 15;

    @Column(length = 1000)
    private String notes;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public MachineDowntime() {}

    public MachineDowntime(Long id, String tenantId, Long machineId, String downtimeReason, int durationMinutes, String notes, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.tenantId = tenantId;
        this.machineId = machineId;
        this.downtimeReason = downtimeReason;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static MachineDowntimeBuilder builder() {
        return new MachineDowntimeBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    public String getDowntimeReason() { return downtimeReason; }
    public void setDowntimeReason(String downtimeReason) { this.downtimeReason = downtimeReason; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    @PrePersist
    protected void onCreate() {
        if (startTime == null) startTime = LocalDateTime.now();
    }

    public static class MachineDowntimeBuilder {
        private Long id;
        private String tenantId;
        private Long machineId;
        private String downtimeReason;
        private int durationMinutes = 15;
        private String notes;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public MachineDowntimeBuilder id(Long id) { this.id = id; return this; }
        public MachineDowntimeBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public MachineDowntimeBuilder machineId(Long machineId) { this.machineId = machineId; return this; }
        public MachineDowntimeBuilder downtimeReason(String downtimeReason) { this.downtimeReason = downtimeReason; return this; }
        public MachineDowntimeBuilder durationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public MachineDowntimeBuilder notes(String notes) { this.notes = notes; return this; }
        public MachineDowntimeBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public MachineDowntimeBuilder endTime(LocalDateTime endTime) { this.endTime = endTime; return this; }

        public MachineDowntime build() {
            return new MachineDowntime(id, tenantId, machineId, downtimeReason, durationMinutes, notes, startTime, endTime);
        }
    }
}
