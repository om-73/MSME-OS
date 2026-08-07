package com.msme.erp.repository;

import com.msme.erp.domain.WorkflowRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkflowRuleRepository extends JpaRepository<WorkflowRule, String> {
    List<WorkflowRule> findByStageId(String stageId);
    void deleteByStageId(String stageId);
}
