package com.msme.erp.controller;

import com.msme.erp.domain.ProductionWorkflow;
import com.msme.erp.dto.WorkflowLogDto;
import com.msme.erp.service.WorkflowEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/workflows/execution")
public class WorkflowExecutionController {

    private final WorkflowEngineService workflowEngineService;

    public WorkflowExecutionController(WorkflowEngineService workflowEngineService) {
        this.workflowEngineService = workflowEngineService;
    }

    @PostMapping("/assign")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_OPERATOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<ProductionWorkflow> assignWorkflowToOrder(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("orderId");
        String workflowVersionId = payload.get("workflowVersionId");
        return ResponseEntity.ok(workflowEngineService.assignWorkflowToOrder(orderId, workflowVersionId));
    }

    @PostMapping("/move")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_OPERATOR', 'ROLE_QUALITY_INSPECTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> moveOrderToStage(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("orderId");
        String targetStageId = payload.get("targetStageId");
        String remarks = payload.getOrDefault("remarks", "");
        workflowEngineService.moveOrderToStage(orderId, targetStageId, remarks);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history/{orderId}")
    public ResponseEntity<List<WorkflowLogDto>> getOrderWorkflowHistory(@PathVariable String orderId) {
        return ResponseEntity.ok(workflowEngineService.getOrderWorkflowHistory(orderId));
    }
}
