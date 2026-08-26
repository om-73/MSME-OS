package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "automation_rules")
public class AutomationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String triggerEvent; // inventory.low_stock, production.stage.completed, qc.failed, order.created

    @Column(length = 1000)
    private String conditionExpression;

    @Column(length = 1000, nullable = false)
    private String actions; // SEND_NOTIFICATION,SEND_WEBHOOK,CREATE_TASK

    private boolean active = true;
    private int executionCount = 0;

    private LocalDateTime createdAt;

    public AutomationRule() {}

    public AutomationRule(Long id, String tenantId, String name, String triggerEvent, String conditionExpression, String actions, boolean active, int executionCount, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.triggerEvent = triggerEvent;
        this.conditionExpression = conditionExpression;
        this.actions = actions;
        this.active = active;
        this.executionCount = executionCount;
        this.createdAt = createdAt;
    }

    public static AutomationRuleBuilder builder() {
        return new AutomationRuleBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTriggerEvent() { return triggerEvent; }
    public void setTriggerEvent(String triggerEvent) { this.triggerEvent = triggerEvent; }
    public String getConditionExpression() { return conditionExpression; }
    public void setConditionExpression(String conditionExpression) { this.conditionExpression = conditionExpression; }
    public String getActions() { return actions; }
    public void setActions(String actions) { this.actions = actions; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public int getExecutionCount() { return executionCount; }
    public void setExecutionCount(int executionCount) { this.executionCount = executionCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class AutomationRuleBuilder {
        private Long id;
        private String tenantId;
        private String name;
        private String triggerEvent;
        private String conditionExpression;
        private String actions;
        private boolean active = true;
        private int executionCount = 0;
        private LocalDateTime createdAt;

        public AutomationRuleBuilder id(Long id) { this.id = id; return this; }
        public AutomationRuleBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public AutomationRuleBuilder name(String name) { this.name = name; return this; }
        public AutomationRuleBuilder triggerEvent(String triggerEvent) { this.triggerEvent = triggerEvent; return this; }
        public AutomationRuleBuilder conditionExpression(String conditionExpression) { this.conditionExpression = conditionExpression; return this; }
        public AutomationRuleBuilder actions(String actions) { this.actions = actions; return this; }
        public AutomationRuleBuilder active(boolean active) { this.active = active; return this; }
        public AutomationRuleBuilder executionCount(int executionCount) { this.executionCount = executionCount; return this; }
        public AutomationRuleBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AutomationRule build() {
            return new AutomationRule(id, tenantId, name, triggerEvent, conditionExpression, actions, active, executionCount, createdAt);
        }
    }
}
