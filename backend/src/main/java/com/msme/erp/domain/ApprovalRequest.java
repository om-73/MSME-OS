package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_requests")
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String requestedBy;

    @Column(nullable = false)
    private String requestType; // INVENTORY_ADJUSTMENT, QC_OVERRIDE, REWORK_ACTION, ROLE_ASSIGNMENT

    @Column(length = 1000, nullable = false)
    private String title;

    @Column(length = 2000)
    private String details;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    private String approvedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;

    public ApprovalRequest() {}

    public ApprovalRequest(Long id, String tenantId, String requestedBy, String requestType, String title, String details, String status, String approvedBy, LocalDateTime approvedAt, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.requestedBy = requestedBy;
        this.requestType = requestType;
        this.title = title;
        this.details = details;
        this.status = status;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
        this.createdAt = createdAt;
    }

    public static ApprovalRequestBuilder builder() {
        return new ApprovalRequestBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class ApprovalRequestBuilder {
        private Long id;
        private String tenantId;
        private String requestedBy;
        private String requestType;
        private String title;
        private String details;
        private String status = "PENDING";
        private String approvedBy;
        private LocalDateTime approvedAt;
        private LocalDateTime createdAt;

        public ApprovalRequestBuilder id(Long id) { this.id = id; return this; }
        public ApprovalRequestBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public ApprovalRequestBuilder requestedBy(String requestedBy) { this.requestedBy = requestedBy; return this; }
        public ApprovalRequestBuilder requestType(String requestType) { this.requestType = requestType; return this; }
        public ApprovalRequestBuilder title(String title) { this.title = title; return this; }
        public ApprovalRequestBuilder details(String details) { this.details = details; return this; }
        public ApprovalRequestBuilder status(String status) { this.status = status; return this; }
        public ApprovalRequestBuilder approvedBy(String approvedBy) { this.approvedBy = approvedBy; return this; }
        public ApprovalRequestBuilder approvedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; return this; }
        public ApprovalRequestBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ApprovalRequest build() {
            return new ApprovalRequest(id, tenantId, requestedBy, requestType, title, details, status, approvedBy, approvedAt, createdAt);
        }
    }
}
