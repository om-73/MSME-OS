package com.msme.erp.repository;

import com.msme.erp.domain.WorkerTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerTaskRepository extends JpaRepository<WorkerTask, Long> {
    List<WorkerTask> findByTenantId(String tenantId);
    List<WorkerTask> findByTenantIdAndAssignedWorkerId(String tenantId, String workerId);
    List<WorkerTask> findByTenantIdAndOrderId(String tenantId, String orderId);
    List<WorkerTask> findByTenantIdAndStatus(String tenantId, String status);
}
