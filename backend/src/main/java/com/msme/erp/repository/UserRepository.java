package com.msme.erp.repository;

import com.msme.erp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByTenantId(String tenantId);
    boolean existsByEmail(String email);
}
