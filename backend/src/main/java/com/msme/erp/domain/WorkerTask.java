package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "worker_tasks")
public class WorkerTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String stageId;

    @Column(nullable = false)
    private String stageName;

    private String assignedWorkerId;
    private String assignedWorkerName;

    @Column(nullable = false)
    private String status; // PENDING, ASSIGNED, ACCEPTED, IN_PROGRESS, PAUSED, BLOCKED, COMPLETED, REWORK_REQUIRED, VERIFIED

    @Column(length = 1000)
    private String remarks;

    private String photoUrl;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long pausedTimeSeconds = 0L;

    public WorkerTask() {}

    public WorkerTask(Long id, String tenantId, String orderId, String orderNumber, String productName, String stageId, String stageName, String assignedWorkerId, String assignedWorkerName, String status, String remarks, String photoUrl, LocalDateTime startTime, LocalDateTime endTime, Long pausedTimeSeconds) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.productName = productName;
        this.stageId = stageId;
        this.stageName = stageName;
        this.assignedWorkerId = assignedWorkerId;
        this.assignedWorkerName = assignedWorkerName;
        this.status = status;
        this.remarks = remarks;
        this.photoUrl = photoUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pausedTimeSeconds = pausedTimeSeconds;
    }

    public static WorkerTaskBuilder builder() {
        return new WorkerTaskBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getStageId() { return stageId; }
    public void setStageId(String stageId) { this.stageId = stageId; }
    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }
    public String getAssignedWorkerId() { return assignedWorkerId; }
    public void setAssignedWorkerId(String assignedWorkerId) { this.assignedWorkerId = assignedWorkerId; }
    public String getAssignedWorkerName() { return assignedWorkerName; }
    public void setAssignedWorkerName(String assignedWorkerName) { this.assignedWorkerName = assignedWorkerName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Long getPausedTimeSeconds() { return pausedTimeSeconds; }
    public void setPausedTimeSeconds(Long pausedTimeSeconds) { this.pausedTimeSeconds = pausedTimeSeconds; }

    public static class WorkerTaskBuilder {
        private Long id;
        private String tenantId;
        private String orderId;
        private String orderNumber;
        private String productName;
        private String stageId;
        private String stageName;
        private String assignedWorkerId;
        private String assignedWorkerName;
        private String status;
        private String remarks;
        private String photoUrl;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Long pausedTimeSeconds = 0L;

        public WorkerTaskBuilder id(Long id) { this.id = id; return this; }
        public WorkerTaskBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public WorkerTaskBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public WorkerTaskBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public WorkerTaskBuilder productName(String productName) { this.productName = productName; return this; }
        public WorkerTaskBuilder stageId(String stageId) { this.stageId = stageId; return this; }
        public WorkerTaskBuilder stageName(String stageName) { this.stageName = stageName; return this; }
        public WorkerTaskBuilder assignedWorkerId(String assignedWorkerId) { this.assignedWorkerId = assignedWorkerId; return this; }
        public WorkerTaskBuilder assignedWorkerName(String assignedWorkerName) { this.assignedWorkerName = assignedWorkerName; return this; }
        public WorkerTaskBuilder status(String status) { this.status = status; return this; }
        public WorkerTaskBuilder remarks(String remarks) { this.remarks = remarks; return this; }
        public WorkerTaskBuilder photoUrl(String photoUrl) { this.photoUrl = photoUrl; return this; }
        public WorkerTaskBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public WorkerTaskBuilder endTime(LocalDateTime endTime) { this.endTime = endTime; return this; }
        public WorkerTaskBuilder pausedTimeSeconds(Long pausedTimeSeconds) { this.pausedTimeSeconds = pausedTimeSeconds; return this; }

        public WorkerTask build() {
            return new WorkerTask(id, tenantId, orderId, orderNumber, productName, stageId, stageName, assignedWorkerId, assignedWorkerName, status, remarks, photoUrl, startTime, endTime, pausedTimeSeconds);
        }
    }
}
