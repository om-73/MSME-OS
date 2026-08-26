package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "machine_telemetry")
public class MachineTelemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private Long machineId;

    private Long productionCount = 0L;
    private Double cycleTimeSeconds = 4.2;
    private Double temperatureCelsius = 68.5;
    private Double powerKw = 12.4;

    private String qualityStatus = "GOOD"; // GOOD, STALE, UNCERTAIN
    private LocalDateTime timestamp;

    public MachineTelemetry() {}

    public MachineTelemetry(Long id, String tenantId, Long machineId, Long productionCount, Double cycleTimeSeconds, Double temperatureCelsius, Double powerKw, String qualityStatus, LocalDateTime timestamp) {
        this.id = id;
        this.tenantId = tenantId;
        this.machineId = machineId;
        this.productionCount = productionCount;
        this.cycleTimeSeconds = cycleTimeSeconds;
        this.temperatureCelsius = temperatureCelsius;
        this.powerKw = powerKw;
        this.qualityStatus = qualityStatus;
        this.timestamp = timestamp;
    }

    public static MachineTelemetryBuilder builder() {
        return new MachineTelemetryBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    public Long getProductionCount() { return productionCount; }
    public void setProductionCount(Long productionCount) { this.productionCount = productionCount; }
    public Double getCycleTimeSeconds() { return cycleTimeSeconds; }
    public void setCycleTimeSeconds(Double cycleTimeSeconds) { this.cycleTimeSeconds = cycleTimeSeconds; }
    public Double getTemperatureCelsius() { return temperatureCelsius; }
    public void setTemperatureCelsius(Double temperatureCelsius) { this.temperatureCelsius = temperatureCelsius; }
    public Double getPowerKw() { return powerKw; }
    public void setPowerKw(Double powerKw) { this.powerKw = powerKw; }
    public String getQualityStatus() { return qualityStatus; }
    public void setQualityStatus(String qualityStatus) { this.qualityStatus = qualityStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }

    public static class MachineTelemetryBuilder {
        private Long id;
        private String tenantId;
        private Long machineId;
        private Long productionCount = 0L;
        private Double cycleTimeSeconds = 4.2;
        private Double temperatureCelsius = 68.5;
        private Double powerKw = 12.4;
        private String qualityStatus = "GOOD";
        private LocalDateTime timestamp;

        public MachineTelemetryBuilder id(Long id) { this.id = id; return this; }
        public MachineTelemetryBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public MachineTelemetryBuilder machineId(Long machineId) { this.machineId = machineId; return this; }
        public MachineTelemetryBuilder productionCount(Long productionCount) { this.productionCount = productionCount; return this; }
        public MachineTelemetryBuilder cycleTimeSeconds(Double cycleTimeSeconds) { this.cycleTimeSeconds = cycleTimeSeconds; return this; }
        public MachineTelemetryBuilder temperatureCelsius(Double temperatureCelsius) { this.temperatureCelsius = temperatureCelsius; return this; }
        public MachineTelemetryBuilder powerKw(Double powerKw) { this.powerKw = powerKw; return this; }
        public MachineTelemetryBuilder qualityStatus(String qualityStatus) { this.qualityStatus = qualityStatus; return this; }
        public MachineTelemetryBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public MachineTelemetry build() {
            return new MachineTelemetry(id, tenantId, machineId, productionCount, cycleTimeSeconds, temperatureCelsius, powerKw, qualityStatus, timestamp);
        }
    }
}
