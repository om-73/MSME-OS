package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "security_policies", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId"})
})
public class SecurityPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tenantId;

    private boolean mfaRequiredForAdmins = true;
    private boolean mfaRequiredForWorkers = false;
    private int sessionTimeoutMinutes = 60;
    private int minPasswordLength = 12;
    private boolean requireSpecialChar = true;
    private int maxFailedAttemptsBeforeLockout = 5;

    private LocalDateTime updatedAt;

    public SecurityPolicy() {}

    public SecurityPolicy(Long id, String tenantId, boolean mfaRequiredForAdmins, boolean mfaRequiredForWorkers, int sessionTimeoutMinutes, int minPasswordLength, boolean requireSpecialChar, int maxFailedAttemptsBeforeLockout, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.mfaRequiredForAdmins = mfaRequiredForAdmins;
        this.mfaRequiredForWorkers = mfaRequiredForWorkers;
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
        this.minPasswordLength = minPasswordLength;
        this.requireSpecialChar = requireSpecialChar;
        this.maxFailedAttemptsBeforeLockout = maxFailedAttemptsBeforeLockout;
        this.updatedAt = updatedAt;
    }

    public static SecurityPolicyBuilder builder() {
        return new SecurityPolicyBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public boolean isMfaRequiredForAdmins() { return mfaRequiredForAdmins; }
    public void setMfaRequiredForAdmins(boolean mfaRequiredForAdmins) { this.mfaRequiredForAdmins = mfaRequiredForAdmins; }
    public boolean isMfaRequiredForWorkers() { return mfaRequiredForWorkers; }
    public void setMfaRequiredForWorkers(boolean mfaRequiredForWorkers) { this.mfaRequiredForWorkers = mfaRequiredForWorkers; }
    public int getSessionTimeoutMinutes() { return sessionTimeoutMinutes; }
    public void setSessionTimeoutMinutes(int sessionTimeoutMinutes) { this.sessionTimeoutMinutes = sessionTimeoutMinutes; }
    public int getMinPasswordLength() { return minPasswordLength; }
    public void setMinPasswordLength(int minPasswordLength) { this.minPasswordLength = minPasswordLength; }
    public boolean isRequireSpecialChar() { return requireSpecialChar; }
    public void setRequireSpecialChar(boolean requireSpecialChar) { this.requireSpecialChar = requireSpecialChar; }
    public int getMaxFailedAttemptsBeforeLockout() { return maxFailedAttemptsBeforeLockout; }
    public void setMaxFailedAttemptsBeforeLockout(int maxFailedAttemptsBeforeLockout) { this.maxFailedAttemptsBeforeLockout = maxFailedAttemptsBeforeLockout; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    @PreUpdate
    protected void onSave() {
        updatedAt = LocalDateTime.now();
    }

    public static class SecurityPolicyBuilder {
        private Long id;
        private String tenantId;
        private boolean mfaRequiredForAdmins = true;
        private boolean mfaRequiredForWorkers = false;
        private int sessionTimeoutMinutes = 60;
        private int minPasswordLength = 12;
        private boolean requireSpecialChar = true;
        private int maxFailedAttemptsBeforeLockout = 5;
        private LocalDateTime updatedAt;

        public SecurityPolicyBuilder id(Long id) { this.id = id; return this; }
        public SecurityPolicyBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public SecurityPolicyBuilder mfaRequiredForAdmins(boolean mfaRequiredForAdmins) { this.mfaRequiredForAdmins = mfaRequiredForAdmins; return this; }
        public SecurityPolicyBuilder mfaRequiredForWorkers(boolean mfaRequiredForWorkers) { this.mfaRequiredForWorkers = mfaRequiredForWorkers; return this; }
        public SecurityPolicyBuilder sessionTimeoutMinutes(int sessionTimeoutMinutes) { this.sessionTimeoutMinutes = sessionTimeoutMinutes; return this; }
        public SecurityPolicyBuilder minPasswordLength(int minPasswordLength) { this.minPasswordLength = minPasswordLength; return this; }
        public SecurityPolicyBuilder requireSpecialChar(boolean requireSpecialChar) { this.requireSpecialChar = requireSpecialChar; return this; }
        public SecurityPolicyBuilder maxFailedAttemptsBeforeLockout(int maxFailedAttemptsBeforeLockout) { this.maxFailedAttemptsBeforeLockout = maxFailedAttemptsBeforeLockout; return this; }
        public SecurityPolicyBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public SecurityPolicy build() {
            return new SecurityPolicy(id, tenantId, mfaRequiredForAdmins, mfaRequiredForWorkers, sessionTimeoutMinutes, minPasswordLength, requireSpecialChar, maxFailedAttemptsBeforeLockout, updatedAt);
        }
    }
}
