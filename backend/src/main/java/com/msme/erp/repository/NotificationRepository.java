package com.msme.erp.repository;

import com.msme.erp.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<Notification> findByTenantIdAndReadStatusFalseOrderByCreatedAtDesc(String tenantId);
    long countByTenantIdAndReadStatusFalse(String tenantId);
}
