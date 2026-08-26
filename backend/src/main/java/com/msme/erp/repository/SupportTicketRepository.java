package com.msme.erp.repository;

import com.msme.erp.domain.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByTenantIdOrderByCreatedAtDesc(String tenantId);
    List<SupportTicket> findByTenantIdAndClientCodeOrderByCreatedAtDesc(String tenantId, String clientCode);
}
