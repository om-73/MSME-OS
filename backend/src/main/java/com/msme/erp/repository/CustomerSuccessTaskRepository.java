package com.msme.erp.repository;

import com.msme.erp.domain.CustomerSuccessTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerSuccessTaskRepository extends JpaRepository<CustomerSuccessTask, Long> {
    List<CustomerSuccessTask> findByTenantId(String tenantId);
    List<CustomerSuccessTask> findByTenantIdAndClientCodeOrderByCreatedAtDesc(String tenantId, String clientCode);
}
