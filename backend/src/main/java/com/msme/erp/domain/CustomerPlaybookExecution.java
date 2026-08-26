package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_playbook_executions")
public class CustomerPlaybookExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private Long playbookId;

    @Column(nullable = false)
    private String clientCode;

    private String status = "ACTIVE"; // ACTIVE, COMPLETED, CANCELLED
    private int currentStepIndex = 0;
    
    private LocalDateTime startedAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        startedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public CustomerPlaybookExecution() {}

    public CustomerPlaybookExecution(Long id, String tenantId, Long playbookId, String clientCode, String status, int currentStepIndex) {
        this.id = id;
        this.tenantId = tenantId;
        this.playbookId = playbookId;
        this.clientCode = clientCode;
        this.status = status;
        this.currentStepIndex = currentStepIndex;
    }

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getPlaybookId() { return playbookId; }
    public void setPlaybookId(Long playbookId) { this.playbookId = playbookId; }
    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getCurrentStepIndex() { return currentStepIndex; }
    public void setCurrentStepIndex(int currentStepIndex) { this.currentStepIndex = currentStepIndex; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public static class Builder {
        private Long id;
        private String tenantId;
        private Long playbookId;
        private String clientCode;
        private String status = "ACTIVE";
        private int currentStepIndex = 0;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder playbookId(Long playbookId) { this.playbookId = playbookId; return this; }
        public Builder clientCode(String clientCode) { this.clientCode = clientCode; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder currentStepIndex(int currentStepIndex) { this.currentStepIndex = currentStepIndex; return this; }

        public CustomerPlaybookExecution build() {
            return new CustomerPlaybookExecution(id, tenantId, playbookId, clientCode, status, currentStepIndex);
        }
    }
}
