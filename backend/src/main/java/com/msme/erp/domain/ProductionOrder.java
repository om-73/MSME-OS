package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_orders")
public class ProductionOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private String brandId;

    @Column(nullable = false)
    private String productName;

    private Integer quantity;
    private String unit;
    private String priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    private String currentStageId;
    private String currentStageName;
    private Integer currentStageSequence;

    private Double totalContractValue;
    private String paymentStatus;

    private LocalDateTime targetCompletionDate;
    private LocalDateTime estimatedDeliveryEta;
    private LocalDateTime actualDispatchDate;

    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductionOrder() {}

    public ProductionOrder(String id, String tenantId, String orderNumber, String brandId, String productName, Integer quantity, String unit, String priority, OrderStatus status, String currentStageId, String currentStageName, Integer currentStageSequence, Double totalContractValue, String paymentStatus, LocalDateTime targetCompletionDate, LocalDateTime estimatedDeliveryEta, LocalDateTime actualDispatchDate, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderNumber = orderNumber;
        this.brandId = brandId;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.priority = priority;
        if (status != null) this.status = status;
        this.currentStageId = currentStageId;
        this.currentStageName = currentStageName;
        this.currentStageSequence = currentStageSequence;
        this.totalContractValue = totalContractValue;
        this.paymentStatus = paymentStatus;
        this.targetCompletionDate = targetCompletionDate;
        this.estimatedDeliveryEta = estimatedDeliveryEta;
        this.actualDispatchDate = actualDispatchDate;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProductionOrderBuilder builder() { return new ProductionOrderBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getBrandId() { return brandId; }
    public void setBrandId(String brandId) { this.brandId = brandId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getCurrentStageId() { return currentStageId; }
    public void setCurrentStageId(String currentStageId) { this.currentStageId = currentStageId; }
    public String getCurrentStageName() { return currentStageName; }
    public void setCurrentStageName(String currentStageName) { this.currentStageName = currentStageName; }
    public Integer getCurrentStageSequence() { return currentStageSequence; }
    public void setCurrentStageSequence(Integer currentStageSequence) { this.currentStageSequence = currentStageSequence; }
    public Double getTotalContractValue() { return totalContractValue; }
    public void setTotalContractValue(Double totalContractValue) { this.totalContractValue = totalContractValue; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDateTime getTargetCompletionDate() { return targetCompletionDate; }
    public void setTargetCompletionDate(LocalDateTime targetCompletionDate) { this.targetCompletionDate = targetCompletionDate; }
    public LocalDateTime getEstimatedDeliveryEta() { return estimatedDeliveryEta; }
    public void setEstimatedDeliveryEta(LocalDateTime estimatedDeliveryEta) { this.estimatedDeliveryEta = estimatedDeliveryEta; }
    public LocalDateTime getActualDispatchDate() { return actualDispatchDate; }
    public void setActualDispatchDate(LocalDateTime actualDispatchDate) { this.actualDispatchDate = actualDispatchDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static class ProductionOrderBuilder {
        private String id;
        private String tenantId;
        private String orderNumber;
        private String brandId;
        private String productName;
        private Integer quantity;
        private String unit;
        private String priority;
        private OrderStatus status = OrderStatus.PENDING;
        private String currentStageId;
        private String currentStageName;
        private Integer currentStageSequence;
        private Double totalContractValue;
        private String paymentStatus;
        private LocalDateTime targetCompletionDate;
        private LocalDateTime estimatedDeliveryEta;
        private LocalDateTime actualDispatchDate;
        private String notes;

        public ProductionOrderBuilder id(String id) { this.id = id; return this; }
        public ProductionOrderBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public ProductionOrderBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public ProductionOrderBuilder brandId(String brandId) { this.brandId = brandId; return this; }
        public ProductionOrderBuilder productName(String productName) { this.productName = productName; return this; }
        public ProductionOrderBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public ProductionOrderBuilder unit(String unit) { this.unit = unit; return this; }
        public ProductionOrderBuilder priority(String priority) { this.priority = priority; return this; }
        public ProductionOrderBuilder status(OrderStatus status) { this.status = status; return this; }
        public ProductionOrderBuilder currentStageId(String currentStageId) { this.currentStageId = currentStageId; return this; }
        public ProductionOrderBuilder currentStageName(String currentStageName) { this.currentStageName = currentStageName; return this; }
        public ProductionOrderBuilder currentStageSequence(Integer currentStageSequence) { this.currentStageSequence = currentStageSequence; return this; }
        public ProductionOrderBuilder totalContractValue(Double totalContractValue) { this.totalContractValue = totalContractValue; return this; }
        public ProductionOrderBuilder paymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public ProductionOrderBuilder targetCompletionDate(LocalDateTime targetCompletionDate) { this.targetCompletionDate = targetCompletionDate; return this; }
        public ProductionOrderBuilder estimatedDeliveryEta(LocalDateTime estimatedDeliveryEta) { this.estimatedDeliveryEta = estimatedDeliveryEta; return this; }
        public ProductionOrderBuilder actualDispatchDate(LocalDateTime actualDispatchDate) { this.actualDispatchDate = actualDispatchDate; return this; }
        public ProductionOrderBuilder notes(String notes) { this.notes = notes; return this; }

        public ProductionOrder build() {
            return new ProductionOrder(id, tenantId, orderNumber, brandId, productName, quantity, unit, priority, status, currentStageId, currentStageName, currentStageSequence, totalContractValue, paymentStatus, targetCompletionDate, estimatedDeliveryEta, actualDispatchDate, notes, null, null);
        }
    }
}
