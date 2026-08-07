package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_assignments", indexes = {
    @Index(name = "idx_emp_assign_tenant", columnList = "tenantId"),
    @Index(name = "idx_emp_assign_user", columnList = "userId"),
    @Index(name = "idx_emp_assign_dept", columnList = "departmentId")
})
public class EmployeeAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String departmentId;

    private LocalDateTime assignedAt;

    public EmployeeAssignment() {}

    public EmployeeAssignment(String id, String tenantId, String userId, String departmentId, LocalDateTime assignedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.departmentId = departmentId;
        this.assignedAt = assignedAt;
    }

    public static EmployeeAssignmentBuilder builder() { return new EmployeeAssignmentBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }

    public static class EmployeeAssignmentBuilder {
        private String id;
        private String tenantId;
        private String userId;
        private String departmentId;

        public EmployeeAssignmentBuilder id(String id) { this.id = id; return this; }
        public EmployeeAssignmentBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public EmployeeAssignmentBuilder userId(String userId) { this.userId = userId; return this; }
        public EmployeeAssignmentBuilder departmentId(String departmentId) { this.departmentId = departmentId; return this; }

        public EmployeeAssignment build() {
            return new EmployeeAssignment(id, tenantId, userId, departmentId, null);
        }
    }
}
