package com.msme.erp.repository;

import com.msme.erp.domain.ServiceAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceAccountRepository extends JpaRepository<ServiceAccount, Long> {
    Optional<ServiceAccount> findByClientId(String clientId);
    List<ServiceAccount> findByTenantId(String tenantId);
}
