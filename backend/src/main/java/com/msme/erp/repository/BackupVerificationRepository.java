package com.msme.erp.repository;

import com.msme.erp.domain.BackupVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackupVerificationRepository extends JpaRepository<BackupVerification, Long> {
    List<BackupVerification> findByBackupRecordId(Long backupRecordId);
}
