package com.msme.erp.repository;

import com.msme.erp.domain.BackupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackupRecordRepository extends JpaRepository<BackupRecord, Long> {
    List<BackupRecord> findByStatus(String status);
    List<BackupRecord> findTop5ByOrderByCreatedAtDesc();
}
