package com.msme.erp.repository;

import com.msme.erp.domain.TicketMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketMessageRepository extends JpaRepository<TicketMessage, Long> {
    List<TicketMessage> findByTenantIdAndTicketIdOrderByCreatedAtAsc(String tenantId, Long ticketId);
    List<TicketMessage> findByTenantIdAndTicketIdAndVisibilityScopeOrderByCreatedAtAsc(String tenantId, Long ticketId, String visibilityScope);
}
