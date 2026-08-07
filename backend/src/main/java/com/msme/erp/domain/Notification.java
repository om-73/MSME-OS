package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationCategory category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    private String orderId;
    private String orderNumber;

    private boolean readStatus = false;
    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(String id, String tenantId, String userId, NotificationCategory category, String title, String message, String orderId, String orderNumber, boolean readStatus, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.category = category;
        this.title = title;
        this.message = message;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.readStatus = readStatus;
        this.createdAt = createdAt;
    }

    public static NotificationBuilder builder() { return new NotificationBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public NotificationCategory getCategory() { return category; }
    public void setCategory(NotificationCategory category) { this.category = category; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public boolean isReadStatus() { return readStatus; }
    public void setReadStatus(boolean readStatus) { this.readStatus = readStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class NotificationBuilder {
        private String id;
        private String tenantId;
        private String userId;
        private NotificationCategory category;
        private String title;
        private String message;
        private String orderId;
        private String orderNumber;
        private boolean readStatus = false;

        public NotificationBuilder id(String id) { this.id = id; return this; }
        public NotificationBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public NotificationBuilder userId(String userId) { this.userId = userId; return this; }
        public NotificationBuilder category(NotificationCategory category) { this.category = category; return this; }
        public NotificationBuilder title(String title) { this.title = title; return this; }
        public NotificationBuilder message(String message) { this.message = message; return this; }
        public NotificationBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public NotificationBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public NotificationBuilder readStatus(boolean readStatus) { this.readStatus = readStatus; return this; }

        public Notification build() {
            return new Notification(id, tenantId, userId, category, title, message, orderId, orderNumber, readStatus, null);
        }
    }
}
