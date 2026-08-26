package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.DispatchRecord;
import com.msme.erp.repository.DispatchRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DispatchService {

    private final DispatchRecordRepository dispatchRecordRepository;
    private final NotificationService notificationService;

    public DispatchService(DispatchRecordRepository dispatchRecordRepository, NotificationService notificationService) {
        this.dispatchRecordRepository = dispatchRecordRepository;
        this.notificationService = notificationService;
    }

    public List<DispatchRecord> getDispatchQueue() {
        return dispatchRecordRepository.findByTenantId(TenantContext.getCurrentTenant());
    }

    @Transactional
    public DispatchRecord assignCourier(Long recordId, String vehicleNo, String courierName, String trackingNumber, String invoiceNumber) {
        DispatchRecord record = dispatchRecordRepository.findById(recordId)
                .orElseThrow(() -> new NoSuchElementException("Dispatch record not found: " + recordId));

        record.setVehicleNo(vehicleNo);
        record.setCourierName(courierName);
        record.setTrackingNumber(trackingNumber);
        record.setInvoiceNumber(invoiceNumber);
        return dispatchRecordRepository.save(record);
    }

    @Transactional
    public DispatchRecord verifyPackage(Long recordId, boolean checklistPassed, boolean barcodeVerified) {
        DispatchRecord record = dispatchRecordRepository.findById(recordId)
                .orElseThrow(() -> new NoSuchElementException("Dispatch record not found: " + recordId));

        record.setChecklistPassed(checklistPassed);
        record.setBarcodeVerified(barcodeVerified);
        return dispatchRecordRepository.save(record);
    }

    @Transactional
    public DispatchRecord shipPackages(Long recordId) {
        DispatchRecord record = dispatchRecordRepository.findById(recordId)
                .orElseThrow(() -> new NoSuchElementException("Dispatch record not found: " + recordId));

        if (!record.isChecklistPassed() || !record.isBarcodeVerified()) {
            throw new IllegalStateException("Cannot ship: packing checks or barcode verification has not been sign-off.");
        }

        record.setStatus("DISPATCHED");
        record = dispatchRecordRepository.save(record);

        // Notify client
        notificationService.createNotification(new com.msme.erp.dto.CreateNotificationRequest(
                com.msme.erp.domain.NotificationCategory.DISPATCH,
                "Order Dispatched",
                "Your shipment for order " + record.getOrderNumber() + " is transit via " + record.getCourierName() + " (Ref: " + record.getTrackingNumber() + ")",
                null,
                record.getOrderNumber()
        ));

        return record;
    }

    @Transactional
    public DispatchRecord deliverConfirm(Long recordId) {
        DispatchRecord record = dispatchRecordRepository.findById(recordId)
                .orElseThrow(() -> new NoSuchElementException("Dispatch record not found: " + recordId));

        record.setStatus("DELIVERED");
        record.setDeliveryConfirmationTime(LocalDateTime.now());
        record = dispatchRecordRepository.save(record);

        // Notify client
        notificationService.createNotification(new com.msme.erp.dto.CreateNotificationRequest(
                com.msme.erp.domain.NotificationCategory.DISPATCH,
                "Order Delivered",
                "Waybill " + record.getOrderNumber() + " has been successfully signed off at destination depot.",
                null,
                record.getOrderNumber()
        ));

        return record;
    }
}
