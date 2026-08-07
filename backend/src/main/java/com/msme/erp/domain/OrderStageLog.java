package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_stage_logs")
public class OrderStageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderId;

    private String stageId;
    private String stageName;
    private String operatorId;
    private String operatorName;
    private String action;
    private String notes;
    private LocalDateTime timestamp;

    public OrderStageLog() {}

    public OrderStageLog(String id, String tenantId, String orderId, String stageId, String stageName, String operatorId, String operatorName, String action, String notes, LocalDateTime timestamp) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.stageId = stageId;
        this.stageName = stageName;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.action = action;
        this.notes = notes;
        this.timestamp = timestamp;
    }

    public static OrderStageLogBuilder builder() { return new OrderStageLogBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }

    public static class OrderStageLogBuilder {
        private String id;
        private String tenantId;
        private String orderId;
        private String stageId;
        private String stageName;
        private String operatorId;
        private String operatorName;
        private String action;
        private String notes;
        private LocalDateTime timestamp;

        public OrderStageLogBuilder id(String id) { this.id = id; return this; }
        public OrderStageLogBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public OrderStageLogBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public OrderStageLogBuilder stageId(String stageId) { this.stageId = stageId; return this; }
        public OrderStageLogBuilder stageName(String stageName) { this.stageName = stageName; return this; }
        public OrderStageLogBuilder operatorId(String operatorId) { this.operatorId = operatorId; return this; }
        public OrderStageLogBuilder operatorName(String operatorName) { this.operatorName = operatorName; return this; }
        public OrderStageLogBuilder action(String action) { this.action = action; return this; }
        public OrderStageLogBuilder notes(String notes) { this.notes = notes; return this; }
        public OrderStageLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public OrderStageLog build() {
            return new OrderStageLog(id, tenantId, orderId, stageId, stageName, operatorId, operatorName, action, notes, timestamp);
        }
    }
}
