package com.msme.erp.repository;

import com.msme.erp.domain.EmployeeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EmployeeAssignmentRepository extends JpaRepository<EmployeeAssignment, String> {
    List<EmployeeAssignment> findByTenantId(String tenantId);
    List<EmployeeAssignment> findByDepartmentId(String departmentId);
    List<EmployeeAssignment> findByUserId(String userId);
    Optional<EmployeeAssignment> findByUserIdAndDepartmentId(String userId, String departmentId);
    void deleteByUserIdAndDepartmentId(String userId, String departmentId);
}
