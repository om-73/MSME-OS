package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mobile_devices")
public class MobileDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, unique = true)
    private String deviceId; // Unique device hardware fingerprint

    private String deviceOs = "ANDROID"; // ANDROID, IOS
    private String pushToken;
    private String appVersion = "2.4.0";
    private String status = "ACTIVE"; // ACTIVE, REVOKED, UPDATE_REQUIRED

    private LocalDateTime lastActiveAt;
    private LocalDateTime createdAt;

    public MobileDevice() {}

    public MobileDevice(Long id, String tenantId, String userId, String deviceId, String deviceOs, String pushToken, String appVersion, String status, LocalDateTime lastActiveAt, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.deviceId = deviceId;
        this.deviceOs = deviceOs;
        this.pushToken = pushToken;
        this.appVersion = appVersion;
        this.status = status;
        this.lastActiveAt = lastActiveAt;
        this.createdAt = createdAt;
    }

    public static MobileDeviceBuilder builder() {
        return new MobileDeviceBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getDeviceOs() { return deviceOs; }
    public void setDeviceOs(String deviceOs) { this.deviceOs = deviceOs; }
    public String getPushToken() { return pushToken; }
    public void setPushToken(String pushToken) { this.pushToken = pushToken; }
    public String getAppVersion() { return appVersion; }
    public void setAppVersion(String appVersion) { this.appVersion = appVersion; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getLastActiveAt() { return lastActiveAt; }
    public void setLastActiveAt(LocalDateTime lastActiveAt) { this.lastActiveAt = lastActiveAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (lastActiveAt == null) lastActiveAt = LocalDateTime.now();
    }

    public static class MobileDeviceBuilder {
        private Long id;
        private String tenantId;
        private String userId;
        private String deviceId;
        private String deviceOs = "ANDROID";
        private String pushToken;
        private String appVersion = "2.4.0";
        private String status = "ACTIVE";
        private LocalDateTime lastActiveAt;
        private LocalDateTime createdAt;

        public MobileDeviceBuilder id(Long id) { this.id = id; return this; }
        public MobileDeviceBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public MobileDeviceBuilder userId(String userId) { this.userId = userId; return this; }
        public MobileDeviceBuilder deviceId(String deviceId) { this.deviceId = deviceId; return this; }
        public MobileDeviceBuilder deviceOs(String deviceOs) { this.deviceOs = deviceOs; return this; }
        public MobileDeviceBuilder pushToken(String pushToken) { this.pushToken = pushToken; return this; }
        public MobileDeviceBuilder appVersion(String appVersion) { this.appVersion = appVersion; return this; }
        public MobileDeviceBuilder status(String status) { this.status = status; return this; }
        public MobileDeviceBuilder lastActiveAt(LocalDateTime lastActiveAt) { this.lastActiveAt = lastActiveAt; return this; }
        public MobileDeviceBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public MobileDevice build() {
            return new MobileDevice(id, tenantId, userId, deviceId, deviceOs, pushToken, appVersion, status, lastActiveAt, createdAt);
        }
    }
}
