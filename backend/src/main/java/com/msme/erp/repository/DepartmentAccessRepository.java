package com.msme.erp.repository;

import com.msme.erp.domain.DepartmentAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentAccessRepository extends JpaRepository<DepartmentAccess, Long> {
    List<DepartmentAccess> findByTenantIdAndUserId(String tenantId, String userId);
    List<DepartmentAccess> findByTenantId(String tenantId);
}
