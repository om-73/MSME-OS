package com.msme.erp.repository;

import com.msme.erp.domain.MachineDowntime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineDowntimeRepository extends JpaRepository<MachineDowntime, Long> {
    List<MachineDowntime> findByTenantIdAndMachineIdOrderByStartTimeDesc(String tenantId, Long machineId);
    List<MachineDowntime> findByTenantIdOrderByStartTimeDesc(String tenantId);
}
