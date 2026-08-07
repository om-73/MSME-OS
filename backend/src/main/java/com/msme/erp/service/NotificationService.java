package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.Notification;
import com.msme.erp.dto.CreateNotificationRequest;
import com.msme.erp.dto.NotificationDto;
import com.msme.erp.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationDto> getNotificationsForTenant() {
        String tenantId = TenantContext.getCurrentTenant();
        List<Notification> list = notificationRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public NotificationDto createNotification(CreateNotificationRequest request) {
        String tenantId = TenantContext.getCurrentTenant();
        Notification notification = Notification.builder()
                .tenantId(tenantId)
                .category(request.getCategory())
                .title(request.getTitle())
                .message(request.getMessage())
                .orderId(request.getOrderId())
                .orderNumber(request.getOrderNumber())
                .readStatus(false)
                .build();

        notification = notificationRepository.save(notification);
        return mapToDto(notification);
    }

    @Transactional
    public void markAsRead(String id) {
        Notification n = notificationRepository.findById(id).orElse(null);
        if (n != null) {
            n.setReadStatus(true);
            notificationRepository.save(n);
        }
    }

    @Transactional
    public void markAllAsRead() {
        String tenantId = TenantContext.getCurrentTenant();
        List<Notification> list = notificationRepository.findByTenantIdAndReadStatusFalseOrderByCreatedAtDesc(tenantId);
        for (Notification n : list) {
            n.setReadStatus(true);
        }
        notificationRepository.saveAll(list);
    }

    private NotificationDto mapToDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .category(n.getCategory())
                .title(n.getTitle())
                .message(n.getMessage())
                .orderId(n.getOrderId())
                .orderNumber(n.getOrderNumber())
                .readStatus(n.isReadStatus())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
