package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_slas")
public class TicketSLA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String priority; // CRITICAL, HIGH, MEDIUM, LOW

    private int responseTimeHours;
    private int resolutionTimeHours;

    public TicketSLA() {}

    public TicketSLA(Long id, String tenantId, String priority, int responseTimeHours, int resolutionTimeHours) {
        this.id = id;
        this.tenantId = tenantId;
        this.priority = priority;
        this.responseTimeHours = responseTimeHours;
        this.resolutionTimeHours = resolutionTimeHours;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public int getResponseTimeHours() { return responseTimeHours; }
    public void setResponseTimeHours(int responseTimeHours) { this.responseTimeHours = responseTimeHours; }
    public int getResolutionTimeHours() { return resolutionTimeHours; }
    public void setResolutionTimeHours(int resolutionTimeHours) { this.resolutionTimeHours = resolutionTimeHours; }

    public static class Builder {
        private Long id;
        private String tenantId;
        private String priority;
        private int responseTimeHours;
        private int resolutionTimeHours;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder priority(String priority) { this.priority = priority; return this; }
        public Builder responseTimeHours(int responseTimeHours) { this.responseTimeHours = responseTimeHours; return this; }
        public Builder resolutionTimeHours(int resolutionTimeHours) { this.resolutionTimeHours = resolutionTimeHours; return this; }

        public TicketSLA build() {
            return new TicketSLA(id, tenantId, priority, responseTimeHours, resolutionTimeHours);
        }
    }
}
