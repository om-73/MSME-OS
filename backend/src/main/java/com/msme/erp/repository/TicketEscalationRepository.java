package com.msme.erp.repository;

import com.msme.erp.domain.TicketEscalation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketEscalationRepository extends JpaRepository<TicketEscalation, Long> {
    List<TicketEscalation> findByTenantId(String tenantId);
    List<TicketEscalation> findByTenantIdAndTicketId(String tenantId, Long ticketId);
}
