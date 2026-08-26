package com.msme.erp.repository;

import com.msme.erp.domain.DocumentShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentShareRepository extends JpaRepository<DocumentShare, Long> {
    List<DocumentShare> findByTenantIdAndDocumentId(String tenantId, Long documentId);
    Optional<DocumentShare> findByShareToken(String shareToken);
}
