package com.msme.erp.repository;

import com.msme.erp.domain.AutomationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutomationRuleRepository extends JpaRepository<AutomationRule, Long> {
    List<AutomationRule> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<AutomationRule> findByTenantIdAndTriggerEventAndActive(String tenantId, String triggerEvent, boolean active);
}
