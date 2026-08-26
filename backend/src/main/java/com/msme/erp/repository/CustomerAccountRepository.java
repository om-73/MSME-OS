package com.msme.erp.repository;

import com.msme.erp.domain.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
    List<CustomerAccount> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<CustomerAccount> findByTenantIdAndClientCode(String tenantId, String clientCode);
}
