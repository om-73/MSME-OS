package com.msme.erp.repository;

import com.msme.erp.domain.OrderStatus;
import com.msme.erp.domain.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, String> {
    List<ProductionOrder> findByTenantId(String tenantId);
    List<ProductionOrder> findByTenantIdAndBrandId(String tenantId, String brandId);
    List<ProductionOrder> findByTenantIdAndStatus(String tenantId, OrderStatus status);
    long countByTenantId(String tenantId);
    long countByTenantIdAndStatus(String tenantId, OrderStatus status);
}
