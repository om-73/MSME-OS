package com.msme.erp.repository;

import com.msme.erp.domain.AIRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIRecommendationRepository extends JpaRepository<AIRecommendation, Long> {
    List<AIRecommendation> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<AIRecommendation> findByTenantIdAndApprovalStatus(String tenantId, String approvalStatus);
}
