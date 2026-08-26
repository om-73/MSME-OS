package com.msme.erp.repository;

import com.msme.erp.domain.CustomerContactReference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CustomerContactReferenceRepository extends JpaRepository<CustomerContactReference, Long> {
    List<CustomerContactReference> findByTenantId(String tenantId);
    List<CustomerContactReference> findByTenantIdAndClientCode(String tenantId, String clientCode);
    Optional<CustomerContactReference> findByTenantIdAndClientCodeAndUserEmail(String tenantId, String clientCode, String userEmail);
}
