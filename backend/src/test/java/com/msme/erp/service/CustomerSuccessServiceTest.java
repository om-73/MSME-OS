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
public class CustomerSuccessServiceTest {

    @Mock
    private CustomerAccountRepository accountRepository;

    @Mock
    private SupportTicketRepository ticketRepository;

    @Mock
    private TicketMessageRepository messageRepository;

    @Mock
    private ClientApprovalRequestRepository approvalRepository;

    @Mock
    private ProductionOrderRepository orderRepository;

    @Mock
    private NotificationCenterService notificationCenterService;

    @Mock
    private CustomerFeedbackRepository feedbackRepository;

    @Mock
    private CustomerSuccessTaskRepository csTaskRepository;

    @Mock
    private CustomerPlaybookRepository playbookRepository;

    @Mock
    private CustomerPlaybookExecutionRepository playbookExecutionRepository;

    @Mock
    private CustomerHealthSnapshotRepository healthSnapshotRepository;

    @Mock
    private TicketSLARepository slaRepository;

    @Mock
    private TicketEscalationRepository escalationRepository;

    @Mock
    private CustomerContactReferenceRepository contactReferenceRepository;

    @Mock
    private CustomerCommunicationPreferenceRepository communicationPreferenceRepository;

    @InjectMocks
    private CustomerSuccessService customerSuccessService;

    @BeforeEach
    void setUp() {
        com.msme.erp.config.TenantContext.setCurrentTenant("apex-tenant-01");
    }

    @Test
    void testDecideApprovalBindsVersionAndFiresNotification() {
        ClientApprovalRequest req = ClientApprovalRequest.builder()
                .id(1L)
                .tenantId("apex-tenant-01")
                .clientCode("CLI-APEX-01")
                .documentId(10L)
                .documentVersion("2.0")
                .title("Tech Pack v2.0 Approval")
                .status("PENDING")
                .build();

        when(approvalRepository.findById(1L)).thenReturn(Optional.of(req));
        when(approvalRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ClientApprovalRequest decided = customerSuccessService.decideApproval(1L, "APPROVED", "Approved for production", "buyer@brand.com");

        assertEquals("APPROVED", decided.getStatus());
        assertEquals("2.0", decided.getDocumentVersion());
        assertEquals("buyer@brand.com", decided.getApprovedByEmail());
        verify(notificationCenterService, times(1)).publishEvent(any(), eq("ApprovalCompletedEvent"), any(), eq("NORMAL"), any());
    }

    @Test
    void testGetTicketMessagesFiltersInternalNotesForClientUser() {
        TicketMessage clientMsg = TicketMessage.builder().id(1L).tenantId("apex-tenant-01").ticketId(101L).visibilityScope("CLIENT_VISIBLE").messageText("Public update").build();

        when(messageRepository.findByTenantIdAndTicketIdAndVisibilityScopeOrderByCreatedAtAsc("apex-tenant-01", 101L, "CLIENT_VISIBLE"))
                .thenReturn(Collections.singletonList(clientMsg));

        List<TicketMessage> messages = customerSuccessService.getTicketMessages(101L, true);

        assertEquals(1, messages.size());
        assertEquals("CLIENT_VISIBLE", messages.get(0).getVisibilityScope());
    }
}
