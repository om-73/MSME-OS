package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenants")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String subdomain;

    private String industry;
    private String logoUrl;
    private String subscriptionTier;

    private boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Tenant() {}

    public Tenant(String id, String companyName, String subdomain, String industry, String logoUrl, String subscriptionTier, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.companyName = companyName;
        this.subdomain = subdomain;
        this.industry = industry;
        this.logoUrl = logoUrl;
        this.subscriptionTier = subscriptionTier;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static TenantBuilder builder() {
        return new TenantBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getSubdomain() { return subdomain; }
    public void setSubdomain(String subdomain) { this.subdomain = subdomain; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getSubscriptionTier() { return subscriptionTier; }
    public void setSubscriptionTier(String subscriptionTier) { this.subscriptionTier = subscriptionTier; }

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

    public static class TenantBuilder {
        private String id;
        private String companyName;
        private String subdomain;
        private String industry;
        private String logoUrl;
        private String subscriptionTier;
        private boolean active = true;

        public TenantBuilder id(String id) { this.id = id; return this; }
        public TenantBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public TenantBuilder subdomain(String subdomain) { this.subdomain = subdomain; return this; }
        public TenantBuilder industry(String industry) { this.industry = industry; return this; }
        public TenantBuilder logoUrl(String logoUrl) { this.logoUrl = logoUrl; return this; }
        public TenantBuilder subscriptionTier(String subscriptionTier) { this.subscriptionTier = subscriptionTier; return this; }
        public TenantBuilder active(boolean active) { this.active = active; return this; }

        public Tenant build() {
            return new Tenant(id, companyName, subdomain, industry, logoUrl, subscriptionTier, active, null, null);
        }
    }
}
