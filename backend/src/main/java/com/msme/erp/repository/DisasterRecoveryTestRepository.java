package com.msme.erp.repository;

import com.msme.erp.domain.DisasterRecoveryTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisasterRecoveryTestRepository extends JpaRepository<DisasterRecoveryTest, Long> {
    List<DisasterRecoveryTest> findTop5ByOrderByTestDateDesc();
}
