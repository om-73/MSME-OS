package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_approval_requests")
public class ClientApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String clientCode;

    @Column(nullable = false)
    private Long documentId;

    @Column(nullable = false)
    private String documentVersion = "1.0"; // Version-specific binding!

    @Column(nullable = false)
    private String approvalType; // TECH_PACK, SAMPLE, ARTWORK, PACKAGING

    @Column(length = 1000, nullable = false)
    private String title;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, CHANGES_REQUESTED

    @Column(length = 1000)
    private String clientComments;

    private String approvedByEmail;
    private LocalDateTime decidedAt;
    private LocalDateTime createdAt;

    public ClientApprovalRequest() {}

    public ClientApprovalRequest(Long id, String tenantId, String clientCode, Long documentId, String documentVersion, String approvalType, String title, String status, String clientComments, String approvedByEmail, LocalDateTime decidedAt, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.clientCode = clientCode;
        this.documentId = documentId;
        this.documentVersion = documentVersion;
        this.approvalType = approvalType;
        this.title = title;
        this.status = status;
        this.clientComments = clientComments;
        this.approvedByEmail = approvedByEmail;
        this.decidedAt = decidedAt;
        this.createdAt = createdAt;
    }

    public static ClientApprovalRequestBuilder builder() {
        return new ClientApprovalRequestBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }
    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }
    public String getDocumentVersion() { return documentVersion; }
    public void setDocumentVersion(String documentVersion) { this.documentVersion = documentVersion; }
    public String getApprovalType() { return approvalType; }
    public void setApprovalType(String approvalType) { this.approvalType = approvalType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getClientComments() { return clientComments; }
    public void setClientComments(String clientComments) { this.clientComments = clientComments; }
    public String getApprovedByEmail() { return approvedByEmail; }
    public void setApprovedByEmail(String approvedByEmail) { this.approvedByEmail = approvedByEmail; }
    public LocalDateTime getDecidedAt() { return decidedAt; }
    public void setDecidedAt(LocalDateTime decidedAt) { this.decidedAt = decidedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class ClientApprovalRequestBuilder {
        private Long id;
        private String tenantId;
        private String clientCode;
        private Long documentId;
        private String documentVersion = "1.0";
        private String approvalType;
        private String title;
        private String status = "PENDING";
        private String clientComments;
        private String approvedByEmail;
        private LocalDateTime decidedAt;
        private LocalDateTime createdAt;

        public ClientApprovalRequestBuilder id(Long id) { this.id = id; return this; }
        public ClientApprovalRequestBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public ClientApprovalRequestBuilder clientCode(String clientCode) { this.clientCode = clientCode; return this; }
        public ClientApprovalRequestBuilder documentId(Long documentId) { this.documentId = documentId; return this; }
        public ClientApprovalRequestBuilder documentVersion(String documentVersion) { this.documentVersion = documentVersion; return this; }
        public ClientApprovalRequestBuilder approvalType(String approvalType) { this.approvalType = approvalType; return this; }
        public ClientApprovalRequestBuilder title(String title) { this.title = title; return this; }
        public ClientApprovalRequestBuilder status(String status) { this.status = status; return this; }
        public ClientApprovalRequestBuilder clientComments(String clientComments) { this.clientComments = clientComments; return this; }
        public ClientApprovalRequestBuilder approvedByEmail(String approvedByEmail) { this.approvedByEmail = approvedByEmail; return this; }
        public ClientApprovalRequestBuilder decidedAt(LocalDateTime decidedAt) { this.decidedAt = decidedAt; return this; }
        public ClientApprovalRequestBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ClientApprovalRequest build() {
            return new ClientApprovalRequest(id, tenantId, clientCode, documentId, documentVersion, approvalType, title, status, clientComments, approvedByEmail, decidedAt, createdAt);
        }
    }
}
