package com.msme.erp.repository;

import com.msme.erp.domain.TenantInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantInvoiceRepository extends JpaRepository<TenantInvoice, Long> {
    List<TenantInvoice> findByTenantIdOrderByInvoiceDateDesc(String tenantId);
}
