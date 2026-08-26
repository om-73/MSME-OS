package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType; // PDF, PNG, DOCX, XLSX

    @Column(nullable = false)
    private String mimeType;

    @Column(nullable = false)
    private Long fileSizeBytes;

    @Column(nullable = false)
    private String category; // PROCUREMENT, PRODUCTION, QUALITY, INVENTORY, DISPATCH, CLIENT, FINANCE

    private String tags; // e.g. TechPack,QC,Certificate,Invoice

    @Column(nullable = false)
    private String currentVersion = "1.0";

    @Column(nullable = false)
    private String status = "APPROVED"; // DRAFT, PENDING_REVIEW, APPROVED, REJECTED, EXPIRED, ARCHIVED

    private String checksumSha256;
    private String storageUri;
    private String uploadedBy;

    private String relatedEntityType; // ORDER, QC_INSPECTION, SKU, DISPATCH, INVOICE
    private String relatedEntityId;

    private LocalDateTime expirationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Document() {}

    public Document(Long id, String tenantId, String fileName, String fileType, String mimeType, Long fileSizeBytes, String category, String tags, String currentVersion, String status, String checksumSha256, String storageUri, String uploadedBy, String relatedEntityType, String relatedEntityId, LocalDateTime expirationDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.mimeType = mimeType;
        this.fileSizeBytes = fileSizeBytes;
        this.category = category;
        this.tags = tags;
        this.currentVersion = currentVersion;
        this.status = status;
        this.checksumSha256 = checksumSha256;
        this.storageUri = storageUri;
        this.uploadedBy = uploadedBy;
        this.relatedEntityType = relatedEntityType;
        this.relatedEntityId = relatedEntityId;
        this.expirationDate = expirationDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DocumentBuilder builder() {
        return new DocumentBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getCurrentVersion() { return currentVersion; }
    public void setCurrentVersion(String currentVersion) { this.currentVersion = currentVersion; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getChecksumSha256() { return checksumSha256; }
    public void setChecksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; }
    public String getStorageUri() { return storageUri; }
    public void setStorageUri(String storageUri) { this.storageUri = storageUri; }
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
    public String getRelatedEntityType() { return relatedEntityType; }
    public void setRelatedEntityType(String relatedEntityType) { this.relatedEntityType = relatedEntityType; }
    public String getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(String relatedEntityId) { this.relatedEntityId = relatedEntityId; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static class DocumentBuilder {
        private Long id;
        private String tenantId;
        private String fileName;
        private String fileType;
        private String mimeType;
        private Long fileSizeBytes;
        private String category;
        private String tags;
        private String currentVersion = "1.0";
        private String status = "APPROVED";
        private String checksumSha256;
        private String storageUri;
        private String uploadedBy;
        private String relatedEntityType;
        private String relatedEntityId;
        private LocalDateTime expirationDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public DocumentBuilder id(Long id) { this.id = id; return this; }
        public DocumentBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public DocumentBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public DocumentBuilder fileType(String fileType) { this.fileType = fileType; return this; }
        public DocumentBuilder mimeType(String mimeType) { this.mimeType = mimeType; return this; }
        public DocumentBuilder fileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; return this; }
        public DocumentBuilder category(String category) { this.category = category; return this; }
        public DocumentBuilder tags(String tags) { this.tags = tags; return this; }
        public DocumentBuilder currentVersion(String currentVersion) { this.currentVersion = currentVersion; return this; }
        public DocumentBuilder status(String status) { this.status = status; return this; }
        public DocumentBuilder checksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; return this; }
        public DocumentBuilder storageUri(String storageUri) { this.storageUri = storageUri; return this; }
        public DocumentBuilder uploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; return this; }
        public DocumentBuilder relatedEntityType(String relatedEntityType) { this.relatedEntityType = relatedEntityType; return this; }
        public DocumentBuilder relatedEntityId(String relatedEntityId) { this.relatedEntityId = relatedEntityId; return this; }
        public DocumentBuilder expirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; return this; }
        public DocumentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public DocumentBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Document build() {
            return new Document(id, tenantId, fileName, fileType, mimeType, fileSizeBytes, category, tags, currentVersion, status, checksumSha256, storageUri, uploadedBy, relatedEntityType, relatedEntityId, expirationDate, createdAt, updatedAt);
        }
    }
}
