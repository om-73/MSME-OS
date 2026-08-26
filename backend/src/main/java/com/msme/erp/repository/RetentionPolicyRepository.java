package com.msme.erp.repository;

import com.msme.erp.domain.RetentionPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RetentionPolicyRepository extends JpaRepository<RetentionPolicy, Long> {
    Optional<RetentionPolicy> findByTenantIdAndTargetRecordType(String tenantId, String targetRecordType);
    List<RetentionPolicy> findByTenantId(String tenantId);
}
