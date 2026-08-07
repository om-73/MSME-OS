package com.msme.erp.repository;

import com.msme.erp.domain.WorkflowLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkflowLogRepository extends JpaRepository<WorkflowLog, String> {
    List<WorkflowLog> findByProductionWorkflowIdOrderByStartTimeDesc(String productionWorkflowId);
    List<WorkflowLog> findByOperatorId(String operatorId);
    List<WorkflowLog> findByDepartmentId(String departmentId);
}
