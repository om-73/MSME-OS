package com.msme.erp.repository;

import com.msme.erp.domain.ClientIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientIssueRepository extends JpaRepository<ClientIssue, String> {
    List<ClientIssue> findByTenantIdAndOrderIdOrderByCreatedAtDesc(String tenantId, String orderId);
}
