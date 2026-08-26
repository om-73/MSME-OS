package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId"})
})
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tenantId;

    @Column(nullable = false)
    private String planKey; // STARTER, PROFESSIONAL, ENTERPRISE

    @Column(nullable = false)
    private String status; // TRIAL, ACTIVE, PAST_DUE, GRACE_PERIOD, SUSPENDED, CANCELLED, EXPIRED

    @Column(nullable = false)
    private String billingCycle = "MONTHLY"; // MONTHLY, ANNUAL

    private Double currentPrice = 0.0;
    private String paymentProvider = "STRIPE";
    private String customerId; // Stripe/Razorpay Customer Ref ID
    private String subscriptionId; // Stripe/Razorpay Subscription Ref ID

    private LocalDateTime trialStartDate;
    private LocalDateTime trialEndDate;
    private LocalDateTime currentPeriodStart;
    private LocalDateTime currentPeriodEnd;

    private boolean cancelAtPeriodEnd = false;
    private String cancellationReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Subscription() {}

    public Subscription(Long id, String tenantId, String planKey, String status, String billingCycle, Double currentPrice, String paymentProvider, String customerId, String subscriptionId, LocalDateTime trialStartDate, LocalDateTime trialEndDate, LocalDateTime currentPeriodStart, LocalDateTime currentPeriodEnd, boolean cancelAtPeriodEnd, String cancellationReason, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.planKey = planKey;
        this.status = status;
        this.billingCycle = billingCycle;
        this.currentPrice = currentPrice;
        this.paymentProvider = paymentProvider;
        this.customerId = customerId;
        this.subscriptionId = subscriptionId;
        this.trialStartDate = trialStartDate;
        this.trialEndDate = trialEndDate;
        this.currentPeriodStart = currentPeriodStart;
        this.currentPeriodEnd = currentPeriodEnd;
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
        this.cancellationReason = cancellationReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SubscriptionBuilder builder() {
        return new SubscriptionBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getPlanKey() { return planKey; }
    public void setPlanKey(String planKey) { this.planKey = planKey; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBillingCycle() { return billingCycle; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }
    public Double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }
    public String getPaymentProvider() { return paymentProvider; }
    public void setPaymentProvider(String paymentProvider) { this.paymentProvider = paymentProvider; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(String subscriptionId) { this.subscriptionId = subscriptionId; }
    public LocalDateTime getTrialStartDate() { return trialStartDate; }
    public void setTrialStartDate(LocalDateTime trialStartDate) { this.trialStartDate = trialStartDate; }
    public LocalDateTime getTrialEndDate() { return trialEndDate; }
    public void setTrialEndDate(LocalDateTime trialEndDate) { this.trialEndDate = trialEndDate; }
    public LocalDateTime getCurrentPeriodStart() { return currentPeriodStart; }
    public void setCurrentPeriodStart(LocalDateTime currentPeriodStart) { this.currentPeriodStart = currentPeriodStart; }
    public LocalDateTime getCurrentPeriodEnd() { return currentPeriodEnd; }
    public void setCurrentPeriodEnd(LocalDateTime currentPeriodEnd) { this.currentPeriodEnd = currentPeriodEnd; }
    public boolean isCancelAtPeriodEnd() { return cancelAtPeriodEnd; }
    public void setCancelAtPeriodEnd(boolean cancelAtPeriodEnd) { this.cancelAtPeriodEnd = cancelAtPeriodEnd; }
    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
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

    public static class SubscriptionBuilder {
        private Long id;
        private String tenantId;
        private String planKey = "STARTER";
        private String status = "TRIAL";
        private String billingCycle = "MONTHLY";
        private Double currentPrice = 0.0;
        private String paymentProvider = "STRIPE";
        private String customerId;
        private String subscriptionId;
        private LocalDateTime trialStartDate;
        private LocalDateTime trialEndDate;
        private LocalDateTime currentPeriodStart;
        private LocalDateTime currentPeriodEnd;
        private boolean cancelAtPeriodEnd = false;
        private String cancellationReason;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public SubscriptionBuilder id(Long id) { this.id = id; return this; }
        public SubscriptionBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public SubscriptionBuilder planKey(String planKey) { this.planKey = planKey; return this; }
        public SubscriptionBuilder status(String status) { this.status = status; return this; }
        public SubscriptionBuilder billingCycle(String billingCycle) { this.billingCycle = billingCycle; return this; }
        public SubscriptionBuilder currentPrice(Double currentPrice) { this.currentPrice = currentPrice; return this; }
        public SubscriptionBuilder paymentProvider(String paymentProvider) { this.paymentProvider = paymentProvider; return this; }
        public SubscriptionBuilder customerId(String customerId) { this.customerId = customerId; return this; }
        public SubscriptionBuilder subscriptionId(String subscriptionId) { this.subscriptionId = subscriptionId; return this; }
        public SubscriptionBuilder trialStartDate(LocalDateTime trialStartDate) { this.trialStartDate = trialStartDate; return this; }
        public SubscriptionBuilder trialEndDate(LocalDateTime trialEndDate) { this.trialEndDate = trialEndDate; return this; }
        public SubscriptionBuilder currentPeriodStart(LocalDateTime currentPeriodStart) { this.currentPeriodStart = currentPeriodStart; return this; }
        public SubscriptionBuilder currentPeriodEnd(LocalDateTime currentPeriodEnd) { this.currentPeriodEnd = currentPeriodEnd; return this; }
        public SubscriptionBuilder cancelAtPeriodEnd(boolean cancelAtPeriodEnd) { this.cancelAtPeriodEnd = cancelAtPeriodEnd; return this; }
        public SubscriptionBuilder cancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; return this; }
        public SubscriptionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SubscriptionBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Subscription build() {
            return new Subscription(id, tenantId, planKey, status, billingCycle, currentPrice, paymentProvider, customerId, subscriptionId, trialStartDate, trialEndDate, currentPeriodStart, currentPeriodEnd, cancelAtPeriodEnd, cancellationReason, createdAt, updatedAt);
        }
    }
}
