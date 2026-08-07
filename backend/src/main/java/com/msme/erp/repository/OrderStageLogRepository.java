package com.msme.erp.repository;

import com.msme.erp.domain.OrderStageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderStageLogRepository extends JpaRepository<OrderStageLog, String> {
    List<OrderStageLog> findByTenantIdAndOrderIdOrderByTimestampDesc(String tenantId, String orderId);
    List<OrderStageLog> findTop20ByTenantIdOrderByTimestampDesc(String tenantId);
}
