package com.msme.erp.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_templates", indexes = {
    @Index(name = "idx_workflow_templates_industry", columnList = "industry")
})
public class WorkflowTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String industry; // e.g. Garments, Furniture, Packaging, Jewelry, Plastic, Textile, Printing

    private String description;

    @Column(columnDefinition = "TEXT")
    private String definitionJson; // Preset blueprint config

    private LocalDateTime createdAt;

    public WorkflowTemplate() {}

    public WorkflowTemplate(String id, String name, String industry, String description, String definitionJson, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.description = description;
        this.definitionJson = definitionJson;
        this.createdAt = createdAt;
    }

    public static WorkflowTemplateBuilder builder() { return new WorkflowTemplateBuilder(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDefinitionJson() { return definitionJson; }
    public void setDefinitionJson(String definitionJson) { this.definitionJson = definitionJson; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static class WorkflowTemplateBuilder {
        private String id;
        private String name;
        private String industry;
        private String description;
        private String definitionJson;

        public WorkflowTemplateBuilder id(String id) { this.id = id; return this; }
        public WorkflowTemplateBuilder name(String name) { this.name = name; return this; }
        public WorkflowTemplateBuilder industry(String industry) { this.industry = industry; return this; }
        public WorkflowTemplateBuilder description(String description) { this.description = description; return this; }
        public WorkflowTemplateBuilder definitionJson(String definitionJson) { this.definitionJson = definitionJson; return this; }

        public WorkflowTemplate build() {
            return new WorkflowTemplate(id, name, industry, description, definitionJson, null);
        }
    }
}
