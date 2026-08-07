package com.msme.erp.dto;

import com.msme.erp.domain.NotificationCategory;
import java.time.LocalDateTime;

public class NotificationDto {
    private String id;
    private NotificationCategory category;
    private String title;
    private String message;
    private String orderId;
    private String orderNumber;
    private boolean readStatus;
    private LocalDateTime createdAt;

    public NotificationDto() {}

    public NotificationDto(String id, NotificationCategory category, String title, String message, String orderId, String orderNumber, boolean readStatus, LocalDateTime createdAt) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.message = message;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.readStatus = readStatus;
        this.createdAt = createdAt;
    }

    public static NotificationDtoBuilder builder() { return new NotificationDtoBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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

    public static class NotificationDtoBuilder {
        private String id;
        private NotificationCategory category;
        private String title;
        private String message;
        private String orderId;
        private String orderNumber;
        private boolean readStatus;
        private LocalDateTime createdAt;

        public NotificationDtoBuilder id(String id) { this.id = id; return this; }
        public NotificationDtoBuilder category(NotificationCategory category) { this.category = category; return this; }
        public NotificationDtoBuilder title(String title) { this.title = title; return this; }
        public NotificationDtoBuilder message(String message) { this.message = message; return this; }
        public NotificationDtoBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public NotificationDtoBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public NotificationDtoBuilder readStatus(boolean readStatus) { this.readStatus = readStatus; return this; }
        public NotificationDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public NotificationDto build() {
            return new NotificationDto(id, category, title, message, orderId, orderNumber, readStatus, createdAt);
        }
    }
}
