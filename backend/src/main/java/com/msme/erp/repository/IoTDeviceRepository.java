package com.msme.erp.repository;

import com.msme.erp.domain.IoTDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IoTDeviceRepository extends JpaRepository<IoTDevice, Long> {
    List<IoTDevice> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<IoTDevice> findByDeviceId(String deviceId);
}
