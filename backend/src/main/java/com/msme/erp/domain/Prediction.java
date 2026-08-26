package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_predictions")
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderNumber;

    private String originalEta;
    private String predictedEta;

    private double delayProbability = 0.0; // 0.0 to 100.0 %

    @Column(nullable = false)
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL

    private String bottleneckCause;
    private String recommendedMitigation;

    private LocalDateTime createdAt;

    public Prediction() {}

    public Prediction(Long id, String tenantId, String orderNumber, String originalEta, String predictedEta, double delayProbability, String riskLevel, String bottleneckCause, String recommendedMitigation, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderNumber = orderNumber;
        this.originalEta = originalEta;
        this.predictedEta = predictedEta;
        this.delayProbability = delayProbability;
        this.riskLevel = riskLevel;
        this.bottleneckCause = bottleneckCause;
        this.recommendedMitigation = recommendedMitigation;
        this.createdAt = createdAt;
    }

    public static PredictionBuilder builder() {
        return new PredictionBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getOriginalEta() { return originalEta; }
    public void setOriginalEta(String originalEta) { this.originalEta = originalEta; }
    public String getPredictedEta() { return predictedEta; }
    public void setPredictedEta(String predictedEta) { this.predictedEta = predictedEta; }
    public double getDelayProbability() { return delayProbability; }
    public void setDelayProbability(double delayProbability) { this.delayProbability = delayProbability; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getBottleneckCause() { return bottleneckCause; }
    public void setBottleneckCause(String bottleneckCause) { this.bottleneckCause = bottleneckCause; }
    public String getRecommendedMitigation() { return recommendedMitigation; }
    public void setRecommendedMitigation(String recommendedMitigation) { this.recommendedMitigation = recommendedMitigation; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class PredictionBuilder {
        private Long id;
        private String tenantId;
        private String orderNumber;
        private String originalEta;
        private String predictedEta;
        private double delayProbability = 0.0;
        private String riskLevel = "LOW";
        private String bottleneckCause;
        private String recommendedMitigation;
        private LocalDateTime createdAt;

        public PredictionBuilder id(Long id) { this.id = id; return this; }
        public PredictionBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public PredictionBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public PredictionBuilder originalEta(String originalEta) { this.originalEta = originalEta; return this; }
        public PredictionBuilder predictedEta(String predictedEta) { this.predictedEta = predictedEta; return this; }
        public PredictionBuilder delayProbability(double delayProbability) { this.delayProbability = delayProbability; return this; }
        public PredictionBuilder riskLevel(String riskLevel) { this.riskLevel = riskLevel; return this; }
        public PredictionBuilder bottleneckCause(String bottleneckCause) { this.bottleneckCause = bottleneckCause; return this; }
        public PredictionBuilder recommendedMitigation(String recommendedMitigation) { this.recommendedMitigation = recommendedMitigation; return this; }
        public PredictionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Prediction build() {
            return new Prediction(id, tenantId, orderNumber, originalEta, predictedEta, delayProbability, riskLevel, bottleneckCause, recommendedMitigation, createdAt);
        }
    }
}
