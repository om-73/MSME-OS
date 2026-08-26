package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "feature_flags")
public class FeatureFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String flagKey;

    private String targetingType; // GLOBAL, TENANT, ROLE
    private String targetingValue;
    private int rolloutPercentage;
    private boolean active;

    public FeatureFlag() {}
    public FeatureFlag(String flagKey, String targetingType, String targetingValue, int rolloutPercentage, boolean active) {
        this.flagKey = flagKey;
        this.targetingType = targetingType;
        this.targetingValue = targetingValue;
        this.rolloutPercentage = rolloutPercentage;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFlagKey() { return flagKey; }
    public void setFlagKey(String flagKey) { this.flagKey = flagKey; }
    public String getTargetingType() { return targetingType; }
    public void setTargetingType(String targetingType) { this.targetingType = targetingType; }
    public String getTargetingValue() { return targetingValue; }
    public void setTargetingValue(String targetingValue) { this.targetingValue = targetingValue; }
    public int getRolloutPercentage() { return rolloutPercentage; }
    public void setRolloutPercentage(int rolloutPercentage) { this.rolloutPercentage = rolloutPercentage; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
