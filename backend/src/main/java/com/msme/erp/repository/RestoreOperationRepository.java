package com.msme.erp.repository;

import com.msme.erp.domain.RestoreOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestoreOperationRepository extends JpaRepository<RestoreOperation, Long> {
    List<RestoreOperation> findByStatus(String status);
}
