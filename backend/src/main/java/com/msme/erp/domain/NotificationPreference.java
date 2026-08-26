package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_preferences", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "userId"})
})
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String userId;

    private boolean inAppEnabled = true;
    private boolean pushEnabled = true;
    private boolean emailEnabled = true;
    private boolean smsEnabled = false;
    private boolean whatsappEnabled = true;

    private boolean quietHoursEnabled = false;
    private String quietHoursStart = "22:00";
    private String quietHoursEnd = "07:00";

    private boolean bypassQuietHoursForCritical = true;

    private LocalDateTime updatedAt;

    public NotificationPreference() {}

    public NotificationPreference(Long id, String tenantId, String userId, boolean inAppEnabled, boolean pushEnabled, boolean emailEnabled, boolean smsEnabled, boolean whatsappEnabled, boolean quietHoursEnabled, String quietHoursStart, String quietHoursEnd, boolean bypassQuietHoursForCritical, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.inAppEnabled = inAppEnabled;
        this.pushEnabled = pushEnabled;
        this.emailEnabled = emailEnabled;
        this.smsEnabled = smsEnabled;
        this.whatsappEnabled = whatsappEnabled;
        this.quietHoursEnabled = quietHoursEnabled;
        this.quietHoursStart = quietHoursStart;
        this.quietHoursEnd = quietHoursEnd;
        this.bypassQuietHoursForCritical = bypassQuietHoursForCritical;
        this.updatedAt = updatedAt;
    }

    public static NotificationPreferenceBuilder builder() {
        return new NotificationPreferenceBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public boolean isInAppEnabled() { return inAppEnabled; }
    public void setInAppEnabled(boolean inAppEnabled) { this.inAppEnabled = inAppEnabled; }
    public boolean isPushEnabled() { return pushEnabled; }
    public void setPushEnabled(boolean pushEnabled) { this.pushEnabled = pushEnabled; }
    public boolean isEmailEnabled() { return emailEnabled; }
    public void setEmailEnabled(boolean emailEnabled) { this.emailEnabled = emailEnabled; }
    public boolean isSmsEnabled() { return smsEnabled; }
    public void setSmsEnabled(boolean smsEnabled) { this.smsEnabled = smsEnabled; }
    public boolean isWhatsappEnabled() { return whatsappEnabled; }
    public void setWhatsappEnabled(boolean whatsappEnabled) { this.whatsappEnabled = whatsappEnabled; }
    public boolean isQuietHoursEnabled() { return quietHoursEnabled; }
    public void setQuietHoursEnabled(boolean quietHoursEnabled) { this.quietHoursEnabled = quietHoursEnabled; }
    public String getQuietHoursStart() { return quietHoursStart; }
    public void setQuietHoursStart(String quietHoursStart) { this.quietHoursStart = quietHoursStart; }
    public String getQuietHoursEnd() { return quietHoursEnd; }
    public void setQuietHoursEnd(String quietHoursEnd) { this.quietHoursEnd = quietHoursEnd; }
    public boolean isBypassQuietHoursForCritical() { return bypassQuietHoursForCritical; }
    public void setBypassQuietHoursForCritical(boolean bypassQuietHoursForCritical) { this.bypassQuietHoursForCritical = bypassQuietHoursForCritical; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    @PreUpdate
    protected void onSave() {
        updatedAt = LocalDateTime.now();
    }

    public static class NotificationPreferenceBuilder {
        private Long id;
        private String tenantId;
        private String userId;
        private boolean inAppEnabled = true;
        private boolean pushEnabled = true;
        private boolean emailEnabled = true;
        private boolean smsEnabled = false;
        private boolean whatsappEnabled = true;
        private boolean quietHoursEnabled = false;
        private String quietHoursStart = "22:00";
        private String quietHoursEnd = "07:00";
        private boolean bypassQuietHoursForCritical = true;
        private LocalDateTime updatedAt;

        public NotificationPreferenceBuilder id(Long id) { this.id = id; return this; }
        public NotificationPreferenceBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public NotificationPreferenceBuilder userId(String userId) { this.userId = userId; return this; }
        public NotificationPreferenceBuilder inAppEnabled(boolean inAppEnabled) { this.inAppEnabled = inAppEnabled; return this; }
        public NotificationPreferenceBuilder pushEnabled(boolean pushEnabled) { this.pushEnabled = pushEnabled; return this; }
        public NotificationPreferenceBuilder emailEnabled(boolean emailEnabled) { this.emailEnabled = emailEnabled; return this; }
        public NotificationPreferenceBuilder smsEnabled(boolean smsEnabled) { this.smsEnabled = smsEnabled; return this; }
        public NotificationPreferenceBuilder whatsappEnabled(boolean whatsappEnabled) { this.whatsappEnabled = whatsappEnabled; return this; }
        public NotificationPreferenceBuilder quietHoursEnabled(boolean quietHoursEnabled) { this.quietHoursEnabled = quietHoursEnabled; return this; }
        public NotificationPreferenceBuilder quietHoursStart(String quietHoursStart) { this.quietHoursStart = quietHoursStart; return this; }
        public NotificationPreferenceBuilder quietHoursEnd(String quietHoursEnd) { this.quietHoursEnd = quietHoursEnd; return this; }
        public NotificationPreferenceBuilder bypassQuietHoursForCritical(boolean bypassQuietHoursForCritical) { this.bypassQuietHoursForCritical = bypassQuietHoursForCritical; return this; }
        public NotificationPreferenceBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public NotificationPreference build() {
            return new NotificationPreference(id, tenantId, userId, inAppEnabled, pushEnabled, emailEnabled, smsEnabled, whatsappEnabled, quietHoursEnabled, quietHoursStart, quietHoursEnd, bypassQuietHoursForCritical, updatedAt);
        }
    }
}
