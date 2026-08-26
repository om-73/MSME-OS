package com.msme.erp.repository;

import com.msme.erp.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<Document> findByTenantIdAndCategory(String tenantId, String category);
    List<Document> findByTenantIdAndRelatedEntityTypeAndRelatedEntityId(String tenantId, String relatedEntityType, String relatedEntityId);
}
