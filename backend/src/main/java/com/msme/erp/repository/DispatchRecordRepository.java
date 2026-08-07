package com.msme.erp.repository;

import com.msme.erp.domain.DispatchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispatchRecordRepository extends JpaRepository<DispatchRecord, Long> {
    List<DispatchRecord> findByTenantId(String tenantId);
    List<DispatchRecord> findByTenantIdAndStatus(String tenantId, String status);
}
