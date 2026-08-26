package com.msme.erp.controller;

import com.msme.erp.service.AnalyticsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/executive")
    public ResponseEntity<Map<String, Object>> getExecutiveSummary() {
        return ResponseEntity.ok(analyticsService.getExecutiveSummary());
    }

    @GetMapping("/forecast")
    public ResponseEntity<Map<String, Object>> getAIForecast() {
        return ResponseEntity.ok(analyticsService.getAIForecast());
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReport(@RequestParam String reportType) {
        String csvContent = analyticsService.generateCsvReport(reportType);
        byte[] bytes = csvContent.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + reportType + "-report.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(bytes);
    }
}
