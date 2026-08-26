package com.msme.erp.controller;

import com.msme.erp.domain.*;
import com.msme.erp.service.DocumentManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentManagementController {

    private final DocumentManagementService documentService;

    public DocumentManagementController(DocumentManagementService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<Document>> getDocuments(@RequestParam(required = false) String category) {
        return ResponseEntity.ok(documentService.getDocuments(category));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Document> uploadDocument(@RequestBody Map<String, String> payload) {
        String fileName = payload.getOrDefault("fileName", "TechPack_V1.pdf");
        String fileType = payload.getOrDefault("fileType", "PDF");
        String category = payload.getOrDefault("category", "PRODUCTION");
        Long fileSizeBytes = Long.valueOf(payload.getOrDefault("fileSizeBytes", "1024500"));
        String relatedType = payload.get("relatedEntityType");
        String relatedId = payload.get("relatedEntityId");
        String tags = payload.get("tags");
        return ResponseEntity.ok(documentService.uploadDocument(fileName, fileType, category, fileSizeBytes, relatedType, relatedId, tags));
    }

    @PostMapping("/{id}/versions")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<DocumentVersion> uploadNewVersion(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String changeDescription = payload.getOrDefault("changeDescription", "Updated specification details");
        Long fileSizeBytes = Long.valueOf(payload.getOrDefault("fileSizeBytes", "1250000"));
        String uploader = payload.getOrDefault("uploader", "designer@apex.com");
        return ResponseEntity.ok(documentService.uploadNewVersion(id, changeDescription, fileSizeBytes, uploader));
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<DocumentVersion>> getDocumentVersions(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentVersions(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Map<String, Object>> generateSignedDownloadUrl(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.generateSignedDownloadUrl(id));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Document> approveDocument(@PathVariable Long id, @RequestParam(required = false, defaultValue = "factory_owner") String reviewer) {
        return ResponseEntity.ok(documentService.approveDocument(id, reviewer));
    }

    @PostMapping("/{id}/share")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<DocumentShare> createShareLink(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        String recipientEmail = (String) payload.getOrDefault("recipientEmail", "client@brand.com");
        int maxDownloads = Integer.parseInt(payload.getOrDefault("maxDownloads", 10).toString());
        return ResponseEntity.ok(documentService.createShareLink(id, recipientEmail, maxDownloads));
    }

    @GetMapping("/quota")
    public ResponseEntity<Map<String, Object>> getStorageQuotaUsage() {
        return ResponseEntity.ok(documentService.getStorageQuotaUsage());
    }
}
