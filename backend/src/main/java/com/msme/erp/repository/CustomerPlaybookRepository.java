package com.msme.erp.repository;

import com.msme.erp.domain.CustomerPlaybook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerPlaybookRepository extends JpaRepository<CustomerPlaybook, Long> {
    List<CustomerPlaybook> findByTenantId(String tenantId);
    List<CustomerPlaybook> findByTenantIdAndActive(String tenantId, boolean active);
}
