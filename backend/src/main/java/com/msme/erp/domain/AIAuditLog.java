package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_audit_logs")
public class AIAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String actorId;

    @Column(nullable = false)
    private String requestType; // DELAY_PREDICTION, BOTTLENECK_DETECTION, COPILOT_QUERY, RECOMMENDATION_APPROVAL

    private String modelProvider = "STATISTICAL_ML_ENGINE_V2";
    private Double confidenceScore = 0.92;

    @Column(length = 2000)
    private String promptOrQuery;

    @Column(length = 2000)
    private String responseSummary;

    private LocalDateTime timestamp;

    public AIAuditLog() {}

    public AIAuditLog(Long id, String tenantId, String actorId, String requestType, String modelProvider, Double confidenceScore, String promptOrQuery, String responseSummary, LocalDateTime timestamp) {
        this.id = id;
        this.tenantId = tenantId;
        this.actorId = actorId;
        this.requestType = requestType;
        this.modelProvider = modelProvider;
        this.confidenceScore = confidenceScore;
        this.promptOrQuery = promptOrQuery;
        this.responseSummary = responseSummary;
        this.timestamp = timestamp;
    }

    public static AIAuditLogBuilder builder() {
        return new AIAuditLogBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getActorId() { return actorId; }
    public void setActorId(String actorId) { this.actorId = actorId; }
    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public String getModelProvider() { return modelProvider; }
    public void setModelProvider(String modelProvider) { this.modelProvider = modelProvider; }
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getPromptOrQuery() { return promptOrQuery; }
    public void setPromptOrQuery(String promptOrQuery) { this.promptOrQuery = promptOrQuery; }
    public String getResponseSummary() { return responseSummary; }
    public void setResponseSummary(String responseSummary) { this.responseSummary = responseSummary; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public static class AIAuditLogBuilder {
        private Long id;
        private String tenantId;
        private String actorId;
        private String requestType;
        private String modelProvider = "STATISTICAL_ML_ENGINE_V2";
        private Double confidenceScore = 0.92;
        private String promptOrQuery;
        private String responseSummary;
        private LocalDateTime timestamp;

        public AIAuditLogBuilder id(Long id) { this.id = id; return this; }
        public AIAuditLogBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public AIAuditLogBuilder actorId(String actorId) { this.actorId = actorId; return this; }
        public AIAuditLogBuilder requestType(String requestType) { this.requestType = requestType; return this; }
        public AIAuditLogBuilder modelProvider(String modelProvider) { this.modelProvider = modelProvider; return this; }
        public AIAuditLogBuilder confidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public AIAuditLogBuilder promptOrQuery(String promptOrQuery) { this.promptOrQuery = promptOrQuery; return this; }
        public AIAuditLogBuilder responseSummary(String responseSummary) { this.responseSummary = responseSummary; return this; }
        public AIAuditLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public AIAuditLog build() {
            return new AIAuditLog(id, tenantId, actorId, requestType, modelProvider, confidenceScore, promptOrQuery, responseSummary, timestamp);
        }
    }
}
