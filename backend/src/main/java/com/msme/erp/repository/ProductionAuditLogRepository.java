package com.msme.erp.repository;

import com.msme.erp.domain.ProductionAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionAuditLogRepository extends JpaRepository<ProductionAuditLog, Long> {
    List<ProductionAuditLog> findByTenantIdAndOrderIdOrderByTimestampDesc(String tenantId, String orderId);
    List<ProductionAuditLog> findByTenantIdOrderByTimestampDesc(String tenantId);
}
