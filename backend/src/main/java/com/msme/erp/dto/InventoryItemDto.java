package com.msme.erp.dto;

import java.time.LocalDateTime;

public class InventoryItemDto {
    private String id;
    private String name;
    private String code;
    private String sku;
    private String barcode;
    private String category;
    private String supplierName;
    private String unit;
    private Double purchasePrice;
    private Double currentStock;
    private Double reservedStock;
    private Double availableStock;
    private String warehouseName;
    private String rackLocation;
    private String batchNumber;
    private LocalDateTime expiryDate;
    private Double safetyStock;
    private Double minStockAlert;
    private Double maxStockAlert;
    private String clientBrandId;
    private String clientBrandName;
    private Boolean isLowStock;

    public InventoryItemDto() {}

    public InventoryItemDto(String id, String name, String code, String sku, String barcode, String category, 
                            String supplierName, String unit, Double purchasePrice, Double currentStock, 
                            Double reservedStock, Double availableStock, String warehouseName, String rackLocation, 
                            String batchNumber, LocalDateTime expiryDate, Double safetyStock, Double minStockAlert, 
                            Double maxStockAlert, String clientBrandId, String clientBrandName, Boolean isLowStock) {
        this.id = id;
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
        this.clientBrandName = clientBrandName;
        this.isLowStock = isLowStock;
    }

    public static InventoryItemDtoBuilder builder() {
        return new InventoryItemDtoBuilder();
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public void setCurrentStock(Double currentStock) { this.currentStock = currentStock; }
    public Double getReservedStock() { return reservedStock; }
    public void setReservedStock(Double reservedStock) { this.reservedStock = reservedStock; }
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
    public String getClientBrandName() { return clientBrandName; }
    public void setClientBrandName(String clientBrandName) { this.clientBrandName = clientBrandName; }
    public Boolean getIsLowStock() { return isLowStock; }
    public void setIsLowStock(Boolean isLowStock) { this.isLowStock = isLowStock; }

    public static class InventoryItemDtoBuilder {
        private String id;
        private String name;
        private String code;
        private String sku;
        private String barcode;
        private String category;
        private String supplierName;
        private String unit;
        private Double purchasePrice;
        private Double currentStock;
        private Double reservedStock;
        private Double availableStock;
        private String warehouseName;
        private String rackLocation;
        private String batchNumber;
        private LocalDateTime expiryDate;
        private Double safetyStock;
        private Double minStockAlert;
        private Double maxStockAlert;
        private String clientBrandId;
        private String clientBrandName;
        private Boolean isLowStock;

        public InventoryItemDtoBuilder id(String id) { this.id = id; return this; }
        public InventoryItemDtoBuilder name(String name) { this.name = name; return this; }
        public InventoryItemDtoBuilder code(String code) { this.code = code; return this; }
        public InventoryItemDtoBuilder sku(String sku) { this.sku = sku; return this; }
        public InventoryItemDtoBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public InventoryItemDtoBuilder category(String category) { this.category = category; return this; }
        public InventoryItemDtoBuilder supplierName(String supplierName) { this.supplierName = supplierName; return this; }
        public InventoryItemDtoBuilder unit(String unit) { this.unit = unit; return this; }
        public InventoryItemDtoBuilder purchasePrice(Double purchasePrice) { this.purchasePrice = purchasePrice; return this; }
        public InventoryItemDtoBuilder currentStock(Double currentStock) { this.currentStock = currentStock; return this; }
        public InventoryItemDtoBuilder reservedStock(Double reservedStock) { this.reservedStock = reservedStock; return this; }
        public InventoryItemDtoBuilder availableStock(Double availableStock) { this.availableStock = availableStock; return this; }
        public InventoryItemDtoBuilder warehouseName(String warehouseName) { this.warehouseName = warehouseName; return this; }
        public InventoryItemDtoBuilder rackLocation(String rackLocation) { this.rackLocation = rackLocation; return this; }
        public InventoryItemDtoBuilder batchNumber(String batchNumber) { this.batchNumber = batchNumber; return this; }
        public InventoryItemDtoBuilder expiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; return this; }
        public InventoryItemDtoBuilder safetyStock(Double safetyStock) { this.safetyStock = safetyStock; return this; }
        public InventoryItemDtoBuilder minStockAlert(Double minStockAlert) { this.minStockAlert = minStockAlert; return this; }
        public InventoryItemDtoBuilder maxStockAlert(Double maxStockAlert) { this.maxStockAlert = maxStockAlert; return this; }
        public InventoryItemDtoBuilder clientBrandId(String clientBrandId) { this.clientBrandId = clientBrandId; return this; }
        public InventoryItemDtoBuilder clientBrandName(String clientBrandName) { this.clientBrandName = clientBrandName; return this; }
        public InventoryItemDtoBuilder isLowStock(Boolean isLowStock) { this.isLowStock = isLowStock; return this; }

        public InventoryItemDto build() {
            return new InventoryItemDto(id, name, code, sku, barcode, category, supplierName, unit, purchasePrice, 
                                        currentStock, reservedStock, availableStock, warehouseName, rackLocation, 
                                        batchNumber, expiryDate, safetyStock, minStockAlert, maxStockAlert, 
                                        clientBrandId, clientBrandName, isLowStock);
        }
    }
}
