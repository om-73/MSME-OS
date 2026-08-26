package com.msme.erp.repository;

import com.msme.erp.domain.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<Prediction> findByTenantIdAndRiskLevel(String tenantId, String riskLevel);
}
