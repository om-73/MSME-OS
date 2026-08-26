package com.msme.erp.repository;

import com.msme.erp.domain.DocumentFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentFolderRepository extends JpaRepository<DocumentFolder, Long> {
    List<DocumentFolder> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}
