package com.msme.erp.repository;

import com.msme.erp.domain.LegalHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegalHoldRepository extends JpaRepository<LegalHold, Long> {
    List<LegalHold> findByTenantIdAndActiveTrue(String tenantId);
    List<LegalHold> findByActiveTrue();
}
