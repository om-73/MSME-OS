package com.msme.erp.dto;

import java.time.LocalDateTime;

public class CreateOrderRequest {
    private String brandId;
    private String productName;
    private Integer quantity;
    private String unit;
    private String priority;
    private Double totalContractValue;
    private LocalDateTime targetCompletionDate;
    private String notes;

    public CreateOrderRequest() {}

    public CreateOrderRequest(String brandId, String productName, Integer quantity, String unit, String priority, Double totalContractValue, LocalDateTime targetCompletionDate, String notes) {
        this.brandId = brandId;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.priority = priority;
        this.totalContractValue = totalContractValue;
        this.targetCompletionDate = targetCompletionDate;
        this.notes = notes;
    }

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
    public Double getTotalContractValue() { return totalContractValue; }
    public void setTotalContractValue(Double totalContractValue) { this.totalContractValue = totalContractValue; }
    public LocalDateTime getTargetCompletionDate() { return targetCompletionDate; }
    public void setTargetCompletionDate(LocalDateTime targetCompletionDate) { this.targetCompletionDate = targetCompletionDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
