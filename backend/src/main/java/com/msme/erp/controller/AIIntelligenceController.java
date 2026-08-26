package com.msme.erp.controller;

import com.msme.erp.domain.*;
import com.msme.erp.service.AIIntelligenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
public class AIIntelligenceController {

    private final AIIntelligenceService aiService;

    public AIIntelligenceController(AIIntelligenceService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverview() {
        return ResponseEntity.ok(aiService.getExecutiveOverview());
    }

    @GetMapping("/predictions")
    public ResponseEntity<List<Prediction>> getPredictions() {
        return ResponseEntity.ok(aiService.getOrderDelayPredictions());
    }

    @GetMapping("/insights")
    public ResponseEntity<List<AIInsight>> getInsights() {
        return ResponseEntity.ok(aiService.getAIInsights());
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<AIRecommendation>> getRecommendations() {
        return ResponseEntity.ok(aiService.getRecommendations());
    }

    @PostMapping("/recommendations/{id}/approve")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<AIRecommendation> approveRecommendation(@PathVariable Long id, @RequestParam(required = false, defaultValue = "factory_owner") String approvedBy) {
        return ResponseEntity.ok(aiService.approveRecommendation(id, approvedBy));
    }

    @PostMapping("/copilot/query")
    public ResponseEntity<Map<String, Object>> processCopilotQuery(@RequestBody Map<String, String> payload) {
        String query = payload.getOrDefault("query", "Which orders are at delay risk?");
        return ResponseEntity.ok(aiService.processCopilotQuery(query));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AIAuditLog>> getAuditLogs() {
        return ResponseEntity.ok(aiService.getAuditLogs());
    }
}
