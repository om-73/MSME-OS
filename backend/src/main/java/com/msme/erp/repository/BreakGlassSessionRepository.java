package com.msme.erp.repository;

import com.msme.erp.domain.BreakGlassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreakGlassSessionRepository extends JpaRepository<BreakGlassSession, Long> {
    List<BreakGlassSession> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}
