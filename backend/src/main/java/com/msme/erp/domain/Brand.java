package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    private String contactEmail;
    private String contactPhone;
    private String logoUrl;
    private String portalAccessCode;

    private LocalDateTime createdAt;

    public Brand() {}

    public Brand(String id, String tenantId, String name, String contactEmail, String contactPhone, String logoUrl, String portalAccessCode, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.logoUrl = logoUrl;
        this.portalAccessCode = portalAccessCode;
        this.createdAt = createdAt;
    }

    public static BrandBuilder builder() { return new BrandBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getPortalAccessCode() { return portalAccessCode; }
    public void setPortalAccessCode(String portalAccessCode) { this.portalAccessCode = portalAccessCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public static class BrandBuilder {
        private String id;
        private String tenantId;
        private String name;
        private String contactEmail;
        private String contactPhone;
        private String logoUrl;
        private String portalAccessCode;

        public BrandBuilder id(String id) { this.id = id; return this; }
        public BrandBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public BrandBuilder name(String name) { this.name = name; return this; }
        public BrandBuilder contactEmail(String contactEmail) { this.contactEmail = contactEmail; return this; }
        public BrandBuilder contactPhone(String contactPhone) { this.contactPhone = contactPhone; return this; }
        public BrandBuilder logoUrl(String logoUrl) { this.logoUrl = logoUrl; return this; }
        public BrandBuilder portalAccessCode(String portalAccessCode) { this.portalAccessCode = portalAccessCode; return this; }

        public Brand build() {
            return new Brand(id, tenantId, name, contactEmail, contactPhone, logoUrl, portalAccessCode, null);
        }
    }
}
