package com.msme.erp.controller;

import com.msme.erp.dto.WorkflowRequest;
import com.msme.erp.dto.WorkflowResponse;
import com.msme.erp.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping
    public ResponseEntity<List<WorkflowResponse>> getAllWorkflows() {
        return ResponseEntity.ok(workflowService.getAllWorkflows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowResponse> getWorkflowById(@PathVariable String id) {
        return ResponseEntity.ok(workflowService.getWorkflowById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<WorkflowResponse> createWorkflow(@RequestBody WorkflowRequest request) {
        return ResponseEntity.ok(workflowService.createWorkflow(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<WorkflowResponse> updateWorkflow(@PathVariable String id, @RequestBody WorkflowRequest request) {
        return ResponseEntity.ok(workflowService.updateWorkflow(id, request));
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<WorkflowResponse> publishWorkflow(@PathVariable String id) {
        return ResponseEntity.ok(workflowService.publishWorkflow(id));
    }

    @PostMapping("/{id}/clone")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<WorkflowResponse> cloneWorkflow(@PathVariable String id) {
        return ResponseEntity.ok(workflowService.cloneWorkflow(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable String id) {
        workflowService.deleteWorkflow(id);
        return ResponseEntity.noContent().build();
    }
}
