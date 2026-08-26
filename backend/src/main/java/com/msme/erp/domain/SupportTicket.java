package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "support_tickets")
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String ticketNumber; // e.g. TKT-9011

    @Column(nullable = false)
    private String clientCode;

    private String orderNumber;

    @Column(length = 1000, nullable = false)
    private String subject;

    @Column(nullable = false)
    private String priority = "MEDIUM"; // CRITICAL, HIGH, MEDIUM, LOW

    @Column(nullable = false)
    private String status = "OPEN"; // OPEN, ACKNOWLEDGED, IN_PROGRESS, WAITING_FOR_CLIENT, RESOLVED, CLOSED

    private String assignedToEmail;
    private LocalDateTime responseDueAt;
    private LocalDateTime resolutionDueAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SupportTicket() {}

    public SupportTicket(Long id, String tenantId, String ticketNumber, String clientCode, String orderNumber, String subject, String priority, String status, String assignedToEmail, LocalDateTime responseDueAt, LocalDateTime resolutionDueAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketNumber = ticketNumber;
        this.clientCode = clientCode;
        this.orderNumber = orderNumber;
        this.subject = subject;
        this.priority = priority;
        this.status = status;
        this.assignedToEmail = assignedToEmail;
        this.responseDueAt = responseDueAt;
        this.resolutionDueAt = resolutionDueAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SupportTicketBuilder builder() {
        return new SupportTicketBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }
    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAssignedToEmail() { return assignedToEmail; }
    public void setAssignedToEmail(String assignedToEmail) { this.assignedToEmail = assignedToEmail; }
    public LocalDateTime getResponseDueAt() { return responseDueAt; }
    public void setResponseDueAt(LocalDateTime responseDueAt) { this.responseDueAt = responseDueAt; }
    public LocalDateTime getResolutionDueAt() { return resolutionDueAt; }
    public void setResolutionDueAt(LocalDateTime resolutionDueAt) { this.resolutionDueAt = resolutionDueAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (responseDueAt == null) responseDueAt = LocalDateTime.now().plusHours("CRITICAL".equalsIgnoreCase(priority) ? 1 : 4);
        if (resolutionDueAt == null) resolutionDueAt = LocalDateTime.now().plusHours("CRITICAL".equalsIgnoreCase(priority) ? 4 : 24);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static class SupportTicketBuilder {
        private Long id;
        private String tenantId;
        private String ticketNumber;
        private String clientCode;
        private String orderNumber;
        private String subject;
        private String priority = "MEDIUM";
        private String status = "OPEN";
        private String assignedToEmail;
        private LocalDateTime responseDueAt;
        private LocalDateTime resolutionDueAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public SupportTicketBuilder id(Long id) { this.id = id; return this; }
        public SupportTicketBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public SupportTicketBuilder ticketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; return this; }
        public SupportTicketBuilder clientCode(String clientCode) { this.clientCode = clientCode; return this; }
        public SupportTicketBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public SupportTicketBuilder subject(String subject) { this.subject = subject; return this; }
        public SupportTicketBuilder priority(String priority) { this.priority = priority; return this; }
        public SupportTicketBuilder status(String status) { this.status = status; return this; }
        public SupportTicketBuilder assignedToEmail(String assignedToEmail) { this.assignedToEmail = assignedToEmail; return this; }
        public SupportTicketBuilder responseDueAt(LocalDateTime responseDueAt) { this.responseDueAt = responseDueAt; return this; }
        public SupportTicketBuilder resolutionDueAt(LocalDateTime resolutionDueAt) { this.resolutionDueAt = resolutionDueAt; return this; }
        public SupportTicketBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SupportTicketBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public SupportTicket build() {
            return new SupportTicket(id, tenantId, ticketNumber, clientCode, orderNumber, subject, priority, status, assignedToEmail, responseDueAt, resolutionDueAt, createdAt, updatedAt);
        }
    }
}
