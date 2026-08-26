package com.msme.erp.repository;

import com.msme.erp.domain.MachineTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineTelemetryRepository extends JpaRepository<MachineTelemetry, Long> {
    List<MachineTelemetry> findByTenantIdAndMachineIdOrderByTimestampDesc(String tenantId, Long machineId);
}
