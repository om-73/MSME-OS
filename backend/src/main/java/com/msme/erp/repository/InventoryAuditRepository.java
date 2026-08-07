package com.msme.erp.repository;

import com.msme.erp.domain.InventoryAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryAuditRepository extends JpaRepository<InventoryAudit, String> {
    List<InventoryAudit> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}
