package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    private String email;
    private String phone;
    private String address;

    @Column(nullable = false)
    private Double outstandingBalance = 0.0;

    private LocalDateTime createdAt;

    public Vendor() {}

    public Vendor(Long id, String tenantId, String name, String code, String email, String phone, String address, Double outstandingBalance, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.code = code;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.outstandingBalance = outstandingBalance;
        this.createdAt = createdAt;
    }

    public static VendorBuilder builder() {
        return new VendorBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getOutstandingBalance() { return outstandingBalance; }
    public void setOutstandingBalance(Double outstandingBalance) { this.outstandingBalance = outstandingBalance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class VendorBuilder {
        private Long id;
        private String tenantId;
        private String name;
        private String code;
        private String email;
        private String phone;
        private String address;
        private Double outstandingBalance = 0.0;
        private LocalDateTime createdAt;

        public VendorBuilder id(Long id) { this.id = id; return this; }
        public VendorBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public VendorBuilder name(String name) { this.name = name; return this; }
        public VendorBuilder code(String code) { this.code = code; return this; }
        public VendorBuilder email(String email) { this.email = email; return this; }
        public VendorBuilder phone(String phone) { this.phone = phone; return this; }
        public VendorBuilder address(String address) { this.address = address; return this; }
        public VendorBuilder outstandingBalance(Double outstandingBalance) { this.outstandingBalance = outstandingBalance; return this; }
        public VendorBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Vendor build() {
            return new Vendor(id, tenantId, name, code, email, phone, address, outstandingBalance, createdAt);
        }
    }
}
