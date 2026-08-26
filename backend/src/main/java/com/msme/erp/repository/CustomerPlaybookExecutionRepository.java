package com.msme.erp.repository;

import com.msme.erp.domain.CustomerPlaybookExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerPlaybookExecutionRepository extends JpaRepository<CustomerPlaybookExecution, Long> {
    List<CustomerPlaybookExecution> findByTenantId(String tenantId);
    List<CustomerPlaybookExecution> findByTenantIdAndClientCode(String tenantId, String clientCode);
}
