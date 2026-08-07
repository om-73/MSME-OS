package com.msme.erp.repository;

import com.msme.erp.domain.WorkflowVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WorkflowVersionRepository extends JpaRepository<WorkflowVersion, String> {
    List<WorkflowVersion> findByWorkflowIdOrderByVersionNumberDesc(String workflowId);
    Optional<WorkflowVersion> findByWorkflowIdAndVersionNumber(String workflowId, Integer versionNumber);
    Optional<WorkflowVersion> findByWorkflowIdAndStatus(String workflowId, String status);
}
