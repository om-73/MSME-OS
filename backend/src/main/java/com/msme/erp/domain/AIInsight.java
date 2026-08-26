package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_insights")
public class AIInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String category; // PRODUCTION, INVENTORY, QUALITY, DELIVERY, BOTTLENECK

    @Column(nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String summary;

    @Column(nullable = false)
    private String confidence; // HIGH, MEDIUM, LOW

    private Double confidenceScore = 0.85;

    @Column(length = 1000)
    private String supportingData;

    @Column(length = 1000)
    private String recommendedAction;

    private LocalDateTime createdAt;

    public AIInsight() {}

    public AIInsight(Long id, String tenantId, String category, String title, String summary, String confidence, Double confidenceScore, String supportingData, String recommendedAction, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.category = category;
        this.title = title;
        this.summary = summary;
        this.confidence = confidence;
        this.confidenceScore = confidenceScore;
        this.supportingData = supportingData;
        this.recommendedAction = recommendedAction;
        this.createdAt = createdAt;
    }

    public static AIInsightBuilder builder() {
        return new AIInsightBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getConfidence() { return confidence; }
    public void setConfidence(String confidence) { this.confidence = confidence; }
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getSupportingData() { return supportingData; }
    public void setSupportingData(String supportingData) { this.supportingData = supportingData; }
    public String getRecommendedAction() { return recommendedAction; }
    public void setRecommendedAction(String recommendedAction) { this.recommendedAction = recommendedAction; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class AIInsightBuilder {
        private Long id;
        private String tenantId;
        private String category;
        private String title;
        private String summary;
        private String confidence = "HIGH";
        private Double confidenceScore = 0.85;
        private String supportingData;
        private String recommendedAction;
        private LocalDateTime createdAt;

        public AIInsightBuilder id(Long id) { this.id = id; return this; }
        public AIInsightBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public AIInsightBuilder category(String category) { this.category = category; return this; }
        public AIInsightBuilder title(String title) { this.title = title; return this; }
        public AIInsightBuilder summary(String summary) { this.summary = summary; return this; }
        public AIInsightBuilder confidence(String confidence) { this.confidence = confidence; return this; }
        public AIInsightBuilder confidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public AIInsightBuilder supportingData(String supportingData) { this.supportingData = supportingData; return this; }
        public AIInsightBuilder recommendedAction(String recommendedAction) { this.recommendedAction = recommendedAction; return this; }
        public AIInsightBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AIInsight build() {
            return new AIInsight(id, tenantId, category, title, summary, confidence, confidenceScore, supportingData, recommendedAction, createdAt);
        }
    }
}
