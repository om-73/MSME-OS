package com.msme.erp.repository;

import com.msme.erp.domain.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    List<UserSession> findByTenantIdAndUserId(String tenantId, String userId);
    List<UserSession> findByTenantIdAndStatus(String tenantId, String status);
}
