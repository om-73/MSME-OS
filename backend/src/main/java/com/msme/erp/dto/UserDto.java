package com.msme.erp.dto;

public class UserDto {
    private String id;
    private String email;
    private String fullName;
    private String role;
    private String tenantId;
    private String brandId;
    private String brandName;

    public UserDto() {}

    public UserDto(String id, String email, String fullName, String role, String tenantId, String brandId, String brandName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.tenantId = tenantId;
        this.brandId = brandId;
        this.brandName = brandName;
    }

    public static UserDtoBuilder builder() { return new UserDtoBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getBrandId() { return brandId; }
    public void setBrandId(String brandId) { this.brandId = brandId; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public static class UserDtoBuilder {
        private String id;
        private String email;
        private String fullName;
        private String role;
        private String tenantId;
        private String brandId;
        private String brandName;

        public UserDtoBuilder id(String id) { this.id = id; return this; }
        public UserDtoBuilder email(String email) { this.email = email; return this; }
        public UserDtoBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserDtoBuilder role(String role) { this.role = role; return this; }
        public UserDtoBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public UserDtoBuilder brandId(String brandId) { this.brandId = brandId; return this; }
        public UserDtoBuilder brandName(String brandName) { this.brandName = brandName; return this; }

        public UserDto build() {
            return new UserDto(id, email, fullName, role, tenantId, brandId, brandName);
        }
    }
}
