package com.msme.erp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "compliance_controls")
public class ComplianceControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String framework; // SOC2, ISO27001, GDPR
    
    @Column(nullable = false, unique = true)
    private String controlCode;
    
    private String description;
    private String status; // COMPLIANT, IN_PROGRESS, NON_COMPLIANT

    public ComplianceControl() {}
    public ComplianceControl(String framework, String controlCode, String description, String status) {
        this.framework = framework;
        this.controlCode = controlCode;
        this.description = description;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFramework() { return framework; }
    public void setFramework(String framework) { this.framework = framework; }
    public String getControlCode() { return controlCode; }
    public void setControlCode(String controlCode) { this.controlCode = controlCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
