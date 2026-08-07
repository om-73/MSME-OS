package com.msme.erp.dto;

import com.msme.erp.domain.NotificationCategory;

public class CreateNotificationRequest {
    private NotificationCategory category;
    private String title;
    private String message;
    private String orderId;
    private String orderNumber;

    public CreateNotificationRequest() {}

    public CreateNotificationRequest(NotificationCategory category, String title, String message, String orderId, String orderNumber) {
        this.category = category;
        this.title = title;
        this.message = message;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
    }

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
}
