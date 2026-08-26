package com.msme.erp.repository;

import com.msme.erp.domain.CustomerHealthSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerHealthSnapshotRepository extends JpaRepository<CustomerHealthSnapshot, Long> {
    List<CustomerHealthSnapshot> findByTenantId(String tenantId);
    List<CustomerHealthSnapshot> findByTenantIdAndClientCodeOrderByRecordedAtDesc(String tenantId, String clientCode);
}
