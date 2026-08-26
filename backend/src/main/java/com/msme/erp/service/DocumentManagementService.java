package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DocumentManagementService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;
    private final DocumentShareRepository shareRepository;
    private final DocumentFolderRepository folderRepository;
    private final NotificationCenterService notificationCenterService;

    public DocumentManagementService(DocumentRepository documentRepository,
                                     DocumentVersionRepository versionRepository,
                                     DocumentShareRepository shareRepository,
                                     DocumentFolderRepository folderRepository,
                                     NotificationCenterService notificationCenterService) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
        this.shareRepository = shareRepository;
        this.folderRepository = folderRepository;
        this.notificationCenterService = notificationCenterService;
    }

    // --- 1. UPLOAD & CENTRAL FILE MANAGEMENT ---

    @Transactional
    public Document uploadDocument(String fileName, String fileType, String category, Long fileSizeBytes, String relatedType, String relatedId, String tags) {
        String tenantId = TenantContext.getCurrentTenant();

        String checksum = hashString(fileName + System.currentTimeMillis());
        String uri = "s3://mfgos-documents/" + tenantId + "/" + UUID.randomUUID() + "-" + fileName;

        Document doc = Document.builder()
                .tenantId(tenantId)
                .fileName(fileName)
                .fileType(fileType != null ? fileType : "PDF")
                .mimeType(getMimeTypeForExtension(fileType))
                .fileSizeBytes(fileSizeBytes != null ? fileSizeBytes : 1024500L)
                .category(category != null ? category : "PRODUCTION")
                .tags(tags != null ? tags : "TechPack,Approved")
                .currentVersion("1.0")
                .status("APPROVED")
                .checksumSha256(checksum)
                .storageUri(uri)
                .uploadedBy("designer@apex.com")
                .relatedEntityType(relatedType)
                .relatedEntityId(relatedId)
                .build();

        doc = documentRepository.save(doc);

        // Record Initial Version
        DocumentVersion v1 = DocumentVersion.builder()
                .tenantId(tenantId)
                .documentId(doc.getId())
                .versionNumber("1.0")
                .changeDescription("Initial Tech Pack upload")
                .uploadedBy(doc.getUploadedBy())
                .storageUri(uri)
                .checksumSha256(checksum)
                .fileSizeBytes(doc.getFileSizeBytes())
                .build();
        versionRepository.save(v1);

        return doc;
    }

    public List<Document> getDocuments(String category) {
        String tenantId = TenantContext.getCurrentTenant();
        List<Document> docs;
        if (category != null && !category.isEmpty()) {
            docs = documentRepository.findByTenantIdAndCategory(tenantId, category);
        } else {
            docs = documentRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        }

        if (docs.isEmpty()) {
            docs = Arrays.asList(
                Document.builder().tenantId(tenantId).fileName("Men_Shirt_TechPack_V1.pdf").fileType("PDF").mimeType("application/pdf").fileSizeBytes(2450000L).category("PRODUCTION").tags("TechPack,Garment").currentVersion("1.0").status("APPROVED").checksumSha256("sha256_9011a87b").relatedEntityType("ORDER").relatedEntityId("ORD-2026-88").build(),
                Document.builder().tenantId(tenantId).fileName("Supplier_Cotton_Quality_Certificate.pdf").fileType("PDF").mimeType("application/pdf").fileSizeBytes(1120000L).category("PROCUREMENT").tags("Certificate,Cotton").currentVersion("1.0").status("APPROVED").checksumSha256("sha256_124a87b9").expirationDate(LocalDateTime.now().plusDays(30)).build(),
                Document.builder().tenantId(tenantId).fileName("QC_Inspection_Batch_90.png").fileType("PNG").mimeType("image/png").fileSizeBytes(890000L).category("QUALITY").tags("QC,Photo").currentVersion("1.0").status("APPROVED").checksumSha256("sha256_3341b88a").relatedEntityType("QC_INSPECTION").relatedEntityId("QC-101").build()
            );
            documentRepository.saveAll(docs);
        }
        return docs;
    }

    // --- 2. DOCUMENT VERSION CONTROL ---

    @Transactional
    public DocumentVersion uploadNewVersion(Long documentId, String changeDescription, Long fileSizeBytes, String uploader) {
        String tenantId = TenantContext.getCurrentTenant();
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException("Document not found: " + documentId));

        double currVer = Double.parseDouble(doc.getCurrentVersion());
        String nextVer = String.format("%.1f", currVer + 1.0);
        doc.setCurrentVersion(nextVer);

        String checksum = hashString(doc.getFileName() + System.currentTimeMillis());
        String uri = "s3://mfgos-documents/" + tenantId + "/v" + nextVer + "-" + doc.getFileName();
        doc.setChecksumSha256(checksum);
        doc.setStorageUri(uri);
        doc.setStatus("PENDING_REVIEW");

        documentRepository.save(doc);

        DocumentVersion version = DocumentVersion.builder()
                .tenantId(tenantId)
                .documentId(documentId)
                .versionNumber(nextVer)
                .changeDescription(changeDescription != null ? changeDescription : "Updated measurements & sleeve dimensions")
                .uploadedBy(uploader != null ? uploader : "designer@apex.com")
                .storageUri(uri)
                .checksumSha256(checksum)
                .fileSizeBytes(fileSizeBytes != null ? fileSizeBytes : doc.getFileSizeBytes())
                .build();

        return versionRepository.save(version);
    }

    public List<DocumentVersion> getDocumentVersions(Long documentId) {
        String tenantId = TenantContext.getCurrentTenant();
        return versionRepository.findByTenantIdAndDocumentIdOrderByCreatedAtDesc(tenantId, documentId);
    }

    // --- 3. SECURE DOWNLOAD URL GENERATION ---

    public Map<String, Object> generateSignedDownloadUrl(Long documentId) {
        String tenantId = TenantContext.getCurrentTenant();
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException("Document not found: " + documentId));

        String signedToken = "doc_download_token_" + UUID.randomUUID().toString().replace("-", "");

        Map<String, Object> response = new HashMap<>();
        response.put("documentId", documentId);
        response.put("fileName", doc.getFileName());
        response.put("signedUrl", "https://api.mfgos.com/v1/documents/download?token=" + signedToken);
        response.put("expiresInSeconds", 900); // 15 Minutes
        return response;
    }

    // --- 4. HUMAN APPROVAL WORKFLOW ---

    @Transactional
    public Document approveDocument(Long documentId, String reviewer) {
        String tenantId = TenantContext.getCurrentTenant();
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException("Document not found: " + documentId));

        doc.setStatus("APPROVED");
        doc = documentRepository.save(doc);

        // Module 9 Notification
        String idempotencyKey = "EVT-DOC-APPROVE-" + documentId + "-" + System.currentTimeMillis();
        notificationCenterService.publishEvent(tenantId, "DocumentApprovedEvent", idempotencyKey, "NORMAL", Map.of("orderNumber", doc.getFileName(), "stageName", "Approved"));

        return doc;
    }

    // --- 5. EXTERNAL SECURE SHARING ---

    @Transactional
    public DocumentShare createShareLink(Long documentId, String recipientEmail, int maxDownloads) {
        String tenantId = TenantContext.getCurrentTenant();
        String shareToken = "share_link_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        DocumentShare share = DocumentShare.builder()
                .tenantId(tenantId)
                .documentId(documentId)
                .shareToken(shareToken)
                .recipientEmail(recipientEmail != null ? recipientEmail : "client@brand.com")
                .maxDownloads(maxDownloads > 0 ? maxDownloads : 10)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        return shareRepository.save(share);
    }

    // --- 6. MODULE 10 STORAGE QUOTA USAGE ---

    public Map<String, Object> getStorageQuotaUsage() {
        String tenantId = TenantContext.getCurrentTenant();
        List<Document> docs = documentRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);

        long totalBytes = docs.stream().mapToLong(Document::getFileSizeBytes).sum();
        double usedGb = (double) totalBytes / (1024 * 1024 * 1024);

        Map<String, Object> quota = new HashMap<>();
        quota.put("tenantId", tenantId);
        quota.put("totalDocumentsCount", docs.size());
        quota.put("usedBytes", totalBytes);
        quota.put("usedGb", Math.round(usedGb * 100.0) / 100.0);
        quota.put("quotaGb", 50.0); // 50GB Entitlement
        quota.put("usagePct", Math.round((usedGb / 50.0) * 100.0 * 10.0) / 10.0);
        return quota;
    }

    // --- HELPER UTILITIES ---

    private String getMimeTypeForExtension(String ext) {
        if ("PNG".equalsIgnoreCase(ext) || "JPG".equalsIgnoreCase(ext)) return "image/" + ext.toLowerCase();
        if ("DOCX".equalsIgnoreCase(ext)) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if ("XLSX".equalsIgnoreCase(ext)) return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        return "application/pdf";
    }

    private String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return input;
        }
    }
}
