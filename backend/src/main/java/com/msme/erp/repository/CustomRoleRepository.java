package com.msme.erp.repository;

import com.msme.erp.domain.CustomRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRoleRepository extends JpaRepository<CustomRole, Long> {
    List<CustomRole> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}
