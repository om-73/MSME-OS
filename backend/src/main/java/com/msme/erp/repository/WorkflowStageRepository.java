package com.msme.erp.repository;

import com.msme.erp.domain.WorkflowStage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WorkflowStageRepository extends JpaRepository<WorkflowStage, String> {
    List<WorkflowStage> findByWorkflowVersionIdOrderBySequenceOrderAsc(String workflowVersionId);
    Optional<WorkflowStage> findByIdAndWorkflowVersionId(String id, String workflowVersionId);
    List<WorkflowStage> findByDepartmentId(String departmentId);
}
