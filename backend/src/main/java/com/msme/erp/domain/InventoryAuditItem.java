package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_audit_items")
public class InventoryAuditItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String auditId;

    @Column(nullable = false)
    private String inventoryItemId;

    @Column(nullable = false)
    private Double systemStock;

    @Column(nullable = false)
    private Double physicalStock;

    @Column(nullable = false)
    private Double variance;

    private Boolean reconciled = false;
    private String reconciliationNotes;

    public InventoryAuditItem() {}

    public InventoryAuditItem(String id, String auditId, String inventoryItemId, Double systemStock, 
                              Double physicalStock, Double variance, Boolean reconciled, String reconciliationNotes) {
        this.id = id;
        this.auditId = auditId;
        this.inventoryItemId = inventoryItemId;
        this.systemStock = systemStock;
        this.physicalStock = physicalStock;
        this.variance = variance;
        this.reconciled = reconciled;
        this.reconciliationNotes = reconciliationNotes;
    }

    public static InventoryAuditItemBuilder builder() {
        return new InventoryAuditItemBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAuditId() { return auditId; }
    public void setAuditId(String auditId) { this.auditId = auditId; }
    public String getInventoryItemId() { return inventoryItemId; }
    public void setInventoryItemId(String inventoryItemId) { this.inventoryItemId = inventoryItemId; }
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

    public static class InventoryAuditItemBuilder {
        private String id;
        private String auditId;
        private String inventoryItemId;
        private Double systemStock;
        private Double physicalStock;
        private Double variance;
        private Boolean reconciled = false;
        private String reconciliationNotes;

        public InventoryAuditItemBuilder id(String id) { this.id = id; return this; }
        public InventoryAuditItemBuilder auditId(String auditId) { this.auditId = auditId; return this; }
        public InventoryAuditItemBuilder inventoryItemId(String inventoryItemId) { this.inventoryItemId = inventoryItemId; return this; }
        public InventoryAuditItemBuilder systemStock(Double systemStock) { this.systemStock = systemStock; return this; }
        public InventoryAuditItemBuilder physicalStock(Double physicalStock) { this.physicalStock = physicalStock; return this; }
        public InventoryAuditItemBuilder variance(Double variance) { this.variance = variance; return this; }
        public InventoryAuditItemBuilder reconciled(Boolean reconciled) { this.reconciled = reconciled; return this; }
        public InventoryAuditItemBuilder reconciliationNotes(String reconciliationNotes) { this.reconciliationNotes = reconciliationNotes; return this; }

        public InventoryAuditItem build() {
            return new InventoryAuditItem(id, auditId, inventoryItemId, systemStock, physicalStock, variance, reconciled, reconciliationNotes);
        }
    }
}
