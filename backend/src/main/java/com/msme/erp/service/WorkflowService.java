package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.dto.WorkflowRequest;
import com.msme.erp.dto.WorkflowResponse;
import com.msme.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowVersionRepository versionRepository;
    private final WorkflowStageRepository stageRepository;
    private final WorkflowEdgeRepository edgeRepository;
    private final WorkflowRuleRepository ruleRepository;
    private final WorkflowTemplateRepository templateRepository;
    private final WorkflowHistoryRepository historyRepository;
    private final DepartmentRepository departmentRepository;

    public WorkflowService(WorkflowRepository workflowRepository, WorkflowVersionRepository versionRepository,
                           WorkflowStageRepository stageRepository, WorkflowEdgeRepository edgeRepository,
                           WorkflowRuleRepository ruleRepository, WorkflowTemplateRepository templateRepository,
                           WorkflowHistoryRepository historyRepository, DepartmentRepository departmentRepository) {
        this.workflowRepository = workflowRepository;
        this.versionRepository = versionRepository;
        this.stageRepository = stageRepository;
        this.edgeRepository = edgeRepository;
        this.ruleRepository = ruleRepository;
        this.templateRepository = templateRepository;
        this.historyRepository = historyRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<WorkflowResponse> getAllWorkflows() {
        String tenantId = TenantContext.getCurrentTenant();
        List<Workflow> workflows = workflowRepository.findByTenantIdAndDeletedFalse(tenantId);
        return workflows.stream().map(w -> {
            WorkflowVersion version = versionRepository.findByWorkflowIdAndVersionNumber(w.getId(), w.getCurrentVersion())
                    .orElse(null);
            return mapToResponse(w, version);
        }).collect(Collectors.toList());
    }

    public WorkflowResponse getWorkflowById(String id) {
        String tenantId = TenantContext.getCurrentTenant();
        Workflow w = workflowRepository.findByIdAndTenantIdAndDeletedFalse(id, tenantId)
                .orElseThrow(() -> new NoSuchElementException("Workflow not found with id: " + id));
        WorkflowVersion version = versionRepository.findByWorkflowIdAndVersionNumber(w.getId(), w.getCurrentVersion())
                .orElse(null);
        return mapToResponse(w, version);
    }

    @Transactional
    public WorkflowResponse createWorkflow(WorkflowRequest request) {
        String tenantId = TenantContext.getCurrentTenant();

        // 1. Save main workflow
        Workflow workflow = Workflow.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .description(request.getDescription())
                .industry(request.getIndustry())
                .currentVersion(1)
                .status("DRAFT")
                .build();
        workflow = workflowRepository.save(workflow);

        // 2. Save first draft version
        WorkflowVersion version = WorkflowVersion.builder()
                .workflowId(workflow.getId())
                .versionNumber(1)
                .status("DRAFT")
                .definitionJson(request.getDefinitionJson())
                .build();
        version = versionRepository.save(version);

        // 3. Save stages & build map of client-id to database-id
        Map<String, String> idMap = saveStagesAndEdges(version.getId(), request.getStages(), request.getEdges());

        // Log history
        historyRepository.save(WorkflowHistory.builder()
                .workflowId(workflow.getId())
                .changeType("CREATE")
                .changedBy("Factory Owner")
                .changeSummary("Created initial workflow draft version 1")
                .build());

        return mapToResponse(workflow, version);
    }

    @Transactional
    public WorkflowResponse updateWorkflow(String id, WorkflowRequest request) {
        String tenantId = TenantContext.getCurrentTenant();
        Workflow w = workflowRepository.findByIdAndTenantIdAndDeletedFalse(id, tenantId)
                .orElseThrow(() -> new NoSuchElementException("Workflow not found with id: " + id));

        w.setName(request.getName());
        w.setDescription(request.getDescription());
        w.setIndustry(request.getIndustry());
        workflowRepository.save(w);

        // Fetch current version
        WorkflowVersion version = versionRepository.findByWorkflowIdAndVersionNumber(w.getId(), w.getCurrentVersion())
                .orElseThrow(() -> new IllegalStateException("Current version not found"));

        if (!"DRAFT".equals(version.getStatus())) {
            // If current version is published/archived, create a new draft version incremented by 1
            int nextVer = w.getCurrentVersion() + 1;
            version = WorkflowVersion.builder()
                    .workflowId(w.getId())
                    .versionNumber(nextVer)
                    .status("DRAFT")
                    .definitionJson(request.getDefinitionJson())
                    .build();
            version = versionRepository.save(version);
            w.setCurrentVersion(nextVer);
            workflowRepository.save(w);
        } else {
            // Modify existing draft version
            version.setDefinitionJson(request.getDefinitionJson());
            versionRepository.save(version);
            // Clear current stages/edges for draft overwrite
            edgeRepository.deleteByWorkflowVersionId(version.getId());
            List<WorkflowStage> oldStages = stageRepository.findByWorkflowVersionIdOrderBySequenceOrderAsc(version.getId());
            for (WorkflowStage os : oldStages) {
                ruleRepository.deleteByStageId(os.getId());
                stageRepository.delete(os);
            }
        }

        saveStagesAndEdges(version.getId(), request.getStages(), request.getEdges());

        historyRepository.save(WorkflowHistory.builder()
                .workflowId(w.getId())
                .changeType("EDIT")
                .changedBy("Factory Owner")
                .changeSummary("Updated workflow draft version " + version.getVersionNumber())
                .build());

        return mapToResponse(w, version);
    }

    @Transactional
    public WorkflowResponse publishWorkflow(String id) {
        String tenantId = TenantContext.getCurrentTenant();
        Workflow w = workflowRepository.findByIdAndTenantIdAndDeletedFalse(id, tenantId)
                .orElseThrow(() -> new NoSuchElementException("Workflow not found with id: " + id));

        WorkflowVersion currentVer = versionRepository.findByWorkflowIdAndVersionNumber(w.getId(), w.getCurrentVersion())
                .orElseThrow(() -> new IllegalStateException("Current version not found"));

        // Validate structure before publishing
        List<WorkflowStage> stages = stageRepository.findByWorkflowVersionIdOrderBySequenceOrderAsc(currentVer.getId());
        validateWorkflowStructure(stages);

        // Archive previous published version if any
        Optional<WorkflowVersion> activeVerOpt = versionRepository.findByWorkflowIdAndStatus(w.getId(), "PUBLISHED");
        if (activeVerOpt.isPresent()) {
            WorkflowVersion prev = activeVerOpt.get();
            prev.setStatus("ARCHIVED");
            versionRepository.save(prev);
        }

        currentVer.setStatus("PUBLISHED");
        versionRepository.save(currentVer);

        w.setStatus("PUBLISHED");
        workflowRepository.save(w);

        historyRepository.save(WorkflowHistory.builder()
                .workflowId(w.getId())
                .changeType("PUBLISH")
                .changedBy("Factory Owner")
                .changeSummary("Published workflow version " + currentVer.getVersionNumber())
                .build());

        return mapToResponse(w, currentVer);
    }

    @Transactional
    public WorkflowResponse cloneWorkflow(String id) {
        String tenantId = TenantContext.getCurrentTenant();
        Workflow source = workflowRepository.findByIdAndTenantIdAndDeletedFalse(id, tenantId)
                .orElseThrow(() -> new NoSuchElementException("Workflow not found to clone"));

        Workflow cloned = Workflow.builder()
                .tenantId(tenantId)
                .name(source.getName() + " (Copy)")
                .description(source.getDescription())
                .industry(source.getIndustry())
                .currentVersion(1)
                .status("DRAFT")
                .build();
        cloned = workflowRepository.save(cloned);

        WorkflowVersion srcVer = versionRepository.findByWorkflowIdAndVersionNumber(source.getId(), source.getCurrentVersion())
                .orElseThrow(() -> new IllegalStateException("Source version not found"));

        WorkflowVersion targetVer = versionRepository.save(WorkflowVersion.builder()
                .workflowId(cloned.getId())
                .versionNumber(1)
                .status("DRAFT")
                .definitionJson(srcVer.getDefinitionJson())
                .build());

        List<WorkflowStage> srcStages = stageRepository.findByWorkflowVersionIdOrderBySequenceOrderAsc(srcVer.getId());
        List<WorkflowEdge> srcEdges = edgeRepository.findByWorkflowVersionId(srcVer.getId());

        List<WorkflowRequest.StageRequest> stagesReq = srcStages.stream().map(s -> {
            WorkflowRequest.StageRequest r = new WorkflowRequest.StageRequest();
            r.setId(s.getId());
            r.setName(s.getName());
            r.setCode(s.getCode());
            r.setDescription(s.getDescription());
            r.setSequenceOrder(s.getSequenceOrder());
            r.setType(s.getType());
            r.setColorHex(s.getColorHex());
            r.setEstimatedSlaHours(s.getEstimatedSlaHours());
            r.setChecklistItems(s.getChecklistItems());
            r.setDepartmentId(s.getDepartmentId());
            return r;
        }).collect(Collectors.toList());

        List<WorkflowRequest.EdgeRequest> edgesReq = srcEdges.stream().map(e -> {
            WorkflowRequest.EdgeRequest r = new WorkflowRequest.EdgeRequest();
            r.setSourceStageId(e.getSourceStageId());
            r.setTargetStageId(e.getTargetStageId());
            r.setConditionExpression(e.getConditionExpression());
            return r;
        }).collect(Collectors.toList());

        saveStagesAndEdges(targetVer.getId(), stagesReq, edgesReq);

        return mapToResponse(cloned, targetVer);
    }

    @Transactional
    public void deleteWorkflow(String id) {
        String tenantId = TenantContext.getCurrentTenant();
        Workflow w = workflowRepository.findByIdAndTenantIdAndDeletedFalse(id, tenantId)
                .orElseThrow(() -> new NoSuchElementException("Workflow not found with id: " + id));
        w.setDeleted(true);
        workflowRepository.save(w);
    }

    private Map<String, String> saveStagesAndEdges(String versionId, List<WorkflowRequest.StageRequest> stages, List<WorkflowRequest.EdgeRequest> edges) {
        Map<String, String> idMap = new HashMap<>();

        if (stages == null) return idMap;

        // 1. Create stages
        for (WorkflowRequest.StageRequest req : stages) {
            WorkflowStage stage = WorkflowStage.builder()
                    .workflowVersionId(versionId)
                    .name(req.getName())
                    .code(req.getCode() != null ? req.getCode() : req.getName().toUpperCase().replace(" ", "_"))
                    .description(req.getDescription())
                    .sequenceOrder(req.getSequenceOrder())
                    .type(req.getType() != null ? req.getType() : "NORMAL")
                    .colorHex(req.getColorHex() != null ? req.getColorHex() : "#3B82F6")
                    .estimatedSlaHours(req.getEstimatedSlaHours() != null ? req.getEstimatedSlaHours() : 12)
                    .checklistItems(req.getChecklistItems())
                    .departmentId(req.getDepartmentId())
                    .build();
            stage = stageRepository.save(stage);

            if (req.getId() != null) {
                idMap.put(req.getId(), stage.getId());
            }
        }

        // 2. Create edges using mapped database ids
        if (edges != null) {
            for (WorkflowRequest.EdgeRequest edgeReq : edges) {
                String sourceId = idMap.getOrDefault(edgeReq.getSourceStageId(), edgeReq.getSourceStageId());
                String targetId = idMap.getOrDefault(edgeReq.getTargetStageId(), edgeReq.getTargetStageId());

                WorkflowEdge edge = WorkflowEdge.builder()
                        .workflowVersionId(versionId)
                        .sourceStageId(sourceId)
                        .targetStageId(targetId)
                        .conditionExpression(edgeReq.getConditionExpression())
                        .build();
                edgeRepository.save(edge);
            }
        }

        return idMap;
    }

    private void validateWorkflowStructure(List<WorkflowStage> stages) {
        boolean hasStart = false;
        boolean hasEnd = false;

        for (WorkflowStage s : stages) {
            if ("START".equalsIgnoreCase(s.getType())) hasStart = true;
            if ("END".equalsIgnoreCase(s.getType())) hasEnd = true;
        }

        if (!hasStart) {
            throw new IllegalArgumentException("Workflow validation failed: Missing START node.");
        }
        if (!hasEnd) {
            throw new IllegalArgumentException("Workflow validation failed: Missing END node.");
        }
    }

    private WorkflowResponse mapToResponse(Workflow w, WorkflowVersion ver) {
        if (w == null) return null;

        List<WorkflowResponse.StageResponse> stages = new ArrayList<>();
        List<WorkflowResponse.EdgeResponse> edges = new ArrayList<>();

        if (ver != null) {
            List<WorkflowStage> entityStages = stageRepository.findByWorkflowVersionIdOrderBySequenceOrderAsc(ver.getId());
            stages = entityStages.stream().map(s -> {
                String deptName = "Unassigned";
                if (s.getDepartmentId() != null) {
                    Optional<Department> d = departmentRepository.findById(s.getDepartmentId());
                    if (d.isPresent()) deptName = d.get().getName();
                }
                return new WorkflowResponse.StageResponse(
                        s.getId(), s.getName(), s.getCode(), s.getDescription(),
                        s.getSequenceOrder(), s.getType(), s.getColorHex(),
                        s.getEstimatedSlaHours(), s.getChecklistItems(),
                        s.getDepartmentId(), deptName
                );
            }).collect(Collectors.toList());

            List<WorkflowEdge> entityEdges = edgeRepository.findByWorkflowVersionId(ver.getId());
            edges = entityEdges.stream().map(e -> new WorkflowResponse.EdgeResponse(
                    e.getId(), e.getSourceStageId(), e.getTargetStageId(), e.getConditionExpression()
            )).collect(Collectors.toList());
        }

        return new WorkflowResponse(
                w.getId(), w.getTenantId(), w.getName(), w.getDescription(), w.getIndustry(),
                w.getCurrentVersion(), w.getStatus(), ver != null ? ver.getDefinitionJson() : null,
                stages, edges, w.getCreatedAt(), w.getUpdatedAt()
        );
    }
}
