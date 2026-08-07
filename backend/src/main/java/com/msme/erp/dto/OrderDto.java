package com.msme.erp.dto;

import com.msme.erp.domain.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
    private String id;
    private String orderNumber;
    private String brandId;
    private String brandName;
    private String productName;
    private Integer quantity;
    private String unit;
    private String priority;
    private OrderStatus status;
    private String currentStageId;
    private String currentStageName;
    private Integer currentStageSequence;
    private String currentStageColor;
    private Double totalContractValue;
    private String paymentStatus;
    private LocalDateTime targetCompletionDate;
    private LocalDateTime estimatedDeliveryEta;
    private LocalDateTime actualDispatchDate;
    private String notes;
    private LocalDateTime createdAt;
    private List<OrderStageLogDto> historyLogs;
    private List<QCRecordDto> qcRecords;

    public OrderDto() {}

    public OrderDto(String id, String orderNumber, String brandId, String brandName, String productName, Integer quantity, String unit, String priority, OrderStatus status, String currentStageId, String currentStageName, Integer currentStageSequence, String currentStageColor, Double totalContractValue, String paymentStatus, LocalDateTime targetCompletionDate, LocalDateTime estimatedDeliveryEta, LocalDateTime actualDispatchDate, String notes, LocalDateTime createdAt, List<OrderStageLogDto> historyLogs, List<QCRecordDto> qcRecords) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.brandId = brandId;
        this.brandName = brandName;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.priority = priority;
        this.status = status;
        this.currentStageId = currentStageId;
        this.currentStageName = currentStageName;
        this.currentStageSequence = currentStageSequence;
        this.currentStageColor = currentStageColor;
        this.totalContractValue = totalContractValue;
        this.paymentStatus = paymentStatus;
        this.targetCompletionDate = targetCompletionDate;
        this.estimatedDeliveryEta = estimatedDeliveryEta;
        this.actualDispatchDate = actualDispatchDate;
        this.notes = notes;
        this.createdAt = createdAt;
        this.historyLogs = historyLogs;
        this.qcRecords = qcRecords;
    }

    public static OrderDtoBuilder builder() { return new OrderDtoBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getBrandId() { return brandId; }
    public void setBrandId(String brandId) { this.brandId = brandId; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
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
    public String getCurrentStageColor() { return currentStageColor; }
    public void setCurrentStageColor(String currentStageColor) { this.currentStageColor = currentStageColor; }
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
    public List<OrderStageLogDto> getHistoryLogs() { return historyLogs; }
    public void setHistoryLogs(List<OrderStageLogDto> historyLogs) { this.historyLogs = historyLogs; }
    public List<QCRecordDto> getQcRecords() { return qcRecords; }
    public void setQcRecords(List<QCRecordDto> qcRecords) { this.qcRecords = qcRecords; }

    public static class OrderDtoBuilder {
        private String id;
        private String orderNumber;
        private String brandId;
        private String brandName;
        private String productName;
        private Integer quantity;
        private String unit;
        private String priority;
        private OrderStatus status;
        private String currentStageId;
        private String currentStageName;
        private Integer currentStageSequence;
        private String currentStageColor;
        private Double totalContractValue;
        private String paymentStatus;
        private LocalDateTime targetCompletionDate;
        private LocalDateTime estimatedDeliveryEta;
        private LocalDateTime actualDispatchDate;
        private String notes;
        private LocalDateTime createdAt;
        private List<OrderStageLogDto> historyLogs;
        private List<QCRecordDto> qcRecords;

        public OrderDtoBuilder id(String id) { this.id = id; return this; }
        public OrderDtoBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public OrderDtoBuilder brandId(String brandId) { this.brandId = brandId; return this; }
        public OrderDtoBuilder brandName(String brandName) { this.brandName = brandName; return this; }
        public OrderDtoBuilder productName(String productName) { this.productName = productName; return this; }
        public OrderDtoBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public OrderDtoBuilder unit(String unit) { this.unit = unit; return this; }
        public OrderDtoBuilder priority(String priority) { this.priority = priority; return this; }
        public OrderDtoBuilder status(OrderStatus status) { this.status = status; return this; }
        public OrderDtoBuilder currentStageId(String currentStageId) { this.currentStageId = currentStageId; return this; }
        public OrderDtoBuilder currentStageName(String currentStageName) { this.currentStageName = currentStageName; return this; }
        public OrderDtoBuilder currentStageSequence(Integer currentStageSequence) { this.currentStageSequence = currentStageSequence; return this; }
        public OrderDtoBuilder currentStageColor(String currentStageColor) { this.currentStageColor = currentStageColor; return this; }
        public OrderDtoBuilder totalContractValue(Double totalContractValue) { this.totalContractValue = totalContractValue; return this; }
        public OrderDtoBuilder paymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public OrderDtoBuilder targetCompletionDate(LocalDateTime targetCompletionDate) { this.targetCompletionDate = targetCompletionDate; return this; }
        public OrderDtoBuilder estimatedDeliveryEta(LocalDateTime estimatedDeliveryEta) { this.estimatedDeliveryEta = estimatedDeliveryEta; return this; }
        public OrderDtoBuilder actualDispatchDate(LocalDateTime actualDispatchDate) { this.actualDispatchDate = actualDispatchDate; return this; }
        public OrderDtoBuilder notes(String notes) { this.notes = notes; return this; }
        public OrderDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public OrderDtoBuilder historyLogs(List<OrderStageLogDto> historyLogs) { this.historyLogs = historyLogs; return this; }
        public OrderDtoBuilder qcRecords(List<QCRecordDto> qcRecords) { this.qcRecords = qcRecords; return this; }

        public OrderDto build() {
            return new OrderDto(id, orderNumber, brandId, brandName, productName, quantity, unit, priority, status, currentStageId, currentStageName, currentStageSequence, currentStageColor, totalContractValue, paymentStatus, targetCompletionDate, estimatedDeliveryEta, actualDispatchDate, notes, createdAt, historyLogs, qcRecords);
        }
    }
}
