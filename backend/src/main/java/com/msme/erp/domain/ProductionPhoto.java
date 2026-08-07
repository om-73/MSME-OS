package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_photos")
public class ProductionPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String photoUrl;

    private String caption;
    private String stageName;
    private String uploadedBy;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public ProductionPhoto() {}

    public ProductionPhoto(String id, String tenantId, String orderId, String photoUrl, String caption, 
                           String stageName, String uploadedBy, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.photoUrl = photoUrl;
        this.caption = caption;
        this.stageName = stageName;
        this.uploadedBy = uploadedBy;
        this.createdAt = createdAt;
    }

    public static ProductionPhotoBuilder builder() {
        return new ProductionPhotoBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class ProductionPhotoBuilder {
        private String id;
        private String tenantId;
        private String orderId;
        private String photoUrl;
        private String caption;
        private String stageName;
        private String uploadedBy;
        private LocalDateTime createdAt;

        public ProductionPhotoBuilder id(String id) { this.id = id; return this; }
        public ProductionPhotoBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public ProductionPhotoBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public ProductionPhotoBuilder photoUrl(String photoUrl) { this.photoUrl = photoUrl; return this; }
        public ProductionPhotoBuilder caption(String caption) { this.caption = caption; return this; }
        public ProductionPhotoBuilder stageName(String stageName) { this.stageName = stageName; return this; }
        public ProductionPhotoBuilder uploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; return this; }
        public ProductionPhotoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ProductionPhoto build() {
            return new ProductionPhoto(id, tenantId, orderId, photoUrl, caption, stageName, uploadedBy, createdAt);
        }
    }
}
