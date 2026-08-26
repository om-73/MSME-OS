package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_feedbacks")
public class CustomerFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String clientCode;

    private String category; // DELIVERY, QUALITY, SUPPORT
    private int rating; // 1-5 CSAT rating
    private String comment;
    private int npsScore; // 0-10 NPS rating
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public CustomerFeedback() {}

    public CustomerFeedback(Long id, String tenantId, String clientCode, String category, int rating, String comment, int npsScore) {
        this.id = id;
        this.tenantId = tenantId;
        this.clientCode = clientCode;
        this.category = category;
        this.rating = rating;
        this.comment = comment;
        this.npsScore = npsScore;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public int getNpsScore() { return npsScore; }
    public void setNpsScore(int npsScore) { this.npsScore = npsScore; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static class Builder {
        private Long id;
        private String tenantId;
        private String clientCode;
        private String category;
        private int rating;
        private String comment;
        private int npsScore;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder clientCode(String clientCode) { this.clientCode = clientCode; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder rating(int rating) { this.rating = rating; return this; }
        public Builder comment(String comment) { this.comment = comment; return this; }
        public Builder npsScore(int npsScore) { this.npsScore = npsScore; return this; }

        public CustomerFeedback build() {
            return new CustomerFeedback(id, tenantId, clientCode, category, rating, comment, npsScore);
        }
    }
}
