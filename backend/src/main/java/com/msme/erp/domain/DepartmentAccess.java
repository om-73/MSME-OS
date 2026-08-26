package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "department_access", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "userId", "departmentName"})
})
public class DepartmentAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String departmentName; // Cutting, Stitching, QC, Warehouse, Dispatch, Finance

    private String accessLevel = "READ_WRITE"; // READ_ONLY, READ_WRITE, ADMIN

    private LocalDateTime grantedAt;

    public DepartmentAccess() {}

    public DepartmentAccess(Long id, String tenantId, String userId, String departmentName, String accessLevel, LocalDateTime grantedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.departmentName = departmentName;
        this.accessLevel = accessLevel;
        this.grantedAt = grantedAt;
    }

    public static DepartmentAccessBuilder builder() {
        return new DepartmentAccessBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
    public LocalDateTime getGrantedAt() { return grantedAt; }
    public void setGrantedAt(LocalDateTime grantedAt) { this.grantedAt = grantedAt; }

    @PrePersist
    protected void onCreate() {
        grantedAt = LocalDateTime.now();
    }

    public static class DepartmentAccessBuilder {
        private Long id;
        private String tenantId;
        private String userId;
        private String departmentName;
        private String accessLevel = "READ_WRITE";
        private LocalDateTime grantedAt;

        public DepartmentAccessBuilder id(Long id) { this.id = id; return this; }
        public DepartmentAccessBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public DepartmentAccessBuilder userId(String userId) { this.userId = userId; return this; }
        public DepartmentAccessBuilder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public DepartmentAccessBuilder accessLevel(String accessLevel) { this.accessLevel = accessLevel; return this; }
        public DepartmentAccessBuilder grantedAt(LocalDateTime grantedAt) { this.grantedAt = grantedAt; return this; }

        public DepartmentAccess build() {
            return new DepartmentAccess(id, tenantId, userId, departmentName, accessLevel, grantedAt);
        }
    }
}
