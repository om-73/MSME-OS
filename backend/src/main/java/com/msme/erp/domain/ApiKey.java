package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String keyPrefix; // e.g. mfg_live_9a8b7c

    @Column(nullable = false)
    private String hashedSecret;

    @Column(length = 1000, nullable = false)
    private String scopes; // e.g. orders:read,production:write,inventory:read,webhooks:manage

    private String status = "ACTIVE"; // ACTIVE, REVOKED, EXPIRED

    private LocalDateTime expiresAt;
    private LocalDateTime lastUsedAt;
    private LocalDateTime createdAt;

    public ApiKey() {}

    public ApiKey(Long id, String tenantId, String name, String keyPrefix, String hashedSecret, String scopes, String status, LocalDateTime expiresAt, LocalDateTime lastUsedAt, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.keyPrefix = keyPrefix;
        this.hashedSecret = hashedSecret;
        this.scopes = scopes;
        this.status = status;
        this.expiresAt = expiresAt;
        this.lastUsedAt = lastUsedAt;
        this.createdAt = createdAt;
    }

    public static ApiKeyBuilder builder() {
        return new ApiKeyBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getKeyPrefix() { return keyPrefix; }
    public void setKeyPrefix(String keyPrefix) { this.keyPrefix = keyPrefix; }
    public String getHashedSecret() { return hashedSecret; }
    public void setHashedSecret(String hashedSecret) { this.hashedSecret = hashedSecret; }
    public String getScopes() { return scopes; }
    public void setScopes(String scopes) { this.scopes = scopes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public LocalDateTime getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(LocalDateTime lastUsedAt) { this.lastUsedAt = lastUsedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class ApiKeyBuilder {
        private Long id;
        private String tenantId;
        private String name;
        private String keyPrefix;
        private String hashedSecret;
        private String scopes;
        private String status = "ACTIVE";
        private LocalDateTime expiresAt;
        private LocalDateTime lastUsedAt;
        private LocalDateTime createdAt;

        public ApiKeyBuilder id(Long id) { this.id = id; return this; }
        public ApiKeyBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public ApiKeyBuilder name(String name) { this.name = name; return this; }
        public ApiKeyBuilder keyPrefix(String keyPrefix) { this.keyPrefix = keyPrefix; return this; }
        public ApiKeyBuilder hashedSecret(String hashedSecret) { this.hashedSecret = hashedSecret; return this; }
        public ApiKeyBuilder scopes(String scopes) { this.scopes = scopes; return this; }
        public ApiKeyBuilder status(String status) { this.status = status; return this; }
        public ApiKeyBuilder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public ApiKeyBuilder lastUsedAt(LocalDateTime lastUsedAt) { this.lastUsedAt = lastUsedAt; return this; }
        public ApiKeyBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ApiKey build() {
            return new ApiKey(id, tenantId, name, keyPrefix, hashedSecret, scopes, status, expiresAt, lastUsedAt, createdAt);
        }
    }
}
