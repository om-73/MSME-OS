package com.msme.erp.repository;

import com.msme.erp.domain.MachineMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineMaintenanceRepository extends JpaRepository<MachineMaintenance, Long> {
    List<MachineMaintenance> findByTenantIdAndMachineIdOrderByScheduledAtDesc(String tenantId, Long machineId);
    List<MachineMaintenance> findByTenantIdOrderByScheduledAtDesc(String tenantId);
}
