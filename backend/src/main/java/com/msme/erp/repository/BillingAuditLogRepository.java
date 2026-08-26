package com.msme.erp.repository;

import com.msme.erp.domain.BillingAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingAuditLogRepository extends JpaRepository<BillingAuditLog, Long> {
    List<BillingAuditLog> findByTenantIdOrderByTimestampDesc(String tenantId);
    List<BillingAuditLog> findAllByOrderByTimestampDesc();
}
