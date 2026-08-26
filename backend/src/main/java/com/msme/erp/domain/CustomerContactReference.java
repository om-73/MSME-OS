package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_contact_references")
public class CustomerContactReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String clientCode;

    @Column(nullable = false)
    private String userEmail;

    private String contactRole; // Brand Owner, Buyer, Procurement, Approver
    private String phoneNumber;
    private String notificationPreferences = "EMAIL";
    private boolean approvalAuthority = false;
    private boolean active = true;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public CustomerContactReference() {}

    public CustomerContactReference(Long id, String tenantId, String clientCode, String userEmail, String contactRole, String phoneNumber, String notificationPreferences, boolean approvalAuthority, boolean active) {
        this.id = id;
        this.tenantId = tenantId;
        this.clientCode = clientCode;
        this.userEmail = userEmail;
        this.contactRole = contactRole;
        this.phoneNumber = phoneNumber;
        this.notificationPreferences = notificationPreferences;
        this.approvalAuthority = approvalAuthority;
        this.active = active;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getContactRole() { return contactRole; }
    public void setContactRole(String contactRole) { this.contactRole = contactRole; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getNotificationPreferences() { return notificationPreferences; }
    public void setNotificationPreferences(String notificationPreferences) { this.notificationPreferences = notificationPreferences; }
    public boolean isApprovalAuthority() { return approvalAuthority; }
    public void setApprovalAuthority(boolean approvalAuthority) { this.approvalAuthority = approvalAuthority; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static class Builder {
        private Long id;
        private String tenantId;
        private String clientCode;
        private String userEmail;
        private String contactRole;
        private String phoneNumber;
        private String notificationPreferences = "EMAIL";
        private boolean approvalAuthority = false;
        private boolean active = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder clientCode(String clientCode) { this.clientCode = clientCode; return this; }
        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder contactRole(String contactRole) { this.contactRole = contactRole; return this; }
        public Builder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public Builder notificationPreferences(String notificationPreferences) { this.notificationPreferences = notificationPreferences; return this; }
        public Builder approvalAuthority(boolean approvalAuthority) { this.approvalAuthority = approvalAuthority; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public CustomerContactReference build() {
            return new CustomerContactReference(id, tenantId, clientCode, userEmail, contactRole, phoneNumber, notificationPreferences, approvalAuthority, active);
        }
    }
}
