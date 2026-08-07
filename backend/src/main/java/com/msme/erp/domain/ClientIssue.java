package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_issues")
public class ClientIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String status = "OPEN"; // OPEN, RESOLVED

    private String severity; // HIGH, MEDIUM, LOW
    private String reportedBy;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public ClientIssue() {}

    public ClientIssue(String id, String tenantId, String orderId, String title, String description, String status, 
                       String severity, String reportedBy, LocalDateTime createdAt, LocalDateTime resolvedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.severity = severity;
        this.reportedBy = reportedBy;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
    }

    public static ClientIssueBuilder builder() {
        return new ClientIssueBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public static class ClientIssueBuilder {
        private String id;
        private String tenantId;
        private String orderId;
        private String title;
        private String description;
        private String status = "OPEN";
        private String severity;
        private String reportedBy;
        private LocalDateTime createdAt;
        private LocalDateTime resolvedAt;

        public ClientIssueBuilder id(String id) { this.id = id; return this; }
        public ClientIssueBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public ClientIssueBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public ClientIssueBuilder title(String title) { this.title = title; return this; }
        public ClientIssueBuilder description(String description) { this.description = description; return this; }
        public ClientIssueBuilder status(String status) { this.status = status; return this; }
        public ClientIssueBuilder severity(String severity) { this.severity = severity; return this; }
        public ClientIssueBuilder reportedBy(String reportedBy) { this.reportedBy = reportedBy; return this; }
        public ClientIssueBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ClientIssueBuilder resolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; return this; }

        public ClientIssue build() {
            return new ClientIssue(id, tenantId, orderId, title, description, status, severity, reportedBy, createdAt, resolvedAt);
        }
    }
}
