package com.msme.erp.service;

import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentManagementServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentVersionRepository versionRepository;

    @Mock
    private DocumentShareRepository shareRepository;

    @Mock
    private DocumentFolderRepository folderRepository;

    @Mock
    private NotificationCenterService notificationCenterService;

    @InjectMocks
    private DocumentManagementService documentService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testUploadDocumentGeneratesChecksumAndInitialVersionV1() {
        when(documentRepository.save(any())).thenAnswer(inv -> {
            Document d = inv.getArgument(0);
            d.setId(10L);
            return d;
        });

        Document doc = documentService.uploadDocument("Men_Shirt_TechPack.pdf", "PDF", "PRODUCTION", 2450000L, "ORDER", "ORD-2026-88", "TechPack");

        assertNotNull(doc);
        assertEquals("1.0", doc.getCurrentVersion());
        assertNotNull(doc.getChecksumSha256());
        verify(versionRepository, times(1)).save(any(DocumentVersion.class));
    }

    @Test
    void testUploadNewVersionIncrementsVersionNumber() {
        Document doc = Document.builder()
                .id(10L)
                .tenantId("apex-tenant-01")
                .fileName("Men_Shirt_TechPack.pdf")
                .currentVersion("1.0")
                .fileSizeBytes(2450000L)
                .build();

        when(documentRepository.findById(10L)).thenReturn(Optional.of(doc));
        when(documentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(versionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DocumentVersion version = documentService.uploadNewVersion(10L, "Updated sleeve dimensions", 2600000L, "designer@apex.com");

        assertEquals("2.0", version.getVersionNumber());
        assertEquals("2.0", doc.getCurrentVersion());
        assertEquals("PENDING_REVIEW", doc.getStatus());
    }

    @Test
    void testApproveDocumentFiresNotification() {
        Document doc = Document.builder()
                .id(10L)
                .tenantId("apex-tenant-01")
                .fileName("TechPack.pdf")
                .status("PENDING_REVIEW")
                .build();

        when(documentRepository.findById(10L)).thenReturn(Optional.of(doc));
        when(documentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Document approved = documentService.approveDocument(10L, "factory_owner");

        assertEquals("APPROVED", approved.getStatus());
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("DocumentApprovedEvent"), any(), eq("NORMAL"), any());
    }
}
