package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "machines")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String machineCode; // e.g. STITCH-004

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String machineType; // INDUSTRIAL_SEWING, CNC_CUTTING, PRESSING_STEAM

    @Column(nullable = false)
    private String department; // Stitching, Cutting, Pressing

    @Column(nullable = false)
    private String status = "RUNNING"; // RUNNING, IDLE, STOPPED, MAINTENANCE, OFFLINE

    private String currentOrderId; // e.g. ORD-2026-88
    private String manufacturer;
    private String modelNumber;

    private Double oeeScorePct = 84.5;
    private Double availabilityPct = 92.0;
    private Double performancePct = 94.0;
    private Double qualityPct = 97.6;

    private LocalDateTime lastMaintenanceAt;
    private LocalDateTime nextMaintenanceAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Machine() {}

    public Machine(Long id, String tenantId, String machineCode, String name, String machineType, String department, String status, String currentOrderId, String manufacturer, String modelNumber, Double oeeScorePct, Double availabilityPct, Double performancePct, Double qualityPct, LocalDateTime lastMaintenanceAt, LocalDateTime nextMaintenanceAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.machineCode = machineCode;
        this.name = name;
        this.machineType = machineType;
        this.department = department;
        this.status = status;
        this.currentOrderId = currentOrderId;
        this.manufacturer = manufacturer;
        this.modelNumber = modelNumber;
        this.oeeScorePct = oeeScorePct;
        this.availabilityPct = availabilityPct;
        this.performancePct = performancePct;
        this.qualityPct = qualityPct;
        this.lastMaintenanceAt = lastMaintenanceAt;
        this.nextMaintenanceAt = nextMaintenanceAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MachineBuilder builder() {
        return new MachineBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getMachineCode() { return machineCode; }
    public void setMachineCode(String machineCode) { this.machineCode = machineCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMachineType() { return machineType; }
    public void setMachineType(String machineType) { this.machineType = machineType; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCurrentOrderId() { return currentOrderId; }
    public void setCurrentOrderId(String currentOrderId) { this.currentOrderId = currentOrderId; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getModelNumber() { return modelNumber; }
    public void setModelNumber(String modelNumber) { this.modelNumber = modelNumber; }
    public Double getOeeScorePct() { return oeeScorePct; }
    public void setOeeScorePct(Double oeeScorePct) { this.oeeScorePct = oeeScorePct; }
    public Double getAvailabilityPct() { return availabilityPct; }
    public void setAvailabilityPct(Double availabilityPct) { this.availabilityPct = availabilityPct; }
    public Double getPerformancePct() { return performancePct; }
    public void setPerformancePct(Double performancePct) { this.performancePct = performancePct; }
    public Double getQualityPct() { return qualityPct; }
    public void setQualityPct(Double qualityPct) { this.qualityPct = qualityPct; }
    public LocalDateTime getLastMaintenanceAt() { return lastMaintenanceAt; }
    public void setLastMaintenanceAt(LocalDateTime lastMaintenanceAt) { this.lastMaintenanceAt = lastMaintenanceAt; }
    public LocalDateTime getNextMaintenanceAt() { return nextMaintenanceAt; }
    public void setNextMaintenanceAt(LocalDateTime nextMaintenanceAt) { this.nextMaintenanceAt = nextMaintenanceAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static class MachineBuilder {
        private Long id;
        private String tenantId;
        private String machineCode;
        private String name;
        private String machineType;
        private String department;
        private String status = "RUNNING";
        private String currentOrderId;
        private String manufacturer;
        private String modelNumber;
        private Double oeeScorePct = 84.5;
        private Double availabilityPct = 92.0;
        private Double performancePct = 94.0;
        private Double qualityPct = 97.6;
        private LocalDateTime lastMaintenanceAt;
        private LocalDateTime nextMaintenanceAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public MachineBuilder id(Long id) { this.id = id; return this; }
        public MachineBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public MachineBuilder machineCode(String machineCode) { this.machineCode = machineCode; return this; }
        public MachineBuilder name(String name) { this.name = name; return this; }
        public MachineBuilder machineType(String machineType) { this.machineType = machineType; return this; }
        public MachineBuilder department(String department) { this.department = department; return this; }
        public MachineBuilder status(String status) { this.status = status; return this; }
        public MachineBuilder currentOrderId(String currentOrderId) { this.currentOrderId = currentOrderId; return this; }
        public MachineBuilder manufacturer(String manufacturer) { this.manufacturer = manufacturer; return this; }
        public MachineBuilder modelNumber(String modelNumber) { this.modelNumber = modelNumber; return this; }
        public MachineBuilder oeeScorePct(Double oeeScorePct) { this.oeeScorePct = oeeScorePct; return this; }
        public MachineBuilder availabilityPct(Double availabilityPct) { this.availabilityPct = availabilityPct; return this; }
        public MachineBuilder performancePct(Double performancePct) { this.performancePct = performancePct; return this; }
        public MachineBuilder qualityPct(Double qualityPct) { this.qualityPct = qualityPct; return this; }
        public MachineBuilder lastMaintenanceAt(LocalDateTime lastMaintenanceAt) { this.lastMaintenanceAt = lastMaintenanceAt; return this; }
        public MachineBuilder nextMaintenanceAt(LocalDateTime nextMaintenanceAt) { this.nextMaintenanceAt = nextMaintenanceAt; return this; }
        public MachineBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public MachineBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Machine build() {
            return new Machine(id, tenantId, machineCode, name, machineType, department, status, currentOrderId, manufacturer, modelNumber, oeeScorePct, availabilityPct, performancePct, qualityPct, lastMaintenanceAt, nextMaintenanceAt, createdAt, updatedAt);
        }
    }
}
