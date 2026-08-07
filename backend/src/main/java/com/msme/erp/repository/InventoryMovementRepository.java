package com.msme.erp.repository;

import com.msme.erp.domain.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, String> {
    List<InventoryMovement> findByTenantIdOrderByTimestampDesc(String tenantId);
    List<InventoryMovement> findByTenantIdAndInventoryItemIdOrderByTimestampDesc(String tenantId, String inventoryItemId);
    List<InventoryMovement> findByTenantIdAndOrderId(String tenantId, String orderId);
}
