package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_accounts")
public class CustomerAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String clientCode; // e.g. CLI-APEX-01

    @Column(nullable = false)
    private String companyName;

    private String accountManagerEmail;
    private String successManagerEmail;
    private String tier = "ENTERPRISE"; // STARTER, GROWTH, ENTERPRISE

    @Column(nullable = false)
    private String healthStatus = "HEALTHY"; // HEALTHY, AT_RISK, CRITICAL

    private Double onTimeDeliveryPct = 95.5;
    private int openIssuesCount = 1;
    private int slaBreachesCount = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CustomerAccount() {}

    public CustomerAccount(Long id, String tenantId, String clientCode, String companyName, String accountManagerEmail, String successManagerEmail, String tier, String healthStatus, Double onTimeDeliveryPct, int openIssuesCount, int slaBreachesCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.clientCode = clientCode;
        this.companyName = companyName;
        this.accountManagerEmail = accountManagerEmail;
        this.successManagerEmail = successManagerEmail;
        this.tier = tier;
        this.healthStatus = healthStatus;
        this.onTimeDeliveryPct = onTimeDeliveryPct;
        this.openIssuesCount = openIssuesCount;
        this.slaBreachesCount = slaBreachesCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CustomerAccountBuilder builder() {
        return new CustomerAccountBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getAccountManagerEmail() { return accountManagerEmail; }
    public void setAccountManagerEmail(String accountManagerEmail) { this.accountManagerEmail = accountManagerEmail; }
    public String getSuccessManagerEmail() { return successManagerEmail; }
    public void setSuccessManagerEmail(String successManagerEmail) { this.successManagerEmail = successManagerEmail; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public Double getOnTimeDeliveryPct() { return onTimeDeliveryPct; }
    public void setOnTimeDeliveryPct(Double onTimeDeliveryPct) { this.onTimeDeliveryPct = onTimeDeliveryPct; }
    public int getOpenIssuesCount() { return openIssuesCount; }
    public void setOpenIssuesCount(int openIssuesCount) { this.openIssuesCount = openIssuesCount; }
    public int getSlaBreachesCount() { return slaBreachesCount; }
    public void setSlaBreachesCount(int slaBreachesCount) { this.slaBreachesCount = slaBreachesCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static class CustomerAccountBuilder {
        private Long id;
        private String tenantId;
        private String clientCode;
        private String companyName;
        private String accountManagerEmail;
        private String successManagerEmail;
        private String tier = "ENTERPRISE";
        private String healthStatus = "HEALTHY";
        private Double onTimeDeliveryPct = 95.5;
        private int openIssuesCount = 1;
        private int slaBreachesCount = 0;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CustomerAccountBuilder id(Long id) { this.id = id; return this; }
        public CustomerAccountBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public CustomerAccountBuilder clientCode(String clientCode) { this.clientCode = clientCode; return this; }
        public CustomerAccountBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public CustomerAccountBuilder accountManagerEmail(String accountManagerEmail) { this.accountManagerEmail = accountManagerEmail; return this; }
        public CustomerAccountBuilder successManagerEmail(String successManagerEmail) { this.successManagerEmail = successManagerEmail; return this; }
        public CustomerAccountBuilder tier(String tier) { this.tier = tier; return this; }
        public CustomerAccountBuilder healthStatus(String healthStatus) { this.healthStatus = healthStatus; return this; }
        public CustomerAccountBuilder onTimeDeliveryPct(Double onTimeDeliveryPct) { this.onTimeDeliveryPct = onTimeDeliveryPct; return this; }
        public CustomerAccountBuilder openIssuesCount(int openIssuesCount) { this.openIssuesCount = openIssuesCount; return this; }
        public CustomerAccountBuilder slaBreachesCount(int slaBreachesCount) { this.slaBreachesCount = slaBreachesCount; return this; }
        public CustomerAccountBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CustomerAccountBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public CustomerAccount build() {
            return new CustomerAccount(id, tenantId, clientCode, companyName, accountManagerEmail, successManagerEmail, tier, healthStatus, onTimeDeliveryPct, openIssuesCount, slaBreachesCount, createdAt, updatedAt);
        }
    }
}
