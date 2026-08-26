package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_connections")
public class IntegrationConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String providerKey; // TALLY, SHOPIFY, DHL, QUICKBOOKS, ZOHO

    @Column(nullable = false)
    private String providerType; // ACCOUNTING, LOGISTICS, ECOMMERCE

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status; // HEALTHY, DEGRADED, DISCONNECTED

    private String syncFrequency = "HOURLY"; // EVERY_15_MINS, HOURLY, DAILY, MANUAL
    private LocalDateTime lastSyncAt;

    private int syncSuccessCount = 0;
    private int syncFailureCount = 0;

    private LocalDateTime createdAt;

    public IntegrationConnection() {}

    public IntegrationConnection(Long id, String tenantId, String providerKey, String providerType, String name, String status, String syncFrequency, LocalDateTime lastSyncAt, int syncSuccessCount, int syncFailureCount, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.providerKey = providerKey;
        this.providerType = providerType;
        this.name = name;
        this.status = status;
        this.syncFrequency = syncFrequency;
        this.lastSyncAt = lastSyncAt;
        this.syncSuccessCount = syncSuccessCount;
        this.syncFailureCount = syncFailureCount;
        this.createdAt = createdAt;
    }

    public static IntegrationConnectionBuilder builder() {
        return new IntegrationConnectionBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getProviderKey() { return providerKey; }
    public void setProviderKey(String providerKey) { this.providerKey = providerKey; }
    public String getProviderType() { return providerType; }
    public void setProviderType(String providerType) { this.providerType = providerType; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSyncFrequency() { return syncFrequency; }
    public void setSyncFrequency(String syncFrequency) { this.syncFrequency = syncFrequency; }
    public LocalDateTime getLastSyncAt() { return lastSyncAt; }
    public void setLastSyncAt(LocalDateTime lastSyncAt) { this.lastSyncAt = lastSyncAt; }
    public int getSyncSuccessCount() { return syncSuccessCount; }
    public void setSyncSuccessCount(int syncSuccessCount) { this.syncSuccessCount = syncSuccessCount; }
    public int getSyncFailureCount() { return syncFailureCount; }
    public void setSyncFailureCount(int syncFailureCount) { this.syncFailureCount = syncFailureCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class IntegrationConnectionBuilder {
        private Long id;
        private String tenantId;
        private String providerKey;
        private String providerType;
        private String name;
        private String status = "HEALTHY";
        private String syncFrequency = "HOURLY";
        private LocalDateTime lastSyncAt;
        private int syncSuccessCount = 0;
        private int syncFailureCount = 0;
        private LocalDateTime createdAt;

        public IntegrationConnectionBuilder id(Long id) { this.id = id; return this; }
        public IntegrationConnectionBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public IntegrationConnectionBuilder providerKey(String providerKey) { this.providerKey = providerKey; return this; }
        public IntegrationConnectionBuilder providerType(String providerType) { this.providerType = providerType; return this; }
        public IntegrationConnectionBuilder name(String name) { this.name = name; return this; }
        public IntegrationConnectionBuilder status(String status) { this.status = status; return this; }
        public IntegrationConnectionBuilder syncFrequency(String syncFrequency) { this.syncFrequency = syncFrequency; return this; }
        public IntegrationConnectionBuilder lastSyncAt(LocalDateTime lastSyncAt) { this.lastSyncAt = lastSyncAt; return this; }
        public IntegrationConnectionBuilder syncSuccessCount(int syncSuccessCount) { this.syncSuccessCount = syncSuccessCount; return this; }
        public IntegrationConnectionBuilder syncFailureCount(int syncFailureCount) { this.syncFailureCount = syncFailureCount; return this; }
        public IntegrationConnectionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public IntegrationConnection build() {
            return new IntegrationConnection(id, tenantId, providerKey, providerType, name, status, syncFrequency, lastSyncAt, syncSuccessCount, syncFailureCount, createdAt);
        }
    }
}
