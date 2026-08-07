package com.msme.erp.repository;

import com.msme.erp.domain.ProductionWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductionWorkflowRepository extends JpaRepository<ProductionWorkflow, String> {
    Optional<ProductionWorkflow> findByOrderId(String orderId);
    Optional<ProductionWorkflow> findByOrderIdAndStatus(String orderId, String status);
}
