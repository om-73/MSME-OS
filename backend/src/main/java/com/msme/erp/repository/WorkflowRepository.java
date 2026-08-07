package com.msme.erp.repository;

import com.msme.erp.domain.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WorkflowRepository extends JpaRepository<Workflow, String> {
    List<Workflow> findByTenantIdAndDeletedFalse(String tenantId);
    List<Workflow> findByTenantIdAndIndustryAndDeletedFalse(String tenantId, String industry);
    Optional<Workflow> findByIdAndTenantIdAndDeletedFalse(String id, String tenantId);
    Optional<Workflow> findByTenantIdAndStatusAndDeletedFalse(String tenantId, String status);
}
