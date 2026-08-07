package com.msme.erp.repository;

import com.msme.erp.domain.WorkflowHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkflowHistoryRepository extends JpaRepository<WorkflowHistory, String> {
    List<WorkflowHistory> findByWorkflowIdOrderByTimestampDesc(String workflowId);
}
