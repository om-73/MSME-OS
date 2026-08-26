package com.msme.erp.repository;

import com.msme.erp.domain.MaintenanceWindow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceWindowRepository extends JpaRepository<MaintenanceWindow, Long> {
    List<MaintenanceWindow> findByStatus(String status);
}
