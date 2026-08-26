package com.msme.erp.repository;

import com.msme.erp.domain.IntegrationConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntegrationConnectionRepository extends JpaRepository<IntegrationConnection, Long> {
    List<IntegrationConnection> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<IntegrationConnection> findByTenantIdAndProviderKey(String tenantId, String providerKey);
}
