package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, unique = true)
    private String sessionToken;

    private String deviceName;
    private String browserName;
    private String ipAddress;

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, REVOKED, EXPIRED

    private LocalDateTime lastActiveAt;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public UserSession() {}

    public UserSession(Long id, String tenantId, String userId, String sessionToken, String deviceName, String browserName, String ipAddress, String status, LocalDateTime lastActiveAt, LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.sessionToken = sessionToken;
        this.deviceName = deviceName;
        this.browserName = browserName;
        this.ipAddress = ipAddress;
        this.status = status;
        this.lastActiveAt = lastActiveAt;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public static UserSessionBuilder builder() {
        return new UserSessionBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getBrowserName() { return browserName; }
    public void setBrowserName(String browserName) { this.browserName = browserName; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getLastActiveAt() { return lastActiveAt; }
    public void setLastActiveAt(LocalDateTime lastActiveAt) { this.lastActiveAt = lastActiveAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (lastActiveAt == null) lastActiveAt = LocalDateTime.now();
        if (expiresAt == null) expiresAt = LocalDateTime.now().plusDays(7);
    }

    public static class UserSessionBuilder {
        private Long id;
        private String tenantId;
        private String userId;
        private String sessionToken;
        private String deviceName = "Chrome (macOS)";
        private String browserName = "Chrome";
        private String ipAddress = "192.168.1.100";
        private String status = "ACTIVE";
        private LocalDateTime lastActiveAt;
        private LocalDateTime expiresAt;
        private LocalDateTime createdAt;

        public UserSessionBuilder id(Long id) { this.id = id; return this; }
        public UserSessionBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public UserSessionBuilder userId(String userId) { this.userId = userId; return this; }
        public UserSessionBuilder sessionToken(String sessionToken) { this.sessionToken = sessionToken; return this; }
        public UserSessionBuilder deviceName(String deviceName) { this.deviceName = deviceName; return this; }
        public UserSessionBuilder browserName(String browserName) { this.browserName = browserName; return this; }
        public UserSessionBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public UserSessionBuilder status(String status) { this.status = status; return this; }
        public UserSessionBuilder lastActiveAt(LocalDateTime lastActiveAt) { this.lastActiveAt = lastActiveAt; return this; }
        public UserSessionBuilder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public UserSessionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public UserSession build() {
            return new UserSession(id, tenantId, userId, sessionToken, deviceName, browserName, ipAddress, status, lastActiveAt, expiresAt, createdAt);
        }
    }
}
