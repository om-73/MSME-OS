package com.msme.erp.repository;

import com.msme.erp.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, String> {
    Optional<Tenant> findBySubdomain(String subdomain);
    Optional<Tenant> findByCompanyName(String companyName);
}
