package com.msme.erp.repository;

import com.msme.erp.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    List<Department> findByTenantIdAndDeletedFalse(String tenantId);
    Optional<Department> findByIdAndTenantIdAndDeletedFalse(String id, String tenantId);
    Optional<Department> findByTenantIdAndCodeAndDeletedFalse(String tenantId, String code);
}
