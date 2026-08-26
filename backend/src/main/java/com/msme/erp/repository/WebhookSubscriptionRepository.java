package com.msme.erp.repository;

import com.msme.erp.domain.WebhookSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebhookSubscriptionRepository extends JpaRepository<WebhookSubscription, Long> {
    List<WebhookSubscription> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<WebhookSubscription> findByTenantIdAndActive(String tenantId, boolean active);
}
