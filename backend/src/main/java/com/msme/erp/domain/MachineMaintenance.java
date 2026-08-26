package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "machine_maintenance")
public class MachineMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private Long machineId;

    @Column(nullable = false)
    private String maintenanceType; // PREVENTIVE, CORRECTIVE, EMERGENCY

    @Column(length = 1000, nullable = false)
    private String description;

    private String technicianName;
    private Double costAmount = 0.0;

    @Column(length = 1000)
    private String sparePartsUsed; // Refers to Module 7 Inventory items

    private String status = "COMPLETED"; // SCHEDULED, IN_PROGRESS, COMPLETED

    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;

    public MachineMaintenance() {}

    public MachineMaintenance(Long id, String tenantId, Long machineId, String maintenanceType, String description, String technicianName, Double costAmount, String sparePartsUsed, String status, LocalDateTime scheduledAt, LocalDateTime completedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.machineId = machineId;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.technicianName = technicianName;
        this.costAmount = costAmount;
        this.sparePartsUsed = sparePartsUsed;
        this.status = status;
        this.scheduledAt = scheduledAt;
        this.completedAt = completedAt;
    }

    public static MachineMaintenanceBuilder builder() {
        return new MachineMaintenanceBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }
    public Double getCostAmount() { return costAmount; }
    public void setCostAmount(Double costAmount) { this.costAmount = costAmount; }
    public String getSparePartsUsed() { return sparePartsUsed; }
    public void setSparePartsUsed(String sparePartsUsed) { this.sparePartsUsed = sparePartsUsed; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    @PrePersist
    protected void onCreate() {
        if (scheduledAt == null) scheduledAt = LocalDateTime.now();
        if (completedAt == null && "COMPLETED".equals(status)) completedAt = LocalDateTime.now();
    }

    public static class MachineMaintenanceBuilder {
        private Long id;
        private String tenantId;
        private Long machineId;
        private String maintenanceType;
        private String description;
        private String technicianName;
        private Double costAmount = 0.0;
        private String sparePartsUsed;
        private String status = "COMPLETED";
        private LocalDateTime scheduledAt;
        private LocalDateTime completedAt;

        public MachineMaintenanceBuilder id(Long id) { this.id = id; return this; }
        public MachineMaintenanceBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public MachineMaintenanceBuilder machineId(Long machineId) { this.machineId = machineId; return this; }
        public MachineMaintenanceBuilder maintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; return this; }
        public MachineMaintenanceBuilder description(String description) { this.description = description; return this; }
        public MachineMaintenanceBuilder technicianName(String technicianName) { this.technicianName = technicianName; return this; }
        public MachineMaintenanceBuilder costAmount(Double costAmount) { this.costAmount = costAmount; return this; }
        public MachineMaintenanceBuilder sparePartsUsed(String sparePartsUsed) { this.sparePartsUsed = sparePartsUsed; return this; }
        public MachineMaintenanceBuilder status(String status) { this.status = status; return this; }
        public MachineMaintenanceBuilder scheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; return this; }
        public MachineMaintenanceBuilder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public MachineMaintenance build() {
            return new MachineMaintenance(id, tenantId, machineId, maintenanceType, description, technicianName, costAmount, sparePartsUsed, status, scheduledAt, completedAt);
        }
    }
}
