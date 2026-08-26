package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_templates")
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String eventType; // e.g. STAGE_COMPLETED, QC_FAILED, LOW_STOCK, SHIPMENT_DISPATCHED

    @Column(nullable = false)
    private String channel; // IN_APP, PUSH, EMAIL, SMS, WHATSAPP, WEBHOOK

    @Column(nullable = false)
    private String subjectTemplate;

    @Column(length = 2000, nullable = false)
    private String bodyTemplate; // e.g. "Order {{orderNumber}} has completed {{stageName}}."

    private boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NotificationTemplate() {}

    public NotificationTemplate(Long id, String tenantId, String eventType, String channel, String subjectTemplate, String bodyTemplate, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.eventType = eventType;
        this.channel = channel;
        this.subjectTemplate = subjectTemplate;
        this.bodyTemplate = bodyTemplate;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static NotificationTemplateBuilder builder() {
        return new NotificationTemplateBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public String getSubjectTemplate() { return subjectTemplate; }
    public void setSubjectTemplate(String subjectTemplate) { this.subjectTemplate = subjectTemplate; }
    public String getBodyTemplate() { return bodyTemplate; }
    public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static class NotificationTemplateBuilder {
        private Long id;
        private String tenantId;
        private String eventType;
        private String channel;
        private String subjectTemplate;
        private String bodyTemplate;
        private boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public NotificationTemplateBuilder id(Long id) { this.id = id; return this; }
        public NotificationTemplateBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public NotificationTemplateBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public NotificationTemplateBuilder channel(String channel) { this.channel = channel; return this; }
        public NotificationTemplateBuilder subjectTemplate(String subjectTemplate) { this.subjectTemplate = subjectTemplate; return this; }
        public NotificationTemplateBuilder bodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; return this; }
        public NotificationTemplateBuilder active(boolean active) { this.active = active; return this; }
        public NotificationTemplateBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public NotificationTemplateBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public NotificationTemplate build() {
            return new NotificationTemplate(id, tenantId, eventType, channel, subjectTemplate, bodyTemplate, active, createdAt, updatedAt);
        }
    }
}
