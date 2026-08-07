package com.msme.erp.repository;

import com.msme.erp.domain.SampleApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SampleApprovalRepository extends JpaRepository<SampleApproval, String> {
    List<SampleApproval> findByTenantIdAndOrderId(String tenantId, String orderId);
}
