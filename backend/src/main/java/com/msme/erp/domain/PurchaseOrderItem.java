package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "purchase_order_items")
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String materialId;

    @Column(nullable = false)
    private String materialName;

    @Column(nullable = false)
    private Double quantityOrdered = 0.0;

    @Column(nullable = false)
    private Double quantityReceived = 0.0;

    @Column(nullable = false)
    private Double unitPrice = 0.0;

    public PurchaseOrderItem() {}

    public PurchaseOrderItem(Long id, String materialId, String materialName, Double quantityOrdered, Double quantityReceived, Double unitPrice) {
        this.id = id;
        this.materialId = materialId;
        this.materialName = materialName;
        this.quantityOrdered = quantityOrdered;
        this.quantityReceived = quantityReceived;
        this.unitPrice = unitPrice;
    }

    public static PurchaseOrderItemBuilder builder() {
        return new PurchaseOrderItemBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaterialId() { return materialId; }
    public void setMaterialId(String materialId) { this.materialId = materialId; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public Double getQuantityOrdered() { return quantityOrdered; }
    public void setQuantityOrdered(Double quantityOrdered) { this.quantityOrdered = quantityOrdered; }
    public Double getQuantityReceived() { return quantityReceived; }
    public void setQuantityReceived(Double quantityReceived) { this.quantityReceived = quantityReceived; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

    public static class PurchaseOrderItemBuilder {
        private Long id;
        private String materialId;
        private String materialName;
        private Double quantityOrdered = 0.0;
        private Double quantityReceived = 0.0;
        private Double unitPrice = 0.0;

        public PurchaseOrderItemBuilder id(Long id) { this.id = id; return this; }
        public PurchaseOrderItemBuilder materialId(String materialId) { this.materialId = materialId; return this; }
        public PurchaseOrderItemBuilder materialName(String materialName) { this.materialName = materialName; return this; }
        public PurchaseOrderItemBuilder quantityOrdered(Double quantityOrdered) { this.quantityOrdered = quantityOrdered; return this; }
        public PurchaseOrderItemBuilder quantityReceived(Double quantityReceived) { this.quantityReceived = quantityReceived; return this; }
        public PurchaseOrderItemBuilder unitPrice(Double unitPrice) { this.unitPrice = unitPrice; return this; }

        public PurchaseOrderItem build() {
            return new PurchaseOrderItem(id, materialId, materialName, quantityOrdered, quantityReceived, unitPrice);
        }
    }
}
