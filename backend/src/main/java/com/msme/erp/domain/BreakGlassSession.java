package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "break_glass_sessions")
public class BreakGlassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String actorId;

    @Column(length = 1000, nullable = false)
    private String emergencyReason;

    private LocalDateTime expiresAt;
    private boolean active = true;

    private LocalDateTime createdAt;

    public BreakGlassSession() {}

    public BreakGlassSession(Long id, String tenantId, String actorId, String emergencyReason, LocalDateTime expiresAt, boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.actorId = actorId;
        this.emergencyReason = emergencyReason;
        this.expiresAt = expiresAt;
        this.active = active;
        this.createdAt = createdAt;
    }

    public static BreakGlassSessionBuilder builder() {
        return new BreakGlassSessionBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getActorId() { return actorId; }
    public void setActorId(String actorId) { this.actorId = actorId; }
    public String getEmergencyReason() { return emergencyReason; }
    public void setEmergencyReason(String emergencyReason) { this.emergencyReason = emergencyReason; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (expiresAt == null) expiresAt = LocalDateTime.now().plusHours(2);
    }

    public static class BreakGlassSessionBuilder {
        private Long id;
        private String tenantId;
        private String actorId;
        private String emergencyReason;
        private LocalDateTime expiresAt;
        private boolean active = true;
        private LocalDateTime createdAt;

        public BreakGlassSessionBuilder id(Long id) { this.id = id; return this; }
        public BreakGlassSessionBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public BreakGlassSessionBuilder actorId(String actorId) { this.actorId = actorId; return this; }
        public BreakGlassSessionBuilder emergencyReason(String emergencyReason) { this.emergencyReason = emergencyReason; return this; }
        public BreakGlassSessionBuilder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public BreakGlassSessionBuilder active(boolean active) { this.active = active; return this; }
        public BreakGlassSessionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public BreakGlassSession build() {
            return new BreakGlassSession(id, tenantId, actorId, emergencyReason, expiresAt, active, createdAt);
        }
    }
}
