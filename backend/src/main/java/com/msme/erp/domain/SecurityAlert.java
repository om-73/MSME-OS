package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "security_alerts")
public class SecurityAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    
    @Column(nullable = false)
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(nullable = false)
    private String alertType; // BRUTE_FORCE, IMPOSSIBLE_TRAVEL, SUSPICIOUS_UPLOAD
    
    private String description;
    private boolean resolved;
    private LocalDateTime createdAt;

    public SecurityAlert() {}
    public SecurityAlert(String tenantId, String severity, String alertType, String description, boolean resolved, LocalDateTime createdAt) {
        this.tenantId = tenantId;
        this.severity = severity;
        this.alertType = alertType;
        this.description = description;
        this.resolved = resolved;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
