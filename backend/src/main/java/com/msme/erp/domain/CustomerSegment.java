package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_segments")
public class CustomerSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String segmentName; // e.g. Apparel Brands, Footwear, Enterprise, Mid-Market

    private String region; // North America, Europe, APAC, Domestic
    private String tier; // VIP, TIER_1, TIER_2, STANDARD
    
    private Double minOrderVolume;

    public CustomerSegment() {}

    public CustomerSegment(Long id, String tenantId, String segmentName, String region, String tier, Double minOrderVolume) {
        this.id = id;
        this.tenantId = tenantId;
        this.segmentName = segmentName;
        this.region = region;
        this.tier = tier;
        this.minOrderVolume = minOrderVolume;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getSegmentName() { return segmentName; }
    public void setSegmentName(String segmentName) { this.segmentName = segmentName; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }
    public Double getMinOrderVolume() { return minOrderVolume; }
    public void setMinOrderVolume(Double minOrderVolume) { this.minOrderVolume = minOrderVolume; }

    public static class Builder {
        private Long id;
        private String tenantId;
        private String segmentName;
        private String region;
        private String tier;
        private Double minOrderVolume;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder segmentName(String segmentName) { this.segmentName = segmentName; return this; }
        public Builder region(String region) { this.region = region; return this; }
        public Builder tier(String tier) { this.tier = tier; return this; }
        public Builder minOrderVolume(Double minOrderVolume) { this.minOrderVolume = minOrderVolume; return this; }

        public CustomerSegment build() {
            return new CustomerSegment(id, tenantId, segmentName, region, tier, minOrderVolume);
        }
    }
}
