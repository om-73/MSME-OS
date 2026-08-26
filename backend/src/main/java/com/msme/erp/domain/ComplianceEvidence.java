package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "compliance_evidence")
public class ComplianceEvidence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long controlId;
    private String evidenceType;
    private String contentHash;
    private String filePath;
    private LocalDateTime signedAt;

    public ComplianceEvidence() {}
    public ComplianceEvidence(Long controlId, String evidenceType, String contentHash, String filePath, LocalDateTime signedAt) {
        this.controlId = controlId;
        this.evidenceType = evidenceType;
        this.contentHash = contentHash;
        this.filePath = filePath;
        this.signedAt = signedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getControlId() { return controlId; }
    public void setControlId(Long controlId) { this.controlId = controlId; }
    public String getEvidenceType() { return evidenceType; }
    public void setEvidenceType(String evidenceType) { this.evidenceType = evidenceType; }
    public String getContentHash() { return contentHash; }
    public void setContentHash(String contentHash) { this.contentHash = contentHash; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public LocalDateTime getSignedAt() { return signedAt; }
    public void setSignedAt(LocalDateTime signedAt) { this.signedAt = signedAt; }
}
