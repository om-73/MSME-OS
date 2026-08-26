package com.msme.erp.controller;

import com.msme.erp.domain.NotificationDeliveryLog;
import com.msme.erp.domain.NotificationPreference;
import com.msme.erp.domain.NotificationTemplate;
import com.msme.erp.service.NotificationCenterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notification-center")
public class NotificationCenterController {

    private final NotificationCenterService notificationCenterService;

    public NotificationCenterController(NotificationCenterService notificationCenterService) {
        this.notificationCenterService = notificationCenterService;
    }

    @GetMapping("/inbox")
    public ResponseEntity<List<NotificationDeliveryLog>> getInbox(@RequestParam(required = false, defaultValue = "user@apex.com") String recipientId) {
        return ResponseEntity.ok(notificationCenterService.getInboxForUser(recipientId));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationDeliveryLog> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationCenterService.markAsRead(id));
    }

    @GetMapping("/preferences")
    public ResponseEntity<NotificationPreference> getPreferences(@RequestParam(required = false, defaultValue = "user@apex.com") String userId) {
        return ResponseEntity.ok(notificationCenterService.getUserPreferences(userId));
    }

    @PostMapping("/preferences")
    public ResponseEntity<NotificationPreference> updatePreferences(@RequestParam(required = false, defaultValue = "user@apex.com") String userId, @RequestBody NotificationPreference pref) {
        return ResponseEntity.ok(notificationCenterService.updateUserPreferences(userId, pref));
    }

    @GetMapping("/templates")
    public ResponseEntity<List<NotificationTemplate>> getTemplates() {
        return ResponseEntity.ok(notificationCenterService.getTemplates());
    }

    @PostMapping("/templates")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<NotificationTemplate> saveTemplate(@RequestBody NotificationTemplate template) {
        return ResponseEntity.ok(notificationCenterService.saveTemplate(template));
    }

    @GetMapping("/logs")
    public ResponseEntity<List<NotificationDeliveryLog>> getDeliveryLogs(@RequestParam(required = false) String statusFilter) {
        return ResponseEntity.ok(notificationCenterService.getDeliveryLogs(statusFilter));
    }

    @PostMapping("/logs/{id}/retry")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<NotificationDeliveryLog> retryDelivery(@PathVariable Long id) {
        return ResponseEntity.ok(notificationCenterService.retryDelivery(id));
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        return ResponseEntity.ok(notificationCenterService.getNotificationAnalytics());
    }

    @PostMapping("/events/publish")
    public ResponseEntity<List<NotificationDeliveryLog>> publishEvent(@RequestBody Map<String, Object> payload) {
        String tenantId = (String) payload.get("tenantId");
        String eventType = (String) payload.get("eventType");
        String idempotencyKey = (String) payload.getOrDefault("idempotencyKey", "EVT-" + System.currentTimeMillis());
        String priority = (String) payload.getOrDefault("priority", "NORMAL");
        Map<String, Object> eventData = (Map<String, Object>) payload.getOrDefault("data", payload);

        return ResponseEntity.ok(notificationCenterService.publishEvent(tenantId, eventType, idempotencyKey, priority, eventData));
    }
}
