package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_recommendations")
public class AIRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String reason;

    private String expectedImpact; // e.g. "Reduces Stitching Queue by 24 hours"
    private String riskLevel = "LOW";
    private Double confidenceScore = 0.88;

    @Column(nullable = false)
    private String approvalStatus = "PENDING"; // PENDING, APPROVED, REJECTED, EXECUTED

    private String approvedBy;
    private LocalDateTime approvedAt;

    private LocalDateTime createdAt;

    public AIRecommendation() {}

    public AIRecommendation(Long id, String tenantId, String title, String reason, String expectedImpact, String riskLevel, Double confidenceScore, String approvalStatus, String approvedBy, LocalDateTime approvedAt, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.title = title;
        this.reason = reason;
        this.expectedImpact = expectedImpact;
        this.riskLevel = riskLevel;
        this.confidenceScore = confidenceScore;
        this.approvalStatus = approvalStatus;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
        this.createdAt = createdAt;
    }

    public static AIRecommendationBuilder builder() {
        return new AIRecommendationBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getExpectedImpact() { return expectedImpact; }
    public void setExpectedImpact(String expectedImpact) { this.expectedImpact = expectedImpact; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class AIRecommendationBuilder {
        private Long id;
        private String tenantId;
        private String title;
        private String reason;
        private String expectedImpact;
        private String riskLevel = "LOW";
        private Double confidenceScore = 0.88;
        private String approvalStatus = "PENDING";
        private String approvedBy;
        private LocalDateTime approvedAt;
        private LocalDateTime createdAt;

        public AIRecommendationBuilder id(Long id) { this.id = id; return this; }
        public AIRecommendationBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public AIRecommendationBuilder title(String title) { this.title = title; return this; }
        public AIRecommendationBuilder reason(String reason) { this.reason = reason; return this; }
        public AIRecommendationBuilder expectedImpact(String expectedImpact) { this.expectedImpact = expectedImpact; return this; }
        public AIRecommendationBuilder riskLevel(String riskLevel) { this.riskLevel = riskLevel; return this; }
        public AIRecommendationBuilder confidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public AIRecommendationBuilder approvalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; return this; }
        public AIRecommendationBuilder approvedBy(String approvedBy) { this.approvedBy = approvedBy; return this; }
        public AIRecommendationBuilder approvedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; return this; }
        public AIRecommendationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AIRecommendation build() {
            return new AIRecommendation(id, tenantId, title, reason, expectedImpact, riskLevel, confidenceScore, approvalStatus, approvedBy, approvedAt, createdAt);
        }
    }
}
