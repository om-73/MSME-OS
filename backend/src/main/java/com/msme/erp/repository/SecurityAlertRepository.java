package com.msme.erp.repository;

import com.msme.erp.domain.SecurityAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityAlertRepository extends JpaRepository<SecurityAlert, Long> {
    List<SecurityAlert> findByTenantId(String tenantId);
    List<SecurityAlert> findByResolvedFalse();
}
