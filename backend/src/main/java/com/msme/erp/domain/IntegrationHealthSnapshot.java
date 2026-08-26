package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_health_snapshots")
public class IntegrationHealthSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String integrationName; // WHATSAPP, PAYMENT, CLOUD_STORAGE, AI

    private String status; // CONNECTED, DEGRADED, AUTHENTICATION_ERROR, RATE_LIMITED
    private LocalDateTime lastChecked;

    public IntegrationHealthSnapshot() {}
    public IntegrationHealthSnapshot(String integrationName, String status, LocalDateTime lastChecked) {
        this.integrationName = integrationName;
        this.status = status;
        this.lastChecked = lastChecked;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIntegrationName() { return integrationName; }
    public void setIntegrationName(String integrationName) { this.integrationName = integrationName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getLastChecked() { return lastChecked; }
    public void setLastChecked(LocalDateTime lastChecked) { this.lastChecked = lastChecked; }
}
