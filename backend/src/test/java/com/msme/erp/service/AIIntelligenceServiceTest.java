package com.msme.erp.service;

import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AIIntelligenceServiceTest {

    @Mock
    private AIInsightRepository insightRepository;

    @Mock
    private PredictionRepository predictionRepository;

    @Mock
    private AIRecommendationRepository recommendationRepository;

    @Mock
    private AIAuditLogRepository aiAuditLogRepository;

    @Mock
    private NotificationCenterService notificationCenterService;

    @InjectMocks
    private AIIntelligenceService aiService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testExecutiveOverviewCalculatesEfficiencyScores() {
        Map<String, Object> overview = aiService.getExecutiveOverview();

        assertNotNull(overview);
        assertEquals(88.4, overview.get("overallEfficiencyPct"));
        assertEquals(96.2, overview.get("qualityPassRatePct"));
    }

    @Test
    void testHumanApprovalWorkflowExecutesRecommendationSafely() {
        Long recId = 10L;
        AIRecommendation rec = AIRecommendation.builder()
                .id(recId)
                .tenantId("apex-tenant-01")
                .title("Re-route Batch ORD-90 to Line B")
                .approvalStatus("PENDING")
                .confidenceScore(0.92)
                .build();

        when(recommendationRepository.findById(recId)).thenReturn(Optional.of(rec));
        when(recommendationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AIRecommendation approved = aiService.approveRecommendation(recId, "factory_owner");

        assertEquals("EXECUTED", approved.getApprovalStatus());
        assertEquals("factory_owner", approved.getApprovedBy());
        verify(aiAuditLogRepository, times(1)).save(any(AIAuditLog.class));
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("RecommendationExecutedEvent"), any(), eq("HIGH"), any());
    }

    @Test
    void testCopilotProcessReadOnlyAnalyticsQuery() {
        Map<String, Object> response = aiService.processCopilotQuery("Which materials are at stockout risk?");

        assertNotNull(response);
        assertTrue(((String) response.get("answer")).contains("RM-TH-01"));
        assertTrue((Boolean) response.get("readOnlyQuery"));
        verify(aiAuditLogRepository, times(1)).save(any(AIAuditLog.class));
    }
}
