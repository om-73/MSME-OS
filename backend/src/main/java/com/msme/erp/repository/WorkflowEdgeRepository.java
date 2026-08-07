package com.msme.erp.repository;

import com.msme.erp.domain.WorkflowEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkflowEdgeRepository extends JpaRepository<WorkflowEdge, String> {
    List<WorkflowEdge> findByWorkflowVersionId(String workflowVersionId);
    List<WorkflowEdge> findByWorkflowVersionIdAndSourceStageId(String workflowVersionId, String sourceStageId);
    void deleteByWorkflowVersionId(String workflowVersionId);
}
