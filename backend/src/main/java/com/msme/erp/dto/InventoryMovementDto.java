package com.msme.erp.dto;

import java.time.LocalDateTime;

public class InventoryMovementDto {
    private String id;
    private String inventoryItemId;
    private String inventoryItemName;
    private String inventoryItemCode;
    private String movementType;
    private Double quantity;
    private String fromWarehouse;
    private String toWarehouse;
    private String orderId;
    private String referenceNumber;
    private String operatorName;
    private String remarks;
    private LocalDateTime timestamp;

    public InventoryMovementDto() {}

    public InventoryMovementDto(String id, String inventoryItemId, String inventoryItemName, String inventoryItemCode, 
                                String movementType, Double quantity, String fromWarehouse, String toWarehouse, 
                                String orderId, String referenceNumber, String operatorName, String remarks, 
                                LocalDateTime timestamp) {
        this.id = id;
        this.inventoryItemId = inventoryItemId;
        this.inventoryItemName = inventoryItemName;
        this.inventoryItemCode = inventoryItemCode;
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

    public static InventoryMovementDtoBuilder builder() {
        return new InventoryMovementDtoBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getInventoryItemId() { return inventoryItemId; }
    public void setInventoryItemId(String inventoryItemId) { this.inventoryItemId = inventoryItemId; }
    public String getInventoryItemName() { return inventoryItemName; }
    public void setInventoryItemName(String inventoryItemName) { this.inventoryItemName = inventoryItemName; }
    public String getInventoryItemCode() { return inventoryItemCode; }
    public void setInventoryItemCode(String inventoryItemCode) { this.inventoryItemCode = inventoryItemCode; }
    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public String getFromWarehouse() { return fromWarehouse; }
    public void setFromWarehouse(String fromWarehouse) { this.fromWarehouse = fromWarehouse; }
    public String getToWarehouse() { return toWarehouse; }
    public void setToWarehouse(String toWarehouse) { this.toWarehouse = toWarehouse; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class InventoryMovementDtoBuilder {
        private String id;
        private String inventoryItemId;
        private String inventoryItemName;
        private String inventoryItemCode;
        private String movementType;
        private Double quantity;
        private String fromWarehouse;
        private String toWarehouse;
        private String orderId;
        private String referenceNumber;
        private String operatorName;
        private String remarks;
        private LocalDateTime timestamp;

        public InventoryMovementDtoBuilder id(String id) { this.id = id; return this; }
        public InventoryMovementDtoBuilder inventoryItemId(String inventoryItemId) { this.inventoryItemId = inventoryItemId; return this; }
        public InventoryMovementDtoBuilder inventoryItemName(String inventoryItemName) { this.inventoryItemName = inventoryItemName; return this; }
        public InventoryMovementDtoBuilder inventoryItemCode(String inventoryItemCode) { this.inventoryItemCode = inventoryItemCode; return this; }
        public InventoryMovementDtoBuilder movementType(String movementType) { this.movementType = movementType; return this; }
        public InventoryMovementDtoBuilder quantity(Double quantity) { this.quantity = quantity; return this; }
        public InventoryMovementDtoBuilder fromWarehouse(String fromWarehouse) { this.fromWarehouse = fromWarehouse; return this; }
        public InventoryMovementDtoBuilder toWarehouse(String toWarehouse) { this.toWarehouse = toWarehouse; return this; }
        public InventoryMovementDtoBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public InventoryMovementDtoBuilder referenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; return this; }
        public InventoryMovementDtoBuilder operatorName(String operatorName) { this.operatorName = operatorName; return this; }
        public InventoryMovementDtoBuilder remarks(String remarks) { this.remarks = remarks; return this; }
        public InventoryMovementDtoBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public InventoryMovementDto build() {
            return new InventoryMovementDto(id, inventoryItemId, inventoryItemName, inventoryItemCode, movementType, quantity, 
                                            fromWarehouse, toWarehouse, orderId, referenceNumber, operatorName, remarks, timestamp);
        }
    }
}
