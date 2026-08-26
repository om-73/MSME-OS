package com.msme.erp.repository;

import com.msme.erp.domain.SecurityPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityPolicyRepository extends JpaRepository<SecurityPolicy, Long> {
    Optional<SecurityPolicy> findByTenantId(String tenantId);
}
