package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_shares")
public class DocumentShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private Long documentId;

    @Column(nullable = false, unique = true)
    private String shareToken;

    private String recipientEmail;
    private int maxDownloads = 10;
    private int downloadCount = 0;

    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public DocumentShare() {}

    public DocumentShare(Long id, String tenantId, Long documentId, String shareToken, String recipientEmail, int maxDownloads, int downloadCount, LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.documentId = documentId;
        this.shareToken = shareToken;
        this.recipientEmail = recipientEmail;
        this.maxDownloads = maxDownloads;
        this.downloadCount = downloadCount;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public static DocumentShareBuilder builder() {
        return new DocumentShareBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }
    public String getShareToken() { return shareToken; }
    public void setShareToken(String shareToken) { this.shareToken = shareToken; }
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public int getMaxDownloads() { return maxDownloads; }
    public void setMaxDownloads(int maxDownloads) { this.maxDownloads = maxDownloads; }
    public int getDownloadCount() { return downloadCount; }
    public void setDownloadCount(int downloadCount) { this.downloadCount = downloadCount; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (expiresAt == null) expiresAt = LocalDateTime.now().plusDays(7);
    }

    public static class DocumentShareBuilder {
        private Long id;
        private String tenantId;
        private Long documentId;
        private String shareToken;
        private String recipientEmail;
        private int maxDownloads = 10;
        private int downloadCount = 0;
        private LocalDateTime expiresAt;
        private LocalDateTime createdAt;

        public DocumentShareBuilder id(Long id) { this.id = id; return this; }
        public DocumentShareBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public DocumentShareBuilder documentId(Long documentId) { this.documentId = documentId; return this; }
        public DocumentShareBuilder shareToken(String shareToken) { this.shareToken = shareToken; return this; }
        public DocumentShareBuilder recipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; return this; }
        public DocumentShareBuilder maxDownloads(int maxDownloads) { this.maxDownloads = maxDownloads; return this; }
        public DocumentShareBuilder downloadCount(int downloadCount) { this.downloadCount = downloadCount; return this; }
        public DocumentShareBuilder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public DocumentShareBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public DocumentShare build() {
            return new DocumentShare(id, tenantId, documentId, shareToken, recipientEmail, maxDownloads, downloadCount, expiresAt, createdAt);
        }
    }
}
