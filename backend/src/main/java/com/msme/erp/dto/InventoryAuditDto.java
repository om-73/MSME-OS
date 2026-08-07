package com.msme.erp.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InventoryAuditDto {
    private String id;
    private String auditName;
    private String status;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private List<AuditItemDetail> items;

    public InventoryAuditDto() {}

    public InventoryAuditDto(String id, String auditName, String status, String createdBy, 
                              LocalDateTime createdAt, LocalDateTime completedAt, List<AuditItemDetail> items) {
        this.id = id;
        this.auditName = auditName;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.items = items;
    }

    public static InventoryAuditDtoBuilder builder() {
        return new InventoryAuditDtoBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAuditName() { return auditName; }
    public void setAuditName(String auditName) { this.auditName = auditName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public List<AuditItemDetail> getItems() { return items; }
    public void setItems(List<AuditItemDetail> items) { this.items = items; }

    public static class AuditItemDetail {
        private String id;
        private String inventoryItemId;
        private String inventoryItemName;
        private String inventoryItemCode;
        private Double systemStock;
        private Double physicalStock;
        private Double variance;
        private Boolean reconciled;
        private String reconciliationNotes;

        public AuditItemDetail() {}

        public AuditItemDetail(String id, String inventoryItemId, String inventoryItemName, String inventoryItemCode, 
                               Double systemStock, Double physicalStock, Double variance, Boolean reconciled, 
                               String reconciliationNotes) {
            this.id = id;
            this.inventoryItemId = inventoryItemId;
            this.inventoryItemName = inventoryItemName;
            this.inventoryItemCode = inventoryItemCode;
            this.systemStock = systemStock;
            this.physicalStock = physicalStock;
            this.variance = variance;
            this.reconciled = reconciled;
            this.reconciliationNotes = reconciliationNotes;
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
        public Double getSystemStock() { return systemStock; }
        public void setSystemStock(Double systemStock) { this.systemStock = systemStock; }
        public Double getPhysicalStock() { return physicalStock; }
        public void setPhysicalStock(Double physicalStock) { this.physicalStock = physicalStock; }
        public Double getVariance() { return variance; }
        public void setVariance(Double variance) { this.variance = variance; }
        public Boolean getReconciled() { return reconciled; }
        public void setReconciled(Boolean reconciled) { this.reconciled = reconciled; }
        public String getReconciliationNotes() { return reconciliationNotes; }
        public void setReconciliationNotes(String reconciliationNotes) { this.reconciliationNotes = reconciliationNotes; }
    }

    public static class InventoryAuditDtoBuilder {
        private String id;
        private String auditName;
        private String status;
        private String createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;
        private List<AuditItemDetail> items;

        public InventoryAuditDtoBuilder id(String id) { this.id = id; return this; }
        public InventoryAuditDtoBuilder auditName(String auditName) { this.auditName = auditName; return this; }
        public InventoryAuditDtoBuilder status(String status) { this.status = status; return this; }
        public InventoryAuditDtoBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public InventoryAuditDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public InventoryAuditDtoBuilder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }
        public InventoryAuditDtoBuilder items(List<AuditItemDetail> items) { this.items = items; return this; }

        public InventoryAuditDto build() {
            return new InventoryAuditDto(id, auditName, status, createdBy, createdAt, completedAt, items);
        }
    }
}
