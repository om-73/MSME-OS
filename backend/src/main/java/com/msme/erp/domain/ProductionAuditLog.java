package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_audit_logs")
public class ProductionAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderId;

    private String stageId;
    private String stageName;

    private String operatorId;
    private String operatorName;

    private String previousState;
    private String newState;

    @Column(length = 1000)
    private String reason;

    private String device;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public ProductionAuditLog() {}

    public ProductionAuditLog(Long id, String tenantId, String orderId, String stageId, String stageName, String operatorId, String operatorName, String previousState, String newState, String reason, String device, LocalDateTime timestamp) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.stageId = stageId;
        this.stageName = stageName;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.previousState = previousState;
        this.newState = newState;
        this.reason = reason;
        this.device = device;
        this.timestamp = timestamp;
    }

    public static ProductionAuditLogBuilder builder() {
        return new ProductionAuditLogBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getStageId() { return stageId; }
    public void setStageId(String stageId) { this.stageId = stageId; }
    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getPreviousState() { return previousState; }
    public void setPreviousState(String previousState) { this.previousState = previousState; }
    public String getNewState() { return newState; }
    public void setNewState(String newState) { this.newState = newState; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class ProductionAuditLogBuilder {
        private Long id;
        private String tenantId;
        private String orderId;
        private String stageId;
        private String stageName;
        private String operatorId;
        private String operatorName;
        private String previousState;
        private String newState;
        private String reason;
        private String device;
        private LocalDateTime timestamp;

        public ProductionAuditLogBuilder id(Long id) { this.id = id; return this; }
        public ProductionAuditLogBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public ProductionAuditLogBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public ProductionAuditLogBuilder stageId(String stageId) { this.stageId = stageId; return this; }
        public ProductionAuditLogBuilder stageName(String stageName) { this.stageName = stageName; return this; }
        public ProductionAuditLogBuilder operatorId(String operatorId) { this.operatorId = operatorId; return this; }
        public ProductionAuditLogBuilder operatorName(String operatorName) { this.operatorName = operatorName; return this; }
        public ProductionAuditLogBuilder previousState(String previousState) { this.previousState = previousState; return this; }
        public ProductionAuditLogBuilder newState(String newState) { this.newState = newState; return this; }
        public ProductionAuditLogBuilder reason(String reason) { this.reason = reason; return this; }
        public ProductionAuditLogBuilder device(String device) { this.device = device; return this; }
        public ProductionAuditLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public ProductionAuditLog build() {
            return new ProductionAuditLog(id, tenantId, orderId, stageId, stageName, operatorId, operatorName, previousState, newState, reason, device, timestamp);
        }
    }
}
