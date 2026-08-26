package com.msme.erp.repository;

import com.msme.erp.domain.DataExportJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataExportJobRepository extends JpaRepository<DataExportJob, Long> {
    List<DataExportJob> findByTenantId(String tenantId);
}
