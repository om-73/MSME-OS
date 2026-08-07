package com.msme.erp.repository;

import com.msme.erp.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, String> {
    List<Brand> findByTenantId(String tenantId);
}
