package com.msme.erp.repository;

import com.msme.erp.domain.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    List<NotificationTemplate> findByTenantId(String tenantId);
    Optional<NotificationTemplate> findByTenantIdAndEventTypeAndChannel(String tenantId, String eventType, String channel);
    List<NotificationTemplate> findByTenantIdAndEventType(String tenantId, String eventType);
}
