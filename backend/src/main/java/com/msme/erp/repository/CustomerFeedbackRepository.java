package com.msme.erp.repository;

import com.msme.erp.domain.CustomerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerFeedbackRepository extends JpaRepository<CustomerFeedback, Long> {
    List<CustomerFeedback> findByTenantId(String tenantId);
    List<CustomerFeedback> findByTenantIdAndClientCodeOrderByCreatedAtDesc(String tenantId, String clientCode);
}
