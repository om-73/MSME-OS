package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_sessions")
public class AdminSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    private String userId;
    
    @Column(nullable = false, unique = true)
    private String sessionToken;
    
    private String ipAddress;
    private String deviceMetadata;
    private LocalDateTime loginTime;
    private LocalDateTime lastActivity;
    private boolean active;

    public AdminSession() {}
    public AdminSession(String tenantId, String userId, String sessionToken, String ipAddress, String deviceMetadata, LocalDateTime loginTime, LocalDateTime lastActivity, boolean active) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.sessionToken = sessionToken;
        this.ipAddress = ipAddress;
        this.deviceMetadata = deviceMetadata;
        this.loginTime = loginTime;
        this.lastActivity = lastActivity;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getDeviceMetadata() { return deviceMetadata; }
    public void setDeviceMetadata(String deviceMetadata) { this.deviceMetadata = deviceMetadata; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }
    public LocalDateTime getLastActivity() { return lastActivity; }
    public void setLastActivity(LocalDateTime lastActivity) { this.lastActivity = lastActivity; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
