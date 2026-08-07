package com.msme.erp.repository;

import com.msme.erp.domain.BrandChat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BrandChatRepository extends JpaRepository<BrandChat, String> {
    List<BrandChat> findByTenantIdAndBrandIdOrderByTimestampAsc(String tenantId, String brandId);
}
