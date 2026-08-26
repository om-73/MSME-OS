package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_communication_preferences")
public class CustomerCommunicationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String clientCode;

    private boolean emailNotifications = true;
    private boolean whatsappNotifications = false;
    private boolean slackNotifications = false;

    public CustomerCommunicationPreference() {}

    public CustomerCommunicationPreference(Long id, String tenantId, String clientCode, boolean emailNotifications, boolean whatsappNotifications, boolean slackNotifications) {
        this.id = id;
        this.tenantId = tenantId;
        this.clientCode = clientCode;
        this.emailNotifications = emailNotifications;
        this.whatsappNotifications = whatsappNotifications;
        this.slackNotifications = slackNotifications;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }
    public boolean isEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(boolean emailNotifications) { this.emailNotifications = emailNotifications; }
    public boolean isWhatsappNotifications() { return whatsappNotifications; }
    public void setWhatsappNotifications(boolean whatsappNotifications) { this.whatsappNotifications = whatsappNotifications; }
    public boolean isSlackNotifications() { return slackNotifications; }
    public void setSlackNotifications(boolean slackNotifications) { this.slackNotifications = slackNotifications; }

    public static class Builder {
        private Long id;
        private String tenantId;
        private String clientCode;
        private boolean emailNotifications = true;
        private boolean whatsappNotifications = false;
        private boolean slackNotifications = false;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder clientCode(String clientCode) { this.clientCode = clientCode; return this; }
        public Builder emailNotifications(boolean emailNotifications) { this.emailNotifications = emailNotifications; return this; }
        public Builder whatsappNotifications(boolean whatsappNotifications) { this.whatsappNotifications = whatsappNotifications; return this; }
        public Builder slackNotifications(boolean slackNotifications) { this.slackNotifications = slackNotifications; return this; }

        public CustomerCommunicationPreference build() {
            return new CustomerCommunicationPreference(id, tenantId, clientCode, emailNotifications, whatsappNotifications, slackNotifications);
        }
    }
}
