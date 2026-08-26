package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_windows")
public class MaintenanceWindow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String affectedServices;
    private String impactDescription;
    private String status; // PLANNED, IN_PROGRESS, COMPLETED

    public MaintenanceWindow() {}
    public MaintenanceWindow(LocalDateTime startTime, LocalDateTime endTime, String affectedServices, String impactDescription, String status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.affectedServices = affectedServices;
        this.impactDescription = impactDescription;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getAffectedServices() { return affectedServices; }
    public void setAffectedServices(String affectedServices) { this.affectedServices = affectedServices; }
    public String getImpactDescription() { return impactDescription; }
    public void setImpactDescription(String impactDescription) { this.impactDescription = impactDescription; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
