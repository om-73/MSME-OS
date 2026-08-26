package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String planKey; // STARTER, PROFESSIONAL, ENTERPRISE

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Double monthlyPrice = 0.0;

    @Column(nullable = false)
    private Double annualPrice = 0.0;

    // Limits
    private int maxUsers = 10;
    private int maxActiveOrders = 100;
    private double maxStorageGb = 10.0;
    private int maxWorkflows = 5;
    private int maxNotificationsPerMonth = 5000;

    // Feature Flags CSV String or Set (e.g. FEATURE_WORKFLOW_BUILDER,FEATURE_INVENTORY,FEATURE_CLIENT_PORTAL)
    @Column(length = 2000)
    private String enabledFeatures;

    private boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SubscriptionPlan() {}

    public SubscriptionPlan(Long id, String planKey, String name, String description, Double monthlyPrice, Double annualPrice, int maxUsers, int maxActiveOrders, double maxStorageGb, int maxWorkflows, int maxNotificationsPerMonth, String enabledFeatures, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.planKey = planKey;
        this.name = name;
        this.description = description;
        this.monthlyPrice = monthlyPrice;
        this.annualPrice = annualPrice;
        this.maxUsers = maxUsers;
        this.maxActiveOrders = maxActiveOrders;
        this.maxStorageGb = maxStorageGb;
        this.maxWorkflows = maxWorkflows;
        this.maxNotificationsPerMonth = maxNotificationsPerMonth;
        this.enabledFeatures = enabledFeatures;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SubscriptionPlanBuilder builder() {
        return new SubscriptionPlanBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPlanKey() { return planKey; }
    public void setPlanKey(String planKey) { this.planKey = planKey; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getMonthlyPrice() { return monthlyPrice; }
    public void setMonthlyPrice(Double monthlyPrice) { this.monthlyPrice = monthlyPrice; }
    public Double getAnnualPrice() { return annualPrice; }
    public void setAnnualPrice(Double annualPrice) { this.annualPrice = annualPrice; }
    public int getMaxUsers() { return maxUsers; }
    public void setMaxUsers(int maxUsers) { this.maxUsers = maxUsers; }
    public int getMaxActiveOrders() { return maxActiveOrders; }
    public void setMaxActiveOrders(int maxActiveOrders) { this.maxActiveOrders = maxActiveOrders; }
    public double getMaxStorageGb() { return maxStorageGb; }
    public void setMaxStorageGb(double maxStorageGb) { this.maxStorageGb = maxStorageGb; }
    public int getMaxWorkflows() { return maxWorkflows; }
    public void setMaxWorkflows(int maxWorkflows) { this.maxWorkflows = maxWorkflows; }
    public int getMaxNotificationsPerMonth() { return maxNotificationsPerMonth; }
    public void setMaxNotificationsPerMonth(int maxNotificationsPerMonth) { this.maxNotificationsPerMonth = maxNotificationsPerMonth; }
    public String getEnabledFeatures() { return enabledFeatures; }
    public void setEnabledFeatures(String enabledFeatures) { this.enabledFeatures = enabledFeatures; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
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

    public static class SubscriptionPlanBuilder {
        private Long id;
        private String planKey;
        private String name;
        private String description;
        private Double monthlyPrice = 0.0;
        private Double annualPrice = 0.0;
        private int maxUsers = 10;
        private int maxActiveOrders = 100;
        private double maxStorageGb = 10.0;
        private int maxWorkflows = 5;
        private int maxNotificationsPerMonth = 5000;
        private String enabledFeatures;
        private boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public SubscriptionPlanBuilder id(Long id) { this.id = id; return this; }
        public SubscriptionPlanBuilder planKey(String planKey) { this.planKey = planKey; return this; }
        public SubscriptionPlanBuilder name(String name) { this.name = name; return this; }
        public SubscriptionPlanBuilder description(String description) { this.description = description; return this; }
        public SubscriptionPlanBuilder monthlyPrice(Double monthlyPrice) { this.monthlyPrice = monthlyPrice; return this; }
        public SubscriptionPlanBuilder annualPrice(Double annualPrice) { this.annualPrice = annualPrice; return this; }
        public SubscriptionPlanBuilder maxUsers(int maxUsers) { this.maxUsers = maxUsers; return this; }
        public SubscriptionPlanBuilder maxActiveOrders(int maxActiveOrders) { this.maxActiveOrders = maxActiveOrders; return this; }
        public SubscriptionPlanBuilder maxStorageGb(double maxStorageGb) { this.maxStorageGb = maxStorageGb; return this; }
        public SubscriptionPlanBuilder maxWorkflows(int maxWorkflows) { this.maxWorkflows = maxWorkflows; return this; }
        public SubscriptionPlanBuilder maxNotificationsPerMonth(int maxNotificationsPerMonth) { this.maxNotificationsPerMonth = maxNotificationsPerMonth; return this; }
        public SubscriptionPlanBuilder enabledFeatures(String enabledFeatures) { this.enabledFeatures = enabledFeatures; return this; }
        public SubscriptionPlanBuilder active(boolean active) { this.active = active; return this; }
        public SubscriptionPlanBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SubscriptionPlanBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public SubscriptionPlan build() {
            return new SubscriptionPlan(id, planKey, name, description, monthlyPrice, annualPrice, maxUsers, maxActiveOrders, maxStorageGb, maxWorkflows, maxNotificationsPerMonth, enabledFeatures, active, createdAt, updatedAt);
        }
    }
}
