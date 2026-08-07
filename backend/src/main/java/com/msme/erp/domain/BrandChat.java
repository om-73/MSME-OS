package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "brand_chats")
public class BrandChat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String brandId;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false, length = 1000)
    private String message;

    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public BrandChat() {}

    public BrandChat(String id, String tenantId, String brandId, String senderName, String message, LocalDateTime timestamp) {
        this.id = id;
        this.tenantId = tenantId;
        this.brandId = brandId;
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static BrandChatBuilder builder() {
        return new BrandChatBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getBrandId() { return brandId; }
    public void setBrandId(String brandId) { this.brandId = brandId; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class BrandChatBuilder {
        private String id;
        private String tenantId;
        private String brandId;
        private String senderName;
        private String message;
        private LocalDateTime timestamp;

        public BrandChatBuilder id(String id) { this.id = id; return this; }
        public BrandChatBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public BrandChatBuilder brandId(String brandId) { this.brandId = brandId; return this; }
        public BrandChatBuilder senderName(String senderName) { this.senderName = senderName; return this; }
        public BrandChatBuilder message(String message) { this.message = message; return this; }
        public BrandChatBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public BrandChat build() {
            return new BrandChat(id, tenantId, brandId, senderName, message, timestamp);
        }
    }
}
