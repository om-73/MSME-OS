package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "legal_holds")
public class LegalHold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    private String affectedDataScope;
    private String reason;
    private String createdBy;
    private LocalDateTime startDate;
    private boolean active;

    public LegalHold() {}
    public LegalHold(String tenantId, String affectedDataScope, String reason, String createdBy, LocalDateTime startDate, boolean active) {
        this.tenantId = tenantId;
        this.affectedDataScope = affectedDataScope;
        this.reason = reason;
        this.createdBy = createdBy;
        this.startDate = startDate;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getAffectedDataScope() { return affectedDataScope; }
    public void setAffectedDataScope(String affectedDataScope) { this.affectedDataScope = affectedDataScope; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
