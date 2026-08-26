package com.msme.erp.repository;

import com.msme.erp.domain.AIAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIAuditLogRepository extends JpaRepository<AIAuditLog, Long> {
    List<AIAuditLog> findByTenantIdOrderByTimestampDesc(String tenantId);
}
