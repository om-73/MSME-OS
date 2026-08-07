package com.msme.erp.repository;

import com.msme.erp.domain.InventoryAuditItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryAuditItemRepository extends JpaRepository<InventoryAuditItem, String> {
    List<InventoryAuditItem> findByAuditId(String auditId);
}
