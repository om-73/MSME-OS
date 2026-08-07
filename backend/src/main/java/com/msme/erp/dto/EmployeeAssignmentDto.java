package com.msme.erp.dto;

public class EmployeeAssignmentDto {
    private String id;
    private String userId;
    private String userFullName;
    private String departmentId;
    private String departmentName;

    public EmployeeAssignmentDto() {}

    public EmployeeAssignmentDto(String id, String userId, String userFullName, String departmentId, String departmentName) {
        this.id = id;
        this.userId = userId;
        this.userFullName = userFullName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public static EmployeeAssignmentDtoBuilder builder() { return new EmployeeAssignmentDtoBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public static class EmployeeAssignmentDtoBuilder {
        private String id;
        private String userId;
        private String userFullName;
        private String departmentId;
        private String departmentName;

        public EmployeeAssignmentDtoBuilder id(String id) { this.id = id; return this; }
        public EmployeeAssignmentDtoBuilder userId(String userId) { this.userId = userId; return this; }
        public EmployeeAssignmentDtoBuilder userFullName(String userFullName) { this.userFullName = userFullName; return this; }
        public EmployeeAssignmentDtoBuilder departmentId(String departmentId) { this.departmentId = departmentId; return this; }
        public EmployeeAssignmentDtoBuilder departmentName(String departmentName) { this.departmentName = departmentName; return this; }

        public EmployeeAssignmentDto build() {
            return new EmployeeAssignmentDto(id, userId, userFullName, departmentId, departmentName);
        }
    }
}
