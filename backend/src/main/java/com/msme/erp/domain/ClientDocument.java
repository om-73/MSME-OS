package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_documents")
public class ClientDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // INVOICE, SPEC_SHEET, PURCHASE_ORDER, QC_REPORT

    private String fileUrl;
    private String uploadedBy;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public ClientDocument() {}

    public ClientDocument(String id, String tenantId, String orderId, String name, String type, String fileUrl, 
                          String uploadedBy, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.name = name;
        this.type = type;
        this.fileUrl = fileUrl;
        this.uploadedBy = uploadedBy;
        this.createdAt = createdAt;
    }

    public static ClientDocumentBuilder builder() {
        return new ClientDocumentBuilder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class ClientDocumentBuilder {
        private String id;
        private String tenantId;
        private String orderId;
        private String name;
        private String type;
        private String fileUrl;
        private String uploadedBy;
        private LocalDateTime createdAt;

        public ClientDocumentBuilder id(String id) { this.id = id; return this; }
        public ClientDocumentBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public ClientDocumentBuilder orderId(String orderId) { this.orderId = orderId; return this; }
        public ClientDocumentBuilder name(String name) { this.name = name; return this; }
        public ClientDocumentBuilder type(String type) { this.type = type; return this; }
        public ClientDocumentBuilder fileUrl(String fileUrl) { this.fileUrl = fileUrl; return this; }
        public ClientDocumentBuilder uploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; return this; }
        public ClientDocumentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ClientDocument build() {
            return new ClientDocument(id, tenantId, orderId, name, type, fileUrl, uploadedBy, createdAt);
        }
    }
}
