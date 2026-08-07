package com.msme.erp.repository;

import com.msme.erp.domain.QCRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QCRecordRepository extends JpaRepository<QCRecord, String> {
    List<QCRecord> findByTenantId(String tenantId);
    List<QCRecord> findByTenantIdAndOrderId(String tenantId, String orderId);
    long countByTenantIdAndPassedTrue(String tenantId);
    long countByTenantIdAndPassedFalse(String tenantId);
}
