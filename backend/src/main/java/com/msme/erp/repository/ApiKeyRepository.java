package com.msme.erp.repository;

import com.msme.erp.domain.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    List<ApiKey> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<ApiKey> findByKeyPrefix(String keyPrefix);
}
