package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_escalations")
public class TicketEscalation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private Long ticketId;

    private int escalationLevel; // 1 = CSM, 2 = Factory Owner / Enterprise Admin
    private String escalatedToEmail;
    private LocalDateTime escalatedAt;
    private String status = "ESCALATED"; // ESCALATED, RESOLVED

    public TicketEscalation() {}

    public TicketEscalation(Long id, String tenantId, Long ticketId, int escalationLevel, String escalatedToEmail, LocalDateTime escalatedAt, String status) {
        this.id = id;
        this.tenantId = tenantId;
        this.ticketId = ticketId;
        this.escalationLevel = escalationLevel;
        this.escalatedToEmail = escalatedToEmail;
        this.escalatedAt = escalatedAt;
        this.status = status;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
    public int getEscalationLevel() { return escalationLevel; }
    public void setEscalationLevel(int escalationLevel) { this.escalationLevel = escalationLevel; }
    public String getEscatedToEmail() { return escalatedToEmail; }
    public void setEscatedToEmail(String escalatedToEmail) { this.escalatedToEmail = escalatedToEmail; }
    public LocalDateTime getEscalatedAt() { return escalatedAt; }
    public void setEscalatedAt(LocalDateTime escalatedAt) { this.escalatedAt = escalatedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static class Builder {
        private Long id;
        private String tenantId;
        private Long ticketId;
        private int escalationLevel;
        private String escalatedToEmail;
        private LocalDateTime escalatedAt;
        private String status = "ESCALATED";

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder ticketId(Long ticketId) { this.ticketId = ticketId; return this; }
        public Builder escalationLevel(int escalationLevel) { this.escalationLevel = escalationLevel; return this; }
        public Builder escalatedToEmail(String escalatedToEmail) { this.escalatedToEmail = escalatedToEmail; return this; }
        public Builder escalatedAt(LocalDateTime escalatedAt) { this.escalatedAt = escalatedAt; return this; }
        public Builder status(String status) { this.status = status; return this; }

        public TicketEscalation build() {
            return new TicketEscalation(id, tenantId, ticketId, escalationLevel, escalatedToEmail, escalatedAt, status);
        }
    }
}
