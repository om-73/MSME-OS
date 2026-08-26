package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_accounts")
public class ServiceAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    private String name;
    
    @Column(nullable = false, unique = true)
    private String clientId;
    
    private String hashedClientSecret;
    private String scopes;
    private boolean active;
    private LocalDateTime lastUsed;

    public ServiceAccount() {}
    public ServiceAccount(String tenantId, String name, String clientId, String hashedClientSecret, String scopes, boolean active, LocalDateTime lastUsed) {
        this.tenantId = tenantId;
        this.name = name;
        this.clientId = clientId;
        this.hashedClientSecret = hashedClientSecret;
        this.scopes = scopes;
        this.active = active;
        this.lastUsed = lastUsed;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getHashedClientSecret() { return hashedClientSecret; }
    public void setHashedClientSecret(String hashedClientSecret) { this.hashedClientSecret = hashedClientSecret; }
    public String getScopes() { return scopes; }
    public void setScopes(String scopes) { this.scopes = scopes; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getLastUsed() { return lastUsed; }
    public void setLastUsed(LocalDateTime lastUsed) { this.lastUsed = lastUsed; }
}
