package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dispatch_records")
public class DispatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private String productName;

    private String vehicleNo;
    private String courierName;
    private String trackingNumber;

    @Column(nullable = false)
    private String status; // READY, DISPATCHED, DELIVERED

    private boolean checklistPassed = false;
    private String invoiceNumber;
    private boolean barcodeVerified = false;

    private LocalDateTime deliveryConfirmationTime;
    private LocalDateTime createdAt;

    public DispatchRecord() {}

    public DispatchRecord(Long id, String tenantId, String orderId, String orderNumber, String productName, String vehicleNo, String courierName, String trackingNumber, String status, boolean checklistPassed, String invoiceNumber, boolean barcodeVerified, LocalDateTime deliveryConfirmationTime, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.productName = productName;
        this.vehicleNo = vehicleNo;
        this.courierName = courierName;
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.checklistPassed = checklistPassed;
        this.invoiceNumber = invoiceNumber;
        this.barcodeVerified = barcodeVerified;
        this.deliveryConfirmationTime = deliveryConfirmationTime;
        this.createdAt = createdAt;
    }

    public static DispatchRecordBuilder builder() {
        return new DispatchRecordBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getVehicleNo() { return vehicleNo; }
    public void setVehicleNo(String vehicleNo) { this.vehicleNo = vehicleNo; }
    public String getCourierName() { return courierName; }
    public void setCourierName(String courierName) { this.courierName = courierName; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isChecklistPassed() { return checklistPassed; }
    public void setChecklistPassed(boolean checklistPassed) { this.checklistPassed = checklistPassed; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public boolean isBarcodeVerified() { return barcodeVerified; }
    public void setBarcodeVerified(boolean barcodeVerified) { this.barcodeVerified = barcodeVerified; }
    public LocalDateTime getDeliveryConfirmationTime() { return deliveryConfirmationTime; }
    public void setDeliveryConfirmationTime(LocalDateTime deliveryConfirmationTime) { this.deliveryConfirmationTime = deliveryConfirmationTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class DispatchRecordBuilder {
        private Long id;
        private String tenantId;
        private String orderId;
        private String orderNumber;
        private String productName;
        private String vehicleNo;
        private String courierName;
        private String trackingNumber;
        private String status = "READY";
        private boolean checklistPassed = false;
        private String invoiceNumber;
        private boolean barcodeVerified = false;
        private LocalDateTime deliveryConfirmationTime;
        private LocalDateTime createdAt;

        public DispatchRecordBuilder id(Long id) { this.id = id; return this; }
        public DispatchRecordBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public DispatchRecordBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public DispatchRecordBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public DispatchRecordBuilder productName(String productName) { this.productName = productName; return this; }
        public DispatchRecordBuilder vehicleNo(String vehicleNo) { this.vehicleNo = vehicleNo; return this; }
        public DispatchRecordBuilder courierName(String courierName) { this.courierName = courierName; return this; }
        public DispatchRecordBuilder trackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; return this; }
        public DispatchRecordBuilder status(String status) { this.status = status; return this; }
        public DispatchRecordBuilder checklistPassed(boolean checklistPassed) { this.checklistPassed = checklistPassed; return this; }
        public DispatchRecordBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public DispatchRecordBuilder barcodeVerified(boolean barcodeVerified) { this.barcodeVerified = barcodeVerified; return this; }
        public DispatchRecordBuilder deliveryConfirmationTime(LocalDateTime deliveryConfirmationTime) { this.deliveryConfirmationTime = deliveryConfirmationTime; return this; }
        public DispatchRecordBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public DispatchRecord build() {
            return new DispatchRecord(id, tenantId, orderId, orderNumber, productName, vehicleNo, courierName, trackingNumber, status, checklistPassed, invoiceNumber, barcodeVerified, deliveryConfirmationTime, createdAt);
        }
    }
}
