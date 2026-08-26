package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_playbooks")
public class CustomerPlaybook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    private String triggerType; // ONBOARDING, FIRST_ORDER, DELAY_ALERT, QC_ALERT
    
    @Column(length = 2000)
    private String stepsJson; // e.g. [{"step": 1, "title": "Setup client portal"}, {"step": 2, "title": "Upload artwork approval"}]
    
    private boolean active = true;

    public CustomerPlaybook() {}

    public CustomerPlaybook(Long id, String tenantId, String name, String triggerType, String stepsJson, boolean active) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.triggerType = triggerType;
        this.stepsJson = stepsJson;
        this.active = active;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTriggerType() { return triggerType; }
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }
    public String getStepsJson() { return stepsJson; }
    public void setStepsJson(String stepsJson) { this.stepsJson = stepsJson; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static class Builder {
        private Long id;
        private String tenantId;
        private String name;
        private String triggerType;
        private String stepsJson;
        private boolean active = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder triggerType(String triggerType) { this.triggerType = triggerType; return this; }
        public Builder stepsJson(String stepsJson) { this.stepsJson = stepsJson; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public CustomerPlaybook build() {
            return new CustomerPlaybook(id, tenantId, name, triggerType, stepsJson, active);
        }
    }
}
