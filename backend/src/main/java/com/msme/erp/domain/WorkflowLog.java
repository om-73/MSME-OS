package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_logs", indexes = {
    @Index(name = "idx_workflow_logs_prod_wf", columnList = "productionWorkflowId"),
    @Index(name = "idx_workflow_logs_operator", columnList = "operatorId"),
    @Index(name = "idx_workflow_logs_dept", columnList = "departmentId")
})
public class WorkflowLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String productionWorkflowId;

    private String sourceStageId;

    @Column(nullable = false)
    private String targetStageId;

    private String operatorId;
    private String operatorName;
    private String departmentId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;
    
    private Long durationSeconds;
    
    private Long delaySeconds;

    private String status = "COMPLETED"; // COMPLETED, FAILED, PENDING_APPROVAL

    private String remarks;

    public WorkflowLog() {}

    public WorkflowLog(String id, String productionWorkflowId, String sourceStageId, String targetStageId, String operatorId, String operatorName, String departmentId, LocalDateTime startTime, LocalDateTime endTime, Long durationSeconds, Long delaySeconds, String status, String remarks) {
        this.id = id;
        this.productionWorkflowId = productionWorkflowId;
        this.sourceStageId = sourceStageId;
        this.targetStageId = targetStageId;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.departmentId = departmentId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationSeconds = durationSeconds;
        this.delaySeconds = delaySeconds;
        this.status = status;
        this.remarks = remarks;
    }

    public static WorkflowLogBuilder builder() { return new WorkflowLogBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductionWorkflowId() { return productionWorkflowId; }
    public void setProductionWorkflowId(String productionWorkflowId) { this.productionWorkflowId = productionWorkflowId; }
    public String getSourceStageId() { return sourceStageId; }
    public void setSourceStageId(String sourceStageId) { this.sourceStageId = sourceStageId; }
    public String getTargetStageId() { return targetStageId; }
    public void setTargetStageId(String targetStageId) { this.targetStageId = targetStageId; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Long durationSeconds) { this.durationSeconds = durationSeconds; }
    public Long getDelaySeconds() { return delaySeconds; }
    public void setDelaySeconds(Long delaySeconds) { this.delaySeconds = delaySeconds; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    @PrePersist
    protected void onCreate() {
        if (startTime == null) startTime = LocalDateTime.now();
    }

    public static class WorkflowLogBuilder {
        private String id;
        private String productionWorkflowId;
        private String sourceStageId;
        private String targetStageId;
        private String operatorId;
        private String operatorName;
        private String departmentId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Long durationSeconds;
        private Long delaySeconds;
        private String status = "COMPLETED";
        private String remarks;

        public WorkflowLogBuilder id(String id) { this.id = id; return this; }
        public WorkflowLogBuilder productionWorkflowId(String productionWorkflowId) { this.productionWorkflowId = productionWorkflowId; return this; }
        public WorkflowLogBuilder sourceStageId(String sourceStageId) { this.sourceStageId = sourceStageId; return this; }
        public WorkflowLogBuilder targetStageId(String targetStageId) { this.targetStageId = targetStageId; return this; }
        public WorkflowLogBuilder operatorId(String operatorId) { this.operatorId = operatorId; return this; }
        public WorkflowLogBuilder operatorName(String operatorName) { this.operatorName = operatorName; return this; }
        public WorkflowLogBuilder departmentId(String departmentId) { this.departmentId = departmentId; return this; }
        public WorkflowLogBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public WorkflowLogBuilder endTime(LocalDateTime endTime) { this.endTime = endTime; return this; }
        public WorkflowLogBuilder durationSeconds(Long durationSeconds) { this.durationSeconds = durationSeconds; return this; }
        public WorkflowLogBuilder delaySeconds(Long delaySeconds) { this.delaySeconds = delaySeconds; return this; }
        public WorkflowLogBuilder status(String status) { this.status = status; return this; }
        public WorkflowLogBuilder remarks(String remarks) { this.remarks = remarks; return this; }

        public WorkflowLog build() {
            return new WorkflowLog(id, productionWorkflowId, sourceStageId, targetStageId, operatorId, operatorName, departmentId, startTime, endTime, durationSeconds, delaySeconds, status, remarks);
        }
    }
}
