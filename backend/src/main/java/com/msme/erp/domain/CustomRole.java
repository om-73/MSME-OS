package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "custom_roles")
public class CustomRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 2000, nullable = false)
    private String permissions; // e.g. orders:view,production:update,inventory:issue,qc:approve,billing:view

    private boolean isSystemRole = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CustomRole() {}

    public CustomRole(Long id, String tenantId, String name, String description, String permissions, boolean isSystemRole, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.isSystemRole = isSystemRole;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CustomRoleBuilder builder() {
        return new CustomRoleBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
    public boolean isSystemRole() { return isSystemRole; }
    public void setSystemRole(boolean systemRole) { isSystemRole = systemRole; }
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

    public static class CustomRoleBuilder {
        private Long id;
        private String tenantId;
        private String name;
        private String description;
        private String permissions;
        private boolean isSystemRole = false;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CustomRoleBuilder id(Long id) { this.id = id; return this; }
        public CustomRoleBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public CustomRoleBuilder name(String name) { this.name = name; return this; }
        public CustomRoleBuilder description(String description) { this.description = description; return this; }
        public CustomRoleBuilder permissions(String permissions) { this.permissions = permissions; return this; }
        public CustomRoleBuilder isSystemRole(boolean isSystemRole) { this.isSystemRole = isSystemRole; return this; }
        public CustomRoleBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CustomRoleBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public CustomRole build() {
            return new CustomRole(id, tenantId, name, description, permissions, isSystemRole, createdAt, updatedAt);
        }
    }
}
