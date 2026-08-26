package com.msme.erp.repository;

import com.msme.erp.domain.ComplianceEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplianceEvidenceRepository extends JpaRepository<ComplianceEvidence, Long> {
    List<ComplianceEvidence> findByControlId(Long controlId);
}
