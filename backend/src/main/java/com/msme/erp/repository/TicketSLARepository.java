package com.msme.erp.repository;

import com.msme.erp.domain.TicketSLA;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TicketSLARepository extends JpaRepository<TicketSLA, Long> {
    List<TicketSLA> findByTenantId(String tenantId);
    Optional<TicketSLA> findByTenantIdAndPriority(String tenantId, String priority);
}
