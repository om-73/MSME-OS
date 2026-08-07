package com.msme.erp.repository;

import com.msme.erp.domain.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {
    List<InventoryItem> findByTenantId(String tenantId);
    List<InventoryItem> findByTenantIdAndCategory(String tenantId, String category);
    Optional<InventoryItem> findByTenantIdAndCode(String tenantId, String code);
    Optional<InventoryItem> findByTenantIdAndBarcode(String tenantId, String barcode);
    List<InventoryItem> findByTenantIdAndClientBrandId(String tenantId, String clientBrandId);
}
