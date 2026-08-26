package com.msme.erp.repository;

import com.msme.erp.domain.CustomerSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerSegmentRepository extends JpaRepository<CustomerSegment, Long> {
    List<CustomerSegment> findByTenantId(String tenantId);
}
