package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_items", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "code"})
})
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    private String sku;
    private String barcode;

    @Column(nullable = false)
    private String category; // RAW_MATERIAL, FINISHED_GOODS, WIP, CLIENT_SUPPLIED, SCRAP, REJECTED

    private String supplierName;
    private String unit;
    private Double purchasePrice;

    @Column(nullable = false)
    private Double currentStock = 0.0;

    @Column(nullable = false)
    private Double reservedStock = 0.0;

    @Column(nullable = false)
    private Double availableStock = 0.0;

    private String warehouseName;
    private String rackLocation;
    private String batchNumber;
    private LocalDateTime expiryDate;

    private Double safetyStock = 0.0;
    private Double minStockAlert = 0.0;
    private Double maxStockAlert = 0.0;

    private String clientBrandId; // Links to brand if client-supplied

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        recalculateAvailableStock();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        recalculateAvailableStock();
    }

    public void recalculateAvailableStock() {
        if (currentStock == null) currentStock = 0.0;
        if (reservedStock == null) reservedStock = 0.0;
        availableStock = currentStock - reservedStock;
    }

    public InventoryItem() {}

    public InventoryItem(String id, String tenantId, String name, String code, String sku, String barcode, 
                         String category, String supplierName, String unit, Double purchasePrice, 
                         Double currentStock, Double reservedStock, Double availableStock, String warehouseName, 
                         String rackLocation, String batchNumber, LocalDateTime expiryDate, Double safetyStock, 
                         Double minStockAlert, Double maxStockAlert, String clientBrandId, LocalDateTime createdAt, 
                         LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.code = code;
        this.sku = sku;
        this.barcode = barcode;
        this.category = category;
        this.supplierName = supplierName;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.currentStock = currentStock;
        this.reservedStock = reservedStock;
        this.availableStock = availableStock;
        this.warehouseName = warehouseName;
        this.rackLocation = rackLocation;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.safetyStock = safetyStock;
        this.minStockAlert = minStockAlert;
        this.maxStockAlert = maxStockAlert;
        this.clientBrandId = clientBrandId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static InventoryItemBuilder builder() {
        return new InventoryItemBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(Double purchasePrice) { this.purchasePrice = purchasePrice; }
    public Double getCurrentStock() { return currentStock; }
    public void setCurrentStock(Double currentStock) { this.currentStock = currentStock; recalculateAvailableStock(); }
    public Double getReservedStock() { return reservedStock; }
    public void setReservedStock(Double reservedStock) { this.reservedStock = reservedStock; recalculateAvailableStock(); }
    public Double getAvailableStock() { return availableStock; }
    public void setAvailableStock(Double availableStock) { this.availableStock = availableStock; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getRackLocation() { return rackLocation; }
    public void setRackLocation(String rackLocation) { this.rackLocation = rackLocation; }
    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    public Double getSafetyStock() { return safetyStock; }
    public void setSafetyStock(Double safetyStock) { this.safetyStock = safetyStock; }
    public Double getMinStockAlert() { return minStockAlert; }
    public void setMinStockAlert(Double minStockAlert) { this.minStockAlert = minStockAlert; }
    public Double getMaxStockAlert() { return maxStockAlert; }
    public void setMaxStockAlert(Double maxStockAlert) { this.maxStockAlert = maxStockAlert; }
    public String getClientBrandId() { return clientBrandId; }
    public void setClientBrandId(String clientBrandId) { this.clientBrandId = clientBrandId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class InventoryItemBuilder {
        private String id;
        private String tenantId;
        private String name;
        private String code;
        private String sku;
        private String barcode;
        private String category;
        private String supplierName;
        private String unit;
        private Double purchasePrice;
        private Double currentStock = 0.0;
        private Double reservedStock = 0.0;
        private Double availableStock = 0.0;
        private String warehouseName;
        private String rackLocation;
        private String batchNumber;
        private LocalDateTime expiryDate;
        private Double safetyStock = 0.0;
        private Double minStockAlert = 0.0;
        private Double maxStockAlert = 0.0;
        private String clientBrandId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public InventoryItemBuilder id(String id) { this.id = id; return this; }
        public InventoryItemBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public InventoryItemBuilder name(String name) { this.name = name; return this; }
        public InventoryItemBuilder code(String code) { this.code = code; return this; }
        public InventoryItemBuilder sku(String sku) { this.sku = sku; return this; }
        public InventoryItemBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public InventoryItemBuilder category(String category) { this.category = category; return this; }
        public InventoryItemBuilder supplierName(String supplierName) { this.supplierName = supplierName; return this; }
        public InventoryItemBuilder unit(String unit) { this.unit = unit; return this; }
        public InventoryItemBuilder purchasePrice(Double purchasePrice) { this.purchasePrice = purchasePrice; return this; }
        public InventoryItemBuilder currentStock(Double currentStock) { this.currentStock = currentStock; return this; }
        public InventoryItemBuilder reservedStock(Double reservedStock) { this.reservedStock = reservedStock; return this; }
        public InventoryItemBuilder availableStock(Double availableStock) { this.availableStock = availableStock; return this; }
        public InventoryItemBuilder warehouseName(String warehouseName) { this.warehouseName = warehouseName; return this; }
        public InventoryItemBuilder rackLocation(String rackLocation) { this.rackLocation = rackLocation; return this; }
        public InventoryItemBuilder batchNumber(String batchNumber) { this.batchNumber = batchNumber; return this; }
        public InventoryItemBuilder expiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; return this; }
        public InventoryItemBuilder safetyStock(Double safetyStock) { this.safetyStock = safetyStock; return this; }
        public InventoryItemBuilder minStockAlert(Double minStockAlert) { this.minStockAlert = minStockAlert; return this; }
        public InventoryItemBuilder maxStockAlert(Double maxStockAlert) { this.maxStockAlert = maxStockAlert; return this; }
        public InventoryItemBuilder clientBrandId(String clientBrandId) { this.clientBrandId = clientBrandId; return this; }
        public InventoryItemBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public InventoryItemBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public InventoryItem build() {
            return new InventoryItem(id, tenantId, name, code, sku, barcode, category, supplierName, unit, purchasePrice, 
                                     currentStock, reservedStock, availableStock, warehouseName, rackLocation, batchNumber, 
                                     expiryDate, safetyStock, minStockAlert, maxStockAlert, clientBrandId, createdAt, updatedAt);
        }
    }
}
