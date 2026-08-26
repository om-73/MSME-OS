package com.msme.erp.repository;

import com.msme.erp.domain.ApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
    List<ApprovalRequest> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<ApprovalRequest> findByTenantIdAndStatus(String tenantId, String status);
}
