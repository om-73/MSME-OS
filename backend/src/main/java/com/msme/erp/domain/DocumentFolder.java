package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_folders")
public class DocumentFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String folderName;

    private Long parentFolderId; // Nullable for root folders
    private LocalDateTime createdAt;

    public DocumentFolder() {}

    public DocumentFolder(Long id, String tenantId, String folderName, Long parentFolderId, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.folderName = folderName;
        this.parentFolderId = parentFolderId;
        this.createdAt = createdAt;
    }

    public static DocumentFolderBuilder builder() {
        return new DocumentFolderBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getFolderName() { return folderName; }
    public void setFolderName(String folderName) { this.folderName = folderName; }
    public Long getParentFolderId() { return parentFolderId; }
    public void setParentFolderId(Long parentFolderId) { this.parentFolderId = parentFolderId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class DocumentFolderBuilder {
        private Long id;
        private String tenantId;
        private String folderName;
        private Long parentFolderId;
        private LocalDateTime createdAt;

        public DocumentFolderBuilder id(Long id) { this.id = id; return this; }
        public DocumentFolderBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public DocumentFolderBuilder folderName(String folderName) { this.folderName = folderName; return this; }
        public DocumentFolderBuilder parentFolderId(Long parentFolderId) { this.parentFolderId = parentFolderId; return this; }
        public DocumentFolderBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public DocumentFolder build() {
            return new DocumentFolder(id, tenantId, folderName, parentFolderId, createdAt);
        }
    }
}
