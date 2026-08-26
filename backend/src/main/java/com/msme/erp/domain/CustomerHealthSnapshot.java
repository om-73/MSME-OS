package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_health_snapshots")
public class CustomerHealthSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String clientCode;

    private String healthStatus; // HEALTHY, AT_RISK, CRITICAL
    private Double onTimeDeliveryPct;
    private int openIssuesCount;
    private int slaBreachesCount;

    @Column(length = 2000)
    private String explanation;
    
    private LocalDateTime recordedAt;

    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
    }

    public CustomerHealthSnapshot() {}

    public CustomerHealthSnapshot(Long id, String tenantId, String clientCode, String healthStatus, Double onTimeDeliveryPct, int openIssuesCount, int slaBreachesCount, String explanation) {
        this.id = id;
        this.tenantId = tenantId;
        this.clientCode = clientCode;
        this.healthStatus = healthStatus;
        this.onTimeDeliveryPct = onTimeDeliveryPct;
        this.openIssuesCount = openIssuesCount;
        this.slaBreachesCount = slaBreachesCount;
        this.explanation = explanation;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public Double getOnTimeDeliveryPct() { return onTimeDeliveryPct; }
    public void setOnTimeDeliveryPct(Double onTimeDeliveryPct) { this.onTimeDeliveryPct = onTimeDeliveryPct; }
    public int getOpenIssuesCount() { return openIssuesCount; }
    public void setOpenIssuesCount(int openIssuesCount) { this.openIssuesCount = openIssuesCount; }
    public int getSlaBreachesCount() { return slaBreachesCount; }
    public void setSlaBreachesCount(int slaBreachesCount) { this.slaBreachesCount = slaBreachesCount; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public LocalDateTime getRecordedAt() { return recordedAt; }

    public static class Builder {
        private Long id;
        private String tenantId;
        private String clientCode;
        private String healthStatus;
        private Double onTimeDeliveryPct;
        private int openIssuesCount;
        private int slaBreachesCount;
        private String explanation;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder clientCode(String clientCode) { this.clientCode = clientCode; return this; }
        public Builder healthStatus(String healthStatus) { this.healthStatus = healthStatus; return this; }
        public Builder onTimeDeliveryPct(Double onTimeDeliveryPct) { this.onTimeDeliveryPct = onTimeDeliveryPct; return this; }
        public Builder openIssuesCount(int openIssuesCount) { this.openIssuesCount = openIssuesCount; return this; }
        public Builder slaBreachesCount(int slaBreachesCount) { this.slaBreachesCount = slaBreachesCount; return this; }
        public Builder explanation(String explanation) { this.explanation = explanation; return this; }

        public CustomerHealthSnapshot build() {
            return new CustomerHealthSnapshot(id, tenantId, clientCode, healthStatus, onTimeDeliveryPct, openIssuesCount, slaBreachesCount, explanation);
        }
    }
}
