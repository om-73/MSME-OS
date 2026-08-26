package com.msme.erp.repository;

import com.msme.erp.domain.ComplianceControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplianceControlRepository extends JpaRepository<ComplianceControl, Long> {
    Optional<ComplianceControl> findByControlCode(String controlCode);
    List<ComplianceControl> findByFramework(String framework);
}
