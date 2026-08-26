package com.msme.erp.repository;

import com.msme.erp.domain.CustomerCommunicationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerCommunicationPreferenceRepository extends JpaRepository<CustomerCommunicationPreference, Long> {
    Optional<CustomerCommunicationPreference> findByTenantIdAndClientCode(String tenantId, String clientCode);
}
