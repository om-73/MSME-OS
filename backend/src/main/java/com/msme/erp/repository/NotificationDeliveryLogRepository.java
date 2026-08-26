package com.msme.erp.repository;

import com.msme.erp.domain.NotificationDeliveryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationDeliveryLogRepository extends JpaRepository<NotificationDeliveryLog, Long> {
    List<NotificationDeliveryLog> findByTenantIdOrderByTimestampDesc(String tenantId);
    List<NotificationDeliveryLog> findByTenantIdAndRecipientIdOrderByTimestampDesc(String tenantId, String recipientId);
    List<NotificationDeliveryLog> findByTenantIdAndStatus(String tenantId, String status);
    Optional<NotificationDeliveryLog> findByIdempotencyKey(String idempotencyKey);
}
