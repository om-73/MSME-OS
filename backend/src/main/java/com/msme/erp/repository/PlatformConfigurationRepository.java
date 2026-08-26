package com.msme.erp.repository;

import com.msme.erp.domain.PlatformConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatformConfigurationRepository extends JpaRepository<PlatformConfiguration, Long> {
    Optional<PlatformConfiguration> findByConfigKey(String configKey);
    List<PlatformConfiguration> findByTenantId(String tenantId);
}
