package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String inventoryItemId;

    @Column(nullable = false)
    private String movementType; // RECEIVE, ISSUE, RETURN, TRANSFER, ADJUSTMENT, CONSUME, SCRAP

    @Column(nullable = false)
    private Double quantity;

    private String fromWarehouse;
    private String toWarehouse;
    private String orderId;
    private String referenceNumber;
    private String operatorName;
    private String remarks;

    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public InventoryMovement() {}

    public InventoryMovement(String id, String tenantId, String inventoryItemId, String movementType, Double quantity, 
                             String fromWarehouse, String toWarehouse, String orderId, String referenceNumber, 
                             String operatorName, String remarks, LocalDateTime timestamp) {
        this.id = id;
        this.tenantId = tenantId;
        this.inventoryItemId = inventoryItemId;
        this.movementType = movementType;
        this.quantity = quantity;
        this.fromWarehouse = fromWarehouse;
        this.toWarehouse = toWarehouse;
        this.orderId = orderId;
        this.referenceNumber = referenceNumber;
        this.operatorName = operatorName;
        this.remarks = remarks;
        this.timestamp = timestamp;
    }

    public static InventoryMovementBuilder builder() {
        return new InventoryMovementBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getInventoryItemId() { return inventoryItemId; }
    public void setInventoryItemId(String inventoryItemId) { this.inventoryItemId = inventoryItemId; }
    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public String getFromWarehouse() { return fromWarehouse; }
    public void setFromWarehouse(String fromWarehouse) { this.fromWarehouse = fromWarehouse; }
    public String getToWarehouse() { return toWarehouse; }
    public void setToWarehouse(String toWarehouse) { this.toWarehouse = toWarehouse; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class InventoryMovementBuilder {
        private String id;
        private String tenantId;
        private String inventoryItemId;
        private String movementType;
        private Double quantity;
        private String fromWarehouse;
        private String toWarehouse;
        private String orderId;
        private String referenceNumber;
        private String operatorName;
        private String remarks;
        private LocalDateTime timestamp;

        public InventoryMovementBuilder id(String id) { this.id = id; return this; }
        public InventoryMovementBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public InventoryMovementBuilder inventoryItemId(String inventoryItemId) { this.inventoryItemId = inventoryItemId; return this; }
        public InventoryMovementBuilder movementType(String movementType) { this.movementType = movementType; return this; }
        public InventoryMovementBuilder quantity(Double quantity) { this.quantity = quantity; return this; }
        public InventoryMovementBuilder fromWarehouse(String fromWarehouse) { this.fromWarehouse = fromWarehouse; return this; }
        public InventoryMovementBuilder toWarehouse(String toWarehouse) { this.toWarehouse = toWarehouse; return this; }
        public InventoryMovementBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public InventoryMovementBuilder referenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; return this; }
        public InventoryMovementBuilder operatorName(String operatorName) { this.operatorName = operatorName; return this; }
        public InventoryMovementBuilder remarks(String remarks) { this.remarks = remarks; return this; }
        public InventoryMovementBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public InventoryMovement build() {
            return new InventoryMovement(id, tenantId, inventoryItemId, movementType, quantity, fromWarehouse, toWarehouse, orderId, referenceNumber, operatorName, remarks, timestamp);
        }
    }
}
