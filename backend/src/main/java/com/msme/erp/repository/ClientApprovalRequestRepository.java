package com.msme.erp.repository;

import com.msme.erp.domain.ClientApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientApprovalRequestRepository extends JpaRepository<ClientApprovalRequest, Long> {
    List<ClientApprovalRequest> findByTenantIdAndClientCodeOrderByCreatedAtDesc(String tenantId, String clientCode);
    List<ClientApprovalRequest> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}
