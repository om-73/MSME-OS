package com.msme.erp.repository;

import com.msme.erp.domain.ClientDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientDocumentRepository extends JpaRepository<ClientDocument, String> {
    List<ClientDocument> findByTenantIdAndOrderId(String tenantId, String orderId);
    List<ClientDocument> findByTenantIdAndOrderIdAndType(String tenantId, String orderId, String type);
}
