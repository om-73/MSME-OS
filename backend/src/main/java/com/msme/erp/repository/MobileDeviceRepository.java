package com.msme.erp.repository;

import com.msme.erp.domain.MobileDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MobileDeviceRepository extends JpaRepository<MobileDevice, Long> {
    List<MobileDevice> findByTenantIdAndUserId(String tenantId, String userId);
    Optional<MobileDevice> findByDeviceId(String deviceId);
}
