package com.msme.erp.dto;

import com.msme.erp.domain.*;
import java.time.LocalDateTime;
import java.util.List;

public class ClientPortalDto {
    private String orderId;
    private String orderNumber;
    private String productName;
    private Integer quantity;
    private String status;
    private String currentStageName;
    private LocalDateTime estimatedDeliveryEta;
    private String paymentStatus;
    private Double totalContractValue;
    private List<WorkflowLogDto> timeline;
    private List<ClientDocument> documents;
    private List<ProductionPhoto> photos;
    private List<ClientIssue> issues;
    private List<SampleApproval> approvals;

    public ClientPortalDto() {}

    public ClientPortalDto(String orderId, String orderNumber, String productName, Integer quantity, String status, 
                           String currentStageName, LocalDateTime estimatedDeliveryEta, String paymentStatus, 
                           Double totalContractValue, List<WorkflowLogDto> timeline, List<ClientDocument> documents, 
                           List<ProductionPhoto> photos, List<ClientIssue> issues, List<SampleApproval> approvals) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.productName = productName;
        this.quantity = quantity;
        this.status = status;
        this.currentStageName = currentStageName;
        this.estimatedDeliveryEta = estimatedDeliveryEta;
        this.paymentStatus = paymentStatus;
        this.totalContractValue = totalContractValue;
        this.timeline = timeline;
        this.documents = documents;
        this.photos = photos;
        this.issues = issues;
        this.approvals = approvals;
    }

    public static ClientPortalDtoBuilder builder() {
        return new ClientPortalDtoBuilder();
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCurrentStageName() { return currentStageName; }
    public void setCurrentStageName(String currentStageName) { this.currentStageName = currentStageName; }
    public LocalDateTime getEstimatedDeliveryEta() { return estimatedDeliveryEta; }
    public void setEstimatedDeliveryEta(LocalDateTime estimatedDeliveryEta) { this.estimatedDeliveryEta = estimatedDeliveryEta; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public Double getTotalContractValue() { return totalContractValue; }
    public void setTotalContractValue(Double totalContractValue) { this.totalContractValue = totalContractValue; }
    public List<WorkflowLogDto> getTimeline() { return timeline; }
    public void setTimeline(List<WorkflowLogDto> timeline) { this.timeline = timeline; }
    public List<ClientDocument> getDocuments() { return documents; }
    public void setDocuments(List<ClientDocument> documents) { this.documents = documents; }
    public List<ProductionPhoto> getPhotos() { return photos; }
    public void setPhotos(List<ProductionPhoto> photos) { this.photos = photos; }
    public List<ClientIssue> getIssues() { return issues; }
    public void setIssues(List<ClientIssue> issues) { this.issues = issues; }
    public List<SampleApproval> getApprovals() { return approvals; }
    public void setApprovals(List<SampleApproval> approvals) { this.approvals = approvals; }

    public static class ClientPortalDtoBuilder {
        private String orderId;
        private String orderNumber;
        private String productName;
        private Integer quantity;
        private String status;
        private String currentStageName;
        private LocalDateTime militaryDeliveryEta;
        private String paymentStatus;
        private Double totalContractValue;
        private List<WorkflowLogDto> timeline;
        private List<ClientDocument> documents;
        private List<ProductionPhoto> photos;
        private List<ClientIssue> issues;
        private List<SampleApproval> approvals;

        public ClientPortalDtoBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public ClientPortalDtoBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public ClientPortalDtoBuilder productName(String productName) { this.productName = productName; return this; }
        public ClientPortalDtoBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public ClientPortalDtoBuilder status(String status) { this.status = status; return this; }
        public ClientPortalDtoBuilder currentStageName(String currentStageName) { this.currentStageName = currentStageName; return this; }
        public ClientPortalDtoBuilder estimatedDeliveryEta(LocalDateTime eta) { this.militaryDeliveryEta = eta; return this; }
        public ClientPortalDtoBuilder paymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public ClientPortalDtoBuilder totalContractValue(Double totalContractValue) { this.totalContractValue = totalContractValue; return this; }
        public ClientPortalDtoBuilder timeline(List<WorkflowLogDto> timeline) { this.timeline = timeline; return this; }
        public ClientPortalDtoBuilder documents(List<ClientDocument> documents) { this.documents = documents; return this; }
        public ClientPortalDtoBuilder photos(List<ProductionPhoto> photos) { this.photos = photos; return this; }
        public ClientPortalDtoBuilder issues(List<ClientIssue> issues) { this.issues = issues; return this; }
        public ClientPortalDtoBuilder approvals(List<SampleApproval> approvals) { this.approvals = approvals; return this; }

        public ClientPortalDto build() {
            return new ClientPortalDto(orderId, orderNumber, productName, quantity, status, currentStageName, 
                                       militaryDeliveryEta, paymentStatus, totalContractValue, timeline, documents, 
                                       photos, issues, approvals);
        }
    }
}
