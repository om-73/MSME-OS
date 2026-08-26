package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String severity; // SEV_1, SEV_2, SEV_3, SEV_4
    private String affectedService;
    private String status; // DETECTED, ACKNOWLEDGED, INVESTIGATING, MITIGATING, RESOLVED, POSTMORTEM
    private String ownerEmail;
    private String description;
    private LocalDateTime detectedAt;
    private LocalDateTime resolvedAt;

    public Incident() {}
    public Incident(String severity, String affectedService, String status, String ownerEmail, String description, LocalDateTime detectedAt, LocalDateTime resolvedAt) {
        this.severity = severity;
        this.affectedService = affectedService;
        this.status = status;
        this.ownerEmail = ownerEmail;
        this.description = description;
        this.detectedAt = detectedAt;
        this.resolvedAt = resolvedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getAffectedService() { return affectedService; }
    public void setAffectedService(String affectedService) { this.affectedService = affectedService; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
