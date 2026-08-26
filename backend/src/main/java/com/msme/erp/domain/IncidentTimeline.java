package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incident_timelines")
public class IncidentTimeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long incidentId;
    private String statusUpdate;
    private String updatedBy;
    private LocalDateTime timestamp;

    public IncidentTimeline() {}
    public IncidentTimeline(Long incidentId, String statusUpdate, String updatedBy, LocalDateTime timestamp) {
        this.incidentId = incidentId;
        this.statusUpdate = statusUpdate;
        this.updatedBy = updatedBy;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
    public String getStatusUpdate() { return statusUpdate; }
    public void setStatusUpdate(String statusUpdate) { this.statusUpdate = statusUpdate; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
