package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_invoices")
public class TenantInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String invoiceNumber; // e.g. INV-2026-9011

    @Column(nullable = false)
    private String planName;

    @Column(nullable = false)
    private Double subtotal = 0.0;

    private Double taxAmount = 0.0;
    private Double discountAmount = 0.0;

    @Column(nullable = false)
    private Double totalAmount = 0.0;

    private String currency = "USD";

    @Column(nullable = false)
    private String status; // DRAFT, OPEN, PAID, VOID, UNCOLLECTIBLE

    private String taxId;
    private String companyName;
    private String billingAddress;

    private LocalDateTime invoiceDate;
    private LocalDateTime dueDate;
    private LocalDateTime paidAt;

    public TenantInvoice() {}

    public TenantInvoice(Long id, String tenantId, String invoiceNumber, String planName, Double subtotal, Double taxAmount, Double discountAmount, Double totalAmount, String currency, String status, String taxId, String companyName, String billingAddress, LocalDateTime invoiceDate, LocalDateTime dueDate, LocalDateTime paidAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.invoiceNumber = invoiceNumber;
        this.planName = planName;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.status = status;
        this.taxId = taxId;
        this.companyName = companyName;
        this.billingAddress = billingAddress;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.paidAt = paidAt;
    }

    public static TenantInvoiceBuilder builder() {
        return new TenantInvoiceBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    public Double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Double taxAmount) { this.taxAmount = taxAmount; }
    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    @PrePersist
    protected void onCreate() {
        if (invoiceDate == null) invoiceDate = LocalDateTime.now();
        if (dueDate == null) dueDate = LocalDateTime.now().plusDays(14);
    }

    public static class TenantInvoiceBuilder {
        private Long id;
        private String tenantId;
        private String invoiceNumber;
        private String planName;
        private Double subtotal = 0.0;
        private Double taxAmount = 0.0;
        private Double discountAmount = 0.0;
        private Double totalAmount = 0.0;
        private String currency = "USD";
        private String status = "PAID";
        private String taxId;
        private String companyName;
        private String billingAddress;
        private LocalDateTime invoiceDate;
        private LocalDateTime dueDate;
        private LocalDateTime paidAt;

        public TenantInvoiceBuilder id(Long id) { this.id = id; return this; }
        public TenantInvoiceBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public TenantInvoiceBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public TenantInvoiceBuilder planName(String planName) { this.planName = planName; return this; }
        public TenantInvoiceBuilder subtotal(Double subtotal) { this.subtotal = subtotal; return this; }
        public TenantInvoiceBuilder taxAmount(Double taxAmount) { this.taxAmount = taxAmount; return this; }
        public TenantInvoiceBuilder discountAmount(Double discountAmount) { this.discountAmount = discountAmount; return this; }
        public TenantInvoiceBuilder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public TenantInvoiceBuilder currency(String currency) { this.currency = currency; return this; }
        public TenantInvoiceBuilder status(String status) { this.status = status; return this; }
        public TenantInvoiceBuilder taxId(String taxId) { this.taxId = taxId; return this; }
        public TenantInvoiceBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public TenantInvoiceBuilder billingAddress(String billingAddress) { this.billingAddress = billingAddress; return this; }
        public TenantInvoiceBuilder invoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; return this; }
        public TenantInvoiceBuilder dueDate(LocalDateTime dueDate) { this.dueDate = dueDate; return this; }
        public TenantInvoiceBuilder paidAt(LocalDateTime paidAt) { this.paidAt = paidAt; return this; }

        public TenantInvoice build() {
            return new TenantInvoice(id, tenantId, invoiceNumber, planName, subtotal, taxAmount, discountAmount, totalAmount, currency, status, taxId, companyName, billingAddress, invoiceDate, dueDate, paidAt);
        }
    }
}
