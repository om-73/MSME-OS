package com.msme.erp.controller;

import com.msme.erp.domain.WorkerTask;
import com.msme.erp.service.WorkerTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/worker/tasks")
public class WorkerTaskController {

    private final WorkerTaskService workerTaskService;

    public WorkerTaskController(WorkerTaskService workerTaskService) {
        this.workerTaskService = workerTaskService;
    }

    @GetMapping
    public ResponseEntity<List<WorkerTask>> getAllTasks() {
        return ResponseEntity.ok(workerTaskService.getAllTasks());
    }

    @GetMapping("/my/{workerId}")
    public ResponseEntity<List<WorkerTask>> getMyTasks(@PathVariable String workerId) {
        return ResponseEntity.ok(workerTaskService.getTasksForWorker(workerId));
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_OPERATOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<WorkerTask> assignTask(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        String workerId = payload.get("workerId");
        String workerName = payload.get("workerName");
        String managerName = payload.get("managerName");
        return ResponseEntity.ok(workerTaskService.assignTask(id, workerId, workerName, managerName));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<WorkerTask> startTask(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String operatorName = payload.getOrDefault("operatorName", "Worker");
        return ResponseEntity.ok(workerTaskService.startTask(id, operatorName));
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<WorkerTask> pauseTask(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String operatorName = payload.getOrDefault("operatorName", "Worker");
        return ResponseEntity.ok(workerTaskService.pauseTask(id, operatorName));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<WorkerTask> completeTask(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        String remarks = payload.getOrDefault("remarks", "");
        String photoUrl = payload.getOrDefault("photoUrl", "");
        String operatorName = payload.getOrDefault("operatorName", "Worker");
        return ResponseEntity.ok(workerTaskService.completeTask(id, remarks, photoUrl, operatorName));
    }

    @PostMapping("/{id}/report-issue")
    public ResponseEntity<WorkerTask> reportIssue(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        String issueType = payload.get("issueType");
        String remarks = payload.getOrDefault("remarks", "");
        String operatorName = payload.getOrDefault("operatorName", "Worker");
        return ResponseEntity.ok(workerTaskService.reportIssue(id, issueType, remarks, operatorName));
    }

    @PostMapping("/rework")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_QUALITY_INSPECTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> triggerRework(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("orderId");
        String targetStageId = payload.get("targetStageId");
        String reason = payload.get("reason");
        String managerName = payload.getOrDefault("managerName", "QC Lead");
        workerTaskService.triggerRework(orderId, targetStageId, reason, managerName);
        return ResponseEntity.ok().build();
    }
}
