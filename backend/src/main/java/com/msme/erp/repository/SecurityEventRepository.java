package com.msme.erp.repository;

import com.msme.erp.domain.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityEventRepository extends JpaRepository<SecurityEvent, Long> {
    List<SecurityEvent> findByTenantId(String tenantId);
    List<SecurityEvent> findByUserId(String userId);
}
