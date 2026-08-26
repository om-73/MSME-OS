package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "platform_configurations")
public class PlatformConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String tenantId;
    
    @Column(nullable = false, unique = true)
    private String configKey;
    
    private String configValue;
    private boolean sensitive;

    public PlatformConfiguration() {}
    public PlatformConfiguration(String tenantId, String configKey, String configValue, boolean sensitive) {
        this.tenantId = tenantId;
        this.configKey = configKey;
        this.configValue = configValue;
        this.sensitive = sensitive;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }
    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }
    public boolean isSensitive() { return sensitive; }
    public void setSensitive(boolean sensitive) { this.sensitive = sensitive; }
}
