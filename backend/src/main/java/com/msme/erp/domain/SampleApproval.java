package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sample_approvals")
public class SampleApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String sampleName;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    private String comments;
    private LocalDateTime submittedAt;
    private LocalDateTime respondedAt;

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }

    public SampleApproval() {}

    public SampleApproval(String id, String tenantId, String orderId, String sampleName, String status, 
                          String comments, LocalDateTime submittedAt, LocalDateTime respondedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.sampleName = sampleName;
        this.status = status;
        this.comments = comments;
        this.submittedAt = submittedAt;
        this.respondedAt = respondedAt;
    }

    public static SampleApprovalBuilder builder() {
        return new SampleApprovalBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getSampleName() { return sampleName; }
    public void setSampleName(String sampleName) { this.sampleName = sampleName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }

    public static class SampleApprovalBuilder {
        private String id;
        private String tenantId;
        private String orderId;
        private String sampleName;
        private String status = "PENDING";
        private String comments;
        private LocalDateTime submittedAt;
        private LocalDateTime respondedAt;

        public SampleApprovalBuilder id(String id) { this.id = id; return this; }
        public SampleApprovalBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public SampleApprovalBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public SampleApprovalBuilder sampleName(String sampleName) { this.sampleName = sampleName; return this; }
        public SampleApprovalBuilder status(String status) { this.status = status; return this; }
        public SampleApprovalBuilder comments(String comments) { this.comments = comments; return this; }
        public SampleApprovalBuilder submittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; return this; }
        public SampleApprovalBuilder respondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; return this; }

        public SampleApproval build() {
            return new SampleApproval(id, tenantId, orderId, sampleName, status, comments, submittedAt, respondedAt);
        }
    }
}
