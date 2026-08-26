package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "iot_devices")
public class IoTDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String deviceId; // e.g. DEV-PLC-001

    @Column(nullable = false)
    private String deviceType; // PLC, SENSOR, GATEWAY

    @Column(nullable = false)
    private String protocol; // MQTT, OPC_UA, REST_API, MODBUS

    private String ipAddress;
    private String firmwareVersion = "v1.4.2";
    private String status = "ONLINE"; // ONLINE, OFFLINE, ERROR

    private Long boundMachineId;
    private LocalDateTime lastSeenAt;
    private LocalDateTime createdAt;

    public IoTDevice() {}

    public IoTDevice(Long id, String tenantId, String deviceId, String deviceType, String protocol, String ipAddress, String firmwareVersion, String status, Long boundMachineId, LocalDateTime lastSeenAt, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.protocol = protocol;
        this.ipAddress = ipAddress;
        this.firmwareVersion = firmwareVersion;
        this.status = status;
        this.boundMachineId = boundMachineId;
        this.lastSeenAt = lastSeenAt;
        this.createdAt = createdAt;
    }

    public static IoTDeviceBuilder builder() {
        return new IoTDeviceBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getBoundMachineId() { return boundMachineId; }
    public void setBoundMachineId(Long boundMachineId) { this.boundMachineId = boundMachineId; }
    public LocalDateTime getLastSeenAt() { return lastSeenAt; }
    public void setLastSeenAt(LocalDateTime lastSeenAt) { this.lastSeenAt = lastSeenAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (lastSeenAt == null) lastSeenAt = LocalDateTime.now();
    }

    public static class IoTDeviceBuilder {
        private Long id;
        private String tenantId;
        private String deviceId;
        private String deviceType;
        private String protocol = "MQTT";
        private String ipAddress = "192.168.10.50";
        private String firmwareVersion = "v1.4.2";
        private String status = "ONLINE";
        private Long boundMachineId;
        private LocalDateTime lastSeenAt;
        private LocalDateTime createdAt;

        public IoTDeviceBuilder id(Long id) { this.id = id; return this; }
        public IoTDeviceBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public IoTDeviceBuilder deviceId(String deviceId) { this.deviceId = deviceId; return this; }
        public IoTDeviceBuilder deviceType(String deviceType) { this.deviceType = deviceType; return this; }
        public IoTDeviceBuilder protocol(String protocol) { this.protocol = protocol; return this; }
        public IoTDeviceBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public IoTDeviceBuilder firmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; return this; }
        public IoTDeviceBuilder status(String status) { this.status = status; return this; }
        public IoTDeviceBuilder boundMachineId(Long boundMachineId) { this.boundMachineId = boundMachineId; return this; }
        public IoTDeviceBuilder lastSeenAt(LocalDateTime lastSeenAt) { this.lastSeenAt = lastSeenAt; return this; }
        public IoTDeviceBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public IoTDevice build() {
            return new IoTDevice(id, tenantId, deviceId, deviceType, protocol, ipAddress, firmwareVersion, status, boundMachineId, lastSeenAt, createdAt);
        }
    }
}
