package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.InventoryItem;
import com.msme.erp.domain.InventoryMovement;
import com.msme.erp.domain.Brand;
import com.msme.erp.dto.InventoryItemDto;
import com.msme.erp.dto.InventoryMovementDto;
import com.msme.erp.repository.InventoryItemRepository;
import com.msme.erp.repository.InventoryMovementRepository;
import com.msme.erp.repository.BrandRepository;
import com.msme.erp.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryItemRepository itemRepository;
    private final InventoryMovementRepository movementRepository;
    private final BrandRepository brandRepository;

    public InventoryService(InventoryItemRepository itemRepository, 
                            InventoryMovementRepository movementRepository, 
                            BrandRepository brandRepository) {
        this.itemRepository = itemRepository;
        this.movementRepository = movementRepository;
        this.brandRepository = brandRepository;
    }

    public List<InventoryItemDto> getAllInventory() {
        String tenantId = TenantContext.getCurrentTenant();
        return itemRepository.findByTenantId(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<InventoryItemDto> getInventoryByCategory(String category) {
        String tenantId = TenantContext.getCurrentTenant();
        return itemRepository.findByTenantIdAndCategory(tenantId, category).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<InventoryMovementDto> getLedger() {
        String tenantId = TenantContext.getCurrentTenant();
        return movementRepository.findByTenantIdOrderByTimestampDesc(tenantId).stream()
                .map(this::mapMovementToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InventoryItemDto receiveStock(InventoryItemDto request) {
        String tenantId = TenantContext.getCurrentTenant();
        String operator = getCurrentUserFullName();

        // Check if item code already exists for tenant
        Optional<InventoryItem> existingOpt = itemRepository.findByTenantIdAndCode(tenantId, request.getCode());
        InventoryItem item;

        if (existingOpt.isPresent()) {
            item = existingOpt.get();
            item.setCurrentStock(item.getCurrentStock() + request.getCurrentStock());
            item = itemRepository.save(item);
        } else {
            item = InventoryItem.builder()
                    .tenantId(tenantId)
                    .name(request.getName())
                    .code(request.getCode())
                    .sku(request.getSku())
                    .barcode(request.getBarcode() != null ? request.getBarcode() : "BC-" + System.currentTimeMillis())
                    .category(request.getCategory())
                    .supplierName(request.getSupplierName())
                    .unit(request.getUnit() != null ? request.getUnit() : "pcs")
                    .purchasePrice(request.getPurchasePrice() != null ? request.getPurchasePrice() : 0.0)
                    .currentStock(request.getCurrentStock())
                    .reservedStock(0.0)
                    .warehouseName(request.getWarehouseName())
                    .rackLocation(request.getRackLocation())
                    .batchNumber(request.getBatchNumber())
                    .expiryDate(request.getExpiryDate())
                    .safetyStock(request.getSafetyStock() != null ? request.getSafetyStock() : 10.0)
                    .minStockAlert(request.getMinStockAlert() != null ? request.getMinStockAlert() : 5.0)
                    .maxStockAlert(request.getMaxStockAlert() != null ? request.getMaxStockAlert() : 500.0)
                    .clientBrandId(request.getClientBrandId())
                    .build();
            item = itemRepository.save(item);
        }

        movementRepository.save(InventoryMovement.builder()
                .tenantId(tenantId)
                .inventoryItemId(item.getId())
                .movementType("RECEIVE")
                .quantity(request.getCurrentStock())
                .toWarehouse(item.getWarehouseName())
                .operatorName(operator)
                .remarks("Goods Receipt Note (GRN) logged via Inventory Master")
                .build());

        return mapToDto(item);
    }

    @Transactional
    public void issueStock(String itemId, Double quantity, String orderId, String remarks) {
        String tenantId = TenantContext.getCurrentTenant();
        String operator = getCurrentUserFullName();

        InventoryItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Inventory item not found: " + itemId));

        if (item.getAvailableStock() < quantity) {
            throw new IllegalArgumentException("Insufficient available stock for material: " + item.getName());
        }

        item.setCurrentStock(item.getCurrentStock() - quantity);
        itemRepository.save(item);

        movementRepository.save(InventoryMovement.builder()
                .tenantId(tenantId)
                .inventoryItemId(item.getId())
                .movementType("ISSUE")
                .quantity(quantity)
                .fromWarehouse(item.getWarehouseName())
                .orderId(orderId)
                .operatorName(operator)
                .remarks(remarks != null ? remarks : "Issued material for production batch run")
                .build());
    }

    @Transactional
    public InventoryItemDto adjustStock(String id, Double quantity, String movementType, String remarks) {
        String tenantId = TenantContext.getCurrentTenant();
        String operator = getCurrentUserFullName();

        InventoryItem item = itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inventory item not found"));

        if ("SCRAP".equalsIgnoreCase(movementType) || "CONSUME".equalsIgnoreCase(movementType) || "ISSUE".equalsIgnoreCase(movementType)) {
            item.setCurrentStock(Math.max(0, item.getCurrentStock() - quantity));
        } else {
            item.setCurrentStock(item.getCurrentStock() + quantity);
        }

        item = itemRepository.save(item);

        movementRepository.save(InventoryMovement.builder()
                .tenantId(tenantId)
                .inventoryItemId(item.getId())
                .movementType(movementType)
                .quantity(quantity)
                .operatorName(operator)
                .remarks(remarks)
                .build());

        return mapToDto(item);
    }

    @Transactional
    public void reserveStock(String itemId, Double quantity) {
        InventoryItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Inventory item not found: " + itemId));

        if (item.getAvailableStock() < quantity) {
            throw new IllegalArgumentException("Insufficient available stock to reserve: " + item.getName());
        }

        item.setReservedStock(item.getReservedStock() + quantity);
        itemRepository.save(item);
    }

    @Transactional
    public void releaseReservation(String itemId, Double quantity) {
        InventoryItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Inventory item not found: " + itemId));

        item.setReservedStock(Math.max(0.0, item.getReservedStock() - quantity));
        itemRepository.save(item);
    }

    public List<InventoryItemDto> getShortageReport() {
        String tenantId = TenantContext.getCurrentTenant();
        return itemRepository.findByTenantId(tenantId).stream()
                .filter(i -> i.getCurrentStock() <= i.getSafetyStock())
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private InventoryItemDto mapToDto(InventoryItem item) {
        String brandName = null;
        if (item.getClientBrandId() != null) {
            brandName = brandRepository.findById(item.getClientBrandId()).map(Brand::getName).orElse(null);
        }

        boolean isLow = item.getCurrentStock() <= item.getSafetyStock();

        return new InventoryItemDto(
                item.getId(), item.getName(), item.getCode(), item.getSku(), item.getBarcode(),
                item.getCategory(), item.getSupplierName(), item.getUnit(), item.getPurchasePrice(),
                item.getCurrentStock(), item.getReservedStock(), item.getAvailableStock(),
                item.getWarehouseName(), item.getRackLocation(), item.getBatchNumber(),
                item.getExpiryDate(), item.getSafetyStock(), item.getMinStockAlert(),
                item.getMaxStockAlert(), item.getClientBrandId(), brandName, isLow
        );
    }

    private InventoryMovementDto mapMovementToDto(InventoryMovement m) {
        String name = "Unknown";
        String code = "N/A";
        Optional<InventoryItem> itemOpt = itemRepository.findById(m.getInventoryItemId());
        if (itemOpt.isPresent()) {
            name = itemOpt.get().getName();
            code = itemOpt.get().getCode();
        }

        return new InventoryMovementDto(
                m.getId(), m.getInventoryItemId(), name, code, m.getMovementType(),
                m.getQuantity(), m.getFromWarehouse(), m.getToWarehouse(), m.getOrderId(),
                m.getReferenceNumber(), m.getOperatorName(), m.getRemarks(), m.getTimestamp()
        );
    }

    private String getCurrentUserFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            return p.getFullName();
        }
        return "System Storekeeper";
    }
}
