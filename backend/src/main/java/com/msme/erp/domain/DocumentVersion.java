package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_versions")
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private Long documentId;

    @Column(nullable = false)
    private String versionNumber; // 1.0, 2.0, 3.0

    @Column(length = 1000)
    private String changeDescription;

    private String uploadedBy;
    private String storageUri;
    private String checksumSha256;
    private Long fileSizeBytes;

    private LocalDateTime createdAt;

    public DocumentVersion() {}

    public DocumentVersion(Long id, String tenantId, Long documentId, String versionNumber, String changeDescription, String uploadedBy, String storageUri, String checksumSha256, Long fileSizeBytes, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.documentId = documentId;
        this.versionNumber = versionNumber;
        this.changeDescription = changeDescription;
        this.uploadedBy = uploadedBy;
        this.storageUri = storageUri;
        this.checksumSha256 = checksumSha256;
        this.fileSizeBytes = fileSizeBytes;
        this.createdAt = createdAt;
    }

    public static DocumentVersionBuilder builder() {
        return new DocumentVersionBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }
    public String getVersionNumber() { return versionNumber; }
    public void setVersionNumber(String versionNumber) { this.versionNumber = versionNumber; }
    public String getChangeDescription() { return changeDescription; }
    public void setChangeDescription(String changeDescription) { this.changeDescription = changeDescription; }
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
    public String getStorageUri() { return storageUri; }
    public void setStorageUri(String storageUri) { this.storageUri = storageUri; }
    public String getChecksumSha256() { return checksumSha256; }
    public void setChecksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; }
    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class DocumentVersionBuilder {
        private Long id;
        private String tenantId;
        private Long documentId;
        private String versionNumber;
        private String changeDescription;
        private String uploadedBy;
        private String storageUri;
        private String checksumSha256;
        private Long fileSizeBytes;
        private LocalDateTime createdAt;

        public DocumentVersionBuilder id(Long id) { this.id = id; return this; }
        public DocumentVersionBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public DocumentVersionBuilder documentId(Long documentId) { this.documentId = documentId; return this; }
        public DocumentVersionBuilder versionNumber(String versionNumber) { this.versionNumber = versionNumber; return this; }
        public DocumentVersionBuilder changeDescription(String changeDescription) { this.changeDescription = changeDescription; return this; }
        public DocumentVersionBuilder uploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; return this; }
        public DocumentVersionBuilder storageUri(String storageUri) { this.storageUri = storageUri; return this; }
        public DocumentVersionBuilder checksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; return this; }
        public DocumentVersionBuilder fileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; return this; }
        public DocumentVersionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public DocumentVersion build() {
            return new DocumentVersion(id, tenantId, documentId, versionNumber, changeDescription, uploadedBy, storageUri, checksumSha256, fileSizeBytes, createdAt);
        }
    }
}
