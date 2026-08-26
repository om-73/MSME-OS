package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProcurementService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final VendorRepository vendorRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final NotificationService notificationService;

    public ProcurementService(PurchaseOrderRepository purchaseOrderRepository,
                              VendorRepository vendorRepository,
                              InventoryItemRepository inventoryItemRepository,
                              InventoryMovementRepository inventoryMovementRepository,
                              NotificationService notificationService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.vendorRepository = vendorRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryMovementRepository = inventoryMovementRepository;
        this.notificationService = notificationService;
    }

    public List<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrderRepository.findByTenantId(TenantContext.getCurrentTenant());
    }

    @Transactional
    public PurchaseOrder createPurchaseOrder(String vendorName, List<Map<String, Object>> itemPayloads) {
        String tenantId = TenantContext.getCurrentTenant();
        String poNumber = "PO-2026-" + (1000 + new Random().nextInt(9000));

        double totalAmount = 0.0;
        List<PurchaseOrderItem> items = new ArrayList<>();

        for (Map<String, Object> it : itemPayloads) {
            String materialId = (String) it.get("materialId");
            String materialName = (String) it.get("materialName");
            double quantityOrdered = Double.parseDouble(it.get("quantityOrdered").toString());
            double unitPrice = Double.parseDouble(it.get("unitPrice").toString());

            totalAmount += (quantityOrdered * unitPrice);

            items.add(PurchaseOrderItem.builder()
                    .materialId(materialId)
                    .materialName(materialName)
                    .quantityOrdered(quantityOrdered)
                    .quantityReceived(0.0)
                    .unitPrice(unitPrice)
                    .build());
        }

        PurchaseOrder po = PurchaseOrder.builder()
                .tenantId(tenantId)
                .poNumber(poNumber)
                .vendorName(vendorName)
                .status("PENDING_APPROVAL")
                .totalAmount(totalAmount)
                .items(items)
                .build();

        po = purchaseOrderRepository.save(po);

        notificationService.createNotification(new com.msme.erp.dto.CreateNotificationRequest(
                com.msme.erp.domain.NotificationCategory.MATERIAL_SHORTAGE,
                "Purchase Order Created",
                "New Purchase Order " + poNumber + " created for " + vendorName + ". Pending approval.",
                null,
                poNumber
        ));

        return po;
    }

    @Transactional
    public PurchaseOrder approvePurchaseOrder(Long poId) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new NoSuchElementException("Purchase Order not found: " + poId));

        po.setStatus("APPROVED");
        po = purchaseOrderRepository.save(po);

        notificationService.createNotification(new com.msme.erp.dto.CreateNotificationRequest(
                com.msme.erp.domain.NotificationCategory.MATERIAL_SHORTAGE,
                "Purchase Order Approved",
                "Purchase Order " + po.getPoNumber() + " approved. Ready for sourcing receiving.",
                null,
                po.getPoNumber()
        ));

        return po;
    }

    @Transactional
    public PurchaseOrder receiveStockPartial(Long poId, Map<String, Double> itemReceipts, String invoiceNumber) {
        String tenantId = TenantContext.getCurrentTenant();
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new NoSuchElementException("Purchase Order not found: " + poId));

        boolean allReceived = true;
        double receivedValueThisTime = 0.0;

        for (PurchaseOrderItem item : po.getItems()) {
            Double incomingQty = itemReceipts.get(item.getMaterialId());
            if (incomingQty == null) incomingQty = 0.0;

            if (incomingQty > 0) {
                item.setQuantityReceived(item.getQuantityReceived() + incomingQty);
                receivedValueThisTime += (incomingQty * item.getUnitPrice());

                // Update physical InventoryItem stock
                InventoryItem invItem = inventoryItemRepository.findById(item.getMaterialId()).orElse(null);
                if (invItem != null) {
                    double prevStock = invItem.getCurrentStock();
                    invItem.setCurrentStock(invItem.getCurrentStock() + incomingQty);
                    inventoryItemRepository.save(invItem);

                    // Safety alert checking
                    if (invItem.getCurrentStock() < invItem.getSafetyStock()) {
                        notificationService.createNotification(new com.msme.erp.dto.CreateNotificationRequest(
                                com.msme.erp.domain.NotificationCategory.MATERIAL_SHORTAGE,
                                "Low Stock",
                                "Material " + invItem.getName() + " is below safety stock threshold.",
                                invItem.getId(),
                                invItem.getCode()
                        ));
                    }

                    // Log InventoryMovement log
                    inventoryMovementRepository.save(InventoryMovement.builder()
                            .tenantId(tenantId)
                            .inventoryItemId(invItem.getId())
                            .movementType("RECEIVE")
                            .quantity(incomingQty)
                            .toWarehouse(invItem.getWarehouseName())
                            .referenceNumber(po.getPoNumber())
                            .operatorName("Procurement Manager")
                            .remarks("GRN Intake via PO " + po.getPoNumber())
                            .build());
                }
            }

            if (item.getQuantityReceived() < item.getQuantityOrdered()) {
                allReceived = false;
            }
        }

        po.setStatus(allReceived ? "COMPLETED" : "PARTIAL_RECEIVED");
        po.setInvoiceNumber(invoiceNumber);
        final PurchaseOrder savedPo = purchaseOrderRepository.save(po);

        // Update Supplier outstanding balance ledger
        List<Vendor> vendors = vendorRepository.findByTenantId(tenantId);
        Optional<Vendor> activeVendor = vendors.stream()
                .filter(v -> v.getName().equalsIgnoreCase(savedPo.getVendorName()))
                .findFirst();

        if (activeVendor.isPresent()) {
            Vendor v = activeVendor.get();
            v.setOutstandingBalance(v.getOutstandingBalance() + receivedValueThisTime);
            vendorRepository.save(v);
        }

        notificationService.createNotification(new com.msme.erp.dto.CreateNotificationRequest(
                com.msme.erp.domain.NotificationCategory.MATERIAL_SHORTAGE,
                "Material Received",
                "GRN Logged for PO " + savedPo.getPoNumber() + ". Status: " + savedPo.getStatus(),
                null,
                savedPo.getPoNumber()
        ));

        return savedPo;
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findByTenantId(TenantContext.getCurrentTenant());
    }

    @Transactional
    public Vendor createVendor(String name, String code, String email, String phone, String address) {
        String tenantId = TenantContext.getCurrentTenant();
        Vendor v = Vendor.builder()
                .tenantId(tenantId)
                .name(name)
                .code(code)
                .email(email)
                .phone(phone)
                .address(address)
                .outstandingBalance(0.0)
                .build();
        return vendorRepository.save(v);
    }
}
