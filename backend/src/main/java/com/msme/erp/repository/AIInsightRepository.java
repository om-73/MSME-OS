package com.msme.erp.repository;

import com.msme.erp.domain.AIInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIInsightRepository extends JpaRepository<AIInsight, Long> {
    List<AIInsight> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<AIInsight> findByTenantIdAndCategoryOrderByCreatedAtDesc(String tenantId, String category);
}
