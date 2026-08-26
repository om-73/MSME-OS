package com.msme.erp.repository;

import com.msme.erp.domain.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {
    List<DocumentVersion> findByTenantIdAndDocumentIdOrderByCreatedAtDesc(String tenantId, Long documentId);
}
