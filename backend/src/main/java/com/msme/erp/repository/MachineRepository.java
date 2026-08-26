package com.msme.erp.repository;

import com.msme.erp.domain.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    Optional<Machine> findByTenantIdAndMachineCode(String tenantId, String machineCode);
}
