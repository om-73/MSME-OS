package com.msme.erp.repository;

import com.msme.erp.domain.DataDeletionJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataDeletionJobRepository extends JpaRepository<DataDeletionJob, Long> {
    List<DataDeletionJob> findByTenantId(String tenantId);
}
