package com.msme.erp.dto;

public class LoginResponse {
    private String token;
    private String userId;
    private String email;
    private String fullName;
    private String role;
    private String tenantId;
    private String tenantName;
    private String brandId;

    public LoginResponse() {}

    public LoginResponse(String token, String userId, String email, String fullName, String role, String tenantId, String tenantName, String brandId) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.brandId = brandId;
    }

    public static LoginResponseBuilder builder() { return new LoginResponseBuilder(); }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
    public String getBrandId() { return brandId; }
    public void setBrandId(String brandId) { this.brandId = brandId; }

    public static class LoginResponseBuilder {
        private String token;
        private String userId;
        private String email;
        private String fullName;
        private String role;
        private String tenantId;
        private String tenantName;
        private String brandId;

        public LoginResponseBuilder token(String token) { this.token = token; return this; }
        public LoginResponseBuilder userId(String userId) { this.userId = userId; return this; }
        public LoginResponseBuilder email(String email) { this.email = email; return this; }
        public LoginResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public LoginResponseBuilder role(String role) { this.role = role; return this; }
        public LoginResponseBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public LoginResponseBuilder tenantName(String tenantName) { this.tenantName = tenantName; return this; }
        public LoginResponseBuilder brandId(String brandId) { this.brandId = brandId; return this; }

        public LoginResponse build() {
            return new LoginResponse(token, userId, email, fullName, role, tenantId, tenantName, brandId);
        }
    }
}
