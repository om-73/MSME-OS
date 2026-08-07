package com.msme.erp.repository;

import com.msme.erp.domain.ProductionPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductionPhotoRepository extends JpaRepository<ProductionPhoto, String> {
    List<ProductionPhoto> findByTenantIdAndOrderIdOrderByCreatedAtDesc(String tenantId, String orderId);
}
