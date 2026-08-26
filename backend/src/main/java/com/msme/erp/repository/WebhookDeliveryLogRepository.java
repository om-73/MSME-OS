package com.msme.erp.repository;

import com.msme.erp.domain.WebhookDeliveryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebhookDeliveryLogRepository extends JpaRepository<WebhookDeliveryLog, Long> {
    List<WebhookDeliveryLog> findByTenantIdOrderByTimestampDesc(String tenantId);
    Optional<WebhookDeliveryLog> findByIdempotencyKey(String idempotencyKey);
}
