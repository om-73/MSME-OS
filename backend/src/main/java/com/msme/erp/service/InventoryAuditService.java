package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.InventoryAudit;
import com.msme.erp.domain.InventoryAuditItem;
import com.msme.erp.domain.InventoryItem;
import com.msme.erp.dto.InventoryAuditDto;
import com.msme.erp.repository.InventoryAuditItemRepository;
import com.msme.erp.repository.InventoryAuditRepository;
import com.msme.erp.repository.InventoryItemRepository;
import com.msme.erp.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryAuditService {

    private final InventoryAuditRepository auditRepository;
    private final InventoryAuditItemRepository auditItemRepository;
    private final InventoryItemRepository itemRepository;
    private final InventoryService inventoryService;

    public InventoryAuditService(InventoryAuditRepository auditRepository, 
                                 InventoryAuditItemRepository auditItemRepository, 
                                 InventoryItemRepository itemRepository,
                                 InventoryService inventoryService) {
        this.auditRepository = auditRepository;
        this.auditItemRepository = auditItemRepository;
        this.itemRepository = itemRepository;
        this.inventoryService = inventoryService;
    }

    public List<InventoryAuditDto> getAudits() {
        String tenantId = TenantContext.getCurrentTenant();
        return auditRepository.findByTenantIdOrderByCreatedAtDesc(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public InventoryAuditDto getAuditDetails(String auditId) {
        InventoryAudit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new NoSuchElementException("Audit session not found"));
        return mapToDto(audit);
    }

    @Transactional
    public InventoryAuditDto startAudit(String auditName) {
        String tenantId = TenantContext.getCurrentTenant();
        String operator = getCurrentUserFullName();

        // 1. Create Audit Root
        InventoryAudit audit = InventoryAudit.builder()
                .tenantId(tenantId)
                .auditName(auditName)
                .status("DRAFT")
                .createdBy(operator)
                .build();
        audit = auditRepository.save(audit);

        // 2. Snapshot current system stock levels for all inventory items
        List<InventoryItem> items = itemRepository.findByTenantId(tenantId);
        for (InventoryItem i : items) {
            auditItemRepository.save(InventoryAuditItem.builder()
                    .auditId(audit.getId())
                    .inventoryItemId(i.getId())
                    .systemStock(i.getCurrentStock())
                    .physicalStock(i.getCurrentStock()) // default physical matches system initially
                    .variance(0.0)
                    .reconciled(false)
                    .build());
        }

        return mapToDto(audit);
    }

    @Transactional
    public InventoryAuditDto submitCounts(String auditId, Map<String, Double> physicalCounts) {
        InventoryAudit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new NoSuchElementException("Audit session not found"));

        if ("COMPLETED".equals(audit.getStatus())) {
            throw new IllegalStateException("Cannot edit a completed audit session");
        }

        List<InventoryAuditItem> auditItems = auditItemRepository.findByAuditId(auditId);
        for (InventoryAuditItem ai : auditItems) {
            if (physicalCounts.containsKey(ai.getInventoryItemId())) {
                double phys = physicalCounts.get(ai.getInventoryItemId());
                ai.setPhysicalStock(phys);
                ai.setVariance(phys - ai.getSystemStock());
                auditItemRepository.save(ai);
            }
        }

        return mapToDto(audit);
    }

    @Transactional
    public InventoryAuditDto reconcileAudit(String auditId) {
        InventoryAudit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new NoSuchElementException("Audit session not found"));

        if ("COMPLETED".equals(audit.getStatus())) {
            throw new IllegalStateException("Audit session is already reconciled and completed");
        }

        List<InventoryAuditItem> auditItems = auditItemRepository.findByAuditId(auditId);
        for (InventoryAuditItem ai : auditItems) {
            double var = ai.getVariance();
            if (var != 0.0) {
                // Post inventory adjustment ledger entry to align system stock with physical count
                String dir = var > 0 ? "RECONCILE_IN" : "RECONCILE_OUT";
                inventoryService.adjustStock(
                        ai.getInventoryItemId(), 
                        Math.abs(var), 
                        "ADJUSTMENT", 
                        "Physical count alignment reconciliation for: " + audit.getAuditName()
                );
            }
            ai.setReconciled(true);
            ai.setReconciliationNotes("Reconciled by physical stocktake audit signoff.");
            auditItemRepository.save(ai);
        }

        audit.setStatus("COMPLETED");
        audit.setCompletedAt(LocalDateTime.now());
        auditRepository.save(audit);

        return mapToDto(audit);
    }

    private InventoryAuditDto mapToDto(InventoryAudit audit) {
        List<InventoryAuditDto.AuditItemDetail> list = new ArrayList<>();
        List<InventoryAuditItem> auditItems = auditItemRepository.findByAuditId(audit.getId());

        for (InventoryAuditItem ai : auditItems) {
            String name = "Unknown";
            String code = "N/A";
            Optional<InventoryItem> itemOpt = itemRepository.findById(ai.getInventoryItemId());
            if (itemOpt.isPresent()) {
                name = itemOpt.get().getName();
                code = itemOpt.get().getCode();
            }

            list.add(new InventoryAuditDto.AuditItemDetail(
                    ai.getId(), ai.getInventoryItemId(), name, code,
                    ai.getSystemStock(), ai.getPhysicalStock(), ai.getVariance(),
                    ai.getReconciled(), ai.getReconciliationNotes()
            ));
        }

        return new InventoryAuditDto(
                audit.getId(), audit.getAuditName(), audit.getStatus(),
                audit.getCreatedBy(), audit.getCreatedAt(), audit.getCompletedAt(), list
        );
    }

    private String getCurrentUserFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            return p.getFullName();
        }
        return "System Auditor";
    }
}
