package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String poNumber;

    @Column(nullable = false)
    private String vendorName;

    @Column(nullable = false)
    private String status; // PENDING_APPROVAL, APPROVED, PARTIAL_RECEIVED, COMPLETED, REJECTED

    @Column(nullable = false)
    private Double totalAmount = 0.0;

    private String invoiceNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "purchase_order_id")
    private List<PurchaseOrderItem> items = new ArrayList<>();

    public PurchaseOrder() {}

    public PurchaseOrder(Long id, String tenantId, String poNumber, String vendorName, String status, Double totalAmount, String invoiceNumber, LocalDateTime createdAt, LocalDateTime updatedAt, List<PurchaseOrderItem> items) {
        this.id = id;
        this.tenantId = tenantId;
        this.poNumber = poNumber;
        this.vendorName = vendorName;
        this.status = status;
        this.totalAmount = totalAmount;
        this.invoiceNumber = invoiceNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    public static PurchaseOrderBuilder builder() {
        return new PurchaseOrderBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<PurchaseOrderItem> getItems() { return items; }
    public void setItems(List<PurchaseOrderItem> items) { this.items = items; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static class PurchaseOrderBuilder {
        private Long id;
        private String tenantId;
        private String poNumber;
        private String vendorName;
        private String status = "PENDING_APPROVAL";
        private Double totalAmount = 0.0;
        private String invoiceNumber;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<PurchaseOrderItem> items = new ArrayList<>();

        public PurchaseOrderBuilder id(Long id) { this.id = id; return this; }
        public PurchaseOrderBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public PurchaseOrderBuilder poNumber(String poNumber) { this.poNumber = poNumber; return this; }
        public PurchaseOrderBuilder vendorName(String vendorName) { this.vendorName = vendorName; return this; }
        public PurchaseOrderBuilder status(String status) { this.status = status; return this; }
        public PurchaseOrderBuilder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public PurchaseOrderBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public PurchaseOrderBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public PurchaseOrderBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public PurchaseOrderBuilder items(List<PurchaseOrderItem> items) { this.items = items; return this; }

        public PurchaseOrder build() {
            return new PurchaseOrder(id, tenantId, poNumber, vendorName, status, totalAmount, invoiceNumber, createdAt, updatedAt, items);
        }
    }
}
