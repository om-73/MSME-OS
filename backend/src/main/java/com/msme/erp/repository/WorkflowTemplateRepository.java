package com.msme.erp.repository;

import com.msme.erp.domain.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkflowTemplateRepository extends JpaRepository<WorkflowTemplate, String> {
    List<WorkflowTemplate> findByIndustry(String industry);
}
