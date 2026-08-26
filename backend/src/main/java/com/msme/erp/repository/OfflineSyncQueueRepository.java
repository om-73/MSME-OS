package com.msme.erp.repository;

import com.msme.erp.domain.OfflineSyncQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfflineSyncQueueRepository extends JpaRepository<OfflineSyncQueue, Long> {
    List<OfflineSyncQueue> findByTenantIdAndUserIdAndStatus(String tenantId, String userId, String status);
    Optional<OfflineSyncQueue> findByIdempotencyKey(String idempotencyKey);
}
