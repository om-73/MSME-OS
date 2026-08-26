package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerSuccessService {

    private final CustomerAccountRepository accountRepository;
    private final SupportTicketRepository ticketRepository;
    private final TicketMessageRepository messageRepository;
    private final ClientApprovalRequestRepository approvalRepository;
    private final ProductionOrderRepository orderRepository;
    private final NotificationCenterService notificationCenterService;

    // Module 17 Specific Repositories
    private final CustomerFeedbackRepository feedbackRepository;
    private final CustomerSuccessTaskRepository csTaskRepository;
    private final CustomerPlaybookRepository playbookRepository;
    private final CustomerPlaybookExecutionRepository playbookExecutionRepository;
    private final CustomerHealthSnapshotRepository healthSnapshotRepository;
    private final TicketSLARepository slaRepository;
    private final TicketEscalationRepository escalationRepository;
    private final CustomerContactReferenceRepository contactReferenceRepository;
    private final CustomerCommunicationPreferenceRepository communicationPreferenceRepository;

    public CustomerSuccessService(CustomerAccountRepository accountRepository,
                                  SupportTicketRepository ticketRepository,
                                  TicketMessageRepository messageRepository,
                                  ClientApprovalRequestRepository approvalRepository,
                                  ProductionOrderRepository orderRepository,
                                  NotificationCenterService notificationCenterService,
                                  CustomerFeedbackRepository feedbackRepository,
                                  CustomerSuccessTaskRepository csTaskRepository,
                                  CustomerPlaybookRepository playbookRepository,
                                  CustomerPlaybookExecutionRepository playbookExecutionRepository,
                                  CustomerHealthSnapshotRepository healthSnapshotRepository,
                                  TicketSLARepository slaRepository,
                                  TicketEscalationRepository escalationRepository,
                                  CustomerContactReferenceRepository contactReferenceRepository,
                                  CustomerCommunicationPreferenceRepository communicationPreferenceRepository) {
        this.accountRepository = accountRepository;
        this.ticketRepository = ticketRepository;
        this.messageRepository = messageRepository;
        this.approvalRepository = approvalRepository;
        this.orderRepository = orderRepository;
        this.notificationCenterService = notificationCenterService;
        this.feedbackRepository = feedbackRepository;
        this.csTaskRepository = csTaskRepository;
        this.playbookRepository = playbookRepository;
        this.playbookExecutionRepository = playbookExecutionRepository;
        this.healthSnapshotRepository = healthSnapshotRepository;
        this.slaRepository = slaRepository;
        this.escalationRepository = escalationRepository;
        this.contactReferenceRepository = contactReferenceRepository;
        this.communicationPreferenceRepository = communicationPreferenceRepository;
    }

    // --- 1. CLIENT DASHBOARD & PRODUCTION TRANSPARENCY ---

    public Map<String, Object> getClientDashboard(String clientCode) {
        String tenantId = TenantContext.getCurrentTenant();
        String code = clientCode != null ? clientCode : "CLI-APEX-01";

        List<ProductionOrder> orders = orderRepository.findByTenantId(tenantId);
        List<ClientApprovalRequest> approvals = approvalRepository.findByTenantIdAndClientCodeOrderByCreatedAtDesc(tenantId, code);
        List<SupportTicket> tickets = ticketRepository.findByTenantIdAndClientCodeOrderByCreatedAtDesc(tenantId, code);

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("tenantId", tenantId);
        dashboard.put("clientCode", code);
        dashboard.put("activeOrdersCount", orders.size());
        dashboard.put("pendingApprovalsCount", approvals.stream().filter(a -> "PENDING".equals(a.getStatus())).count());
        dashboard.put("openTicketsCount", tickets.stream().filter(t -> !"CLOSED".equals(t.getStatus()) && !"RESOLVED".equals(t.getStatus())).count());
        dashboard.put("orders", orders);
        return dashboard;
    }

    // --- 2. VERSION-AWARE CLIENT APPROVALS ---

    public List<ClientApprovalRequest> getClientApprovals(String clientCode) {
        String tenantId = TenantContext.getCurrentTenant();
        String code = clientCode != null ? clientCode : "CLI-APEX-01";
        List<ClientApprovalRequest> list = approvalRepository.findByTenantIdAndClientCodeOrderByCreatedAtDesc(tenantId, code);

        if (list.isEmpty()) {
            ClientApprovalRequest seeded = ClientApprovalRequest.builder()
                    .tenantId(tenantId)
                    .clientCode(code)
                    .documentId(1L)
                    .documentVersion("2.0") // Explicit Version Binding!
                    .approvalType("TECH_PACK")
                    .title("Approval Request for Men's Shirt Tech Pack v2.0")
                    .status("PENDING")
                    .build();
            approvalRepository.save(seeded);
            list = Collections.singletonList(seeded);
        }

        return list;
    }

    @Transactional
    public ClientApprovalRequest decideApproval(Long approvalId, String decision, String comments, String approvedBy) {
        String tenantId = TenantContext.getCurrentTenant();
        ClientApprovalRequest req = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new NoSuchElementException("Approval request not found: " + approvalId));

        req.setStatus(decision != null ? decision : "APPROVED");
        req.setClientComments(comments);
        req.setApprovedByEmail(approvedBy != null ? approvedBy : "client@brand.com");
        req.setDecidedAt(LocalDateTime.now());
        req = approvalRepository.save(req);

        // Module 9 Event Notification
        String idempotencyKey = "EVT-CLIENT-APPROVE-" + approvalId + "-" + System.currentTimeMillis();
        notificationCenterService.publishEvent(tenantId, "ApprovalCompletedEvent", idempotencyKey, "NORMAL", Map.of("orderNumber", req.getTitle(), "stageName", req.getStatus()));

        return req;
    }

    // --- 3. SUPPORT TICKETS, SLA & INTERNAL NOTE PRIVACY ---

    public List<SupportTicket> getSupportTickets(String clientCode) {
        String tenantId = TenantContext.getCurrentTenant();
        List<SupportTicket> tickets;
        if (clientCode != null && !clientCode.isEmpty()) {
            tickets = ticketRepository.findByTenantIdAndClientCodeOrderByCreatedAtDesc(tenantId, clientCode);
        } else {
            tickets = ticketRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        }

        if (tickets.isEmpty()) {
            SupportTicket t1 = SupportTicket.builder()
                    .tenantId(tenantId)
                    .ticketNumber("TKT-9011")
                    .clientCode(clientCode != null ? clientCode : "CLI-APEX-01")
                    .orderNumber("ORD-2026-88")
                    .subject("Stitching Line Spec Clarification")
                    .priority("HIGH")
                    .status("IN_PROGRESS")
                    .assignedToEmail("account.mgr@apex.com")
                    .build();
            ticketRepository.save(t1);
            tickets = Collections.singletonList(t1);
        }

        return tickets;
    }

    @Transactional
    public SupportTicket createTicket(SupportTicket ticket) {
        String tenantId = TenantContext.getCurrentTenant();
        ticket.setTenantId(tenantId);
        if (ticket.getTicketNumber() == null) {
            ticket.setTicketNumber("TKT-" + (1000 + (int)(Math.random() * 9000)));
        }

        // Apply SLA configurations
        Optional<TicketSLA> configuredSla = slaRepository.findByTenantIdAndPriority(tenantId, ticket.getPriority());
        int responseHours = 24;
        int resolutionHours = 48;
        if (configuredSla.isPresent()) {
            responseHours = configuredSla.get().getResponseTimeHours();
            resolutionHours = configuredSla.get().getResolutionTimeHours();
        } else {
            if ("CRITICAL".equalsIgnoreCase(ticket.getPriority())) {
                responseHours = 1;
                resolutionHours = 4;
            } else if ("HIGH".equalsIgnoreCase(ticket.getPriority())) {
                responseHours = 4;
                resolutionHours = 24;
            }
        }
        ticket.setResponseDueAt(LocalDateTime.now().plusHours(responseHours));
        ticket.setResolutionDueAt(LocalDateTime.now().plusHours(resolutionHours));

        ticket = ticketRepository.save(ticket);

        // Module 9 Notification
        String idempotencyKey = "EVT-NEW-TICKET-" + ticket.getId() + "-" + System.currentTimeMillis();
        notificationCenterService.publishEvent(tenantId, "NewTicketEvent", idempotencyKey, "HIGH", Map.of("orderNumber", ticket.getTicketNumber(), "stageName", ticket.getSubject()));

        return ticket;
    }

    public List<TicketMessage> getTicketMessages(Long ticketId, boolean isClientUser) {
        String tenantId = TenantContext.getCurrentTenant();
        if (isClientUser) {
            // STRICT PRIVACY: Filter out INTERNAL_NOTE entries for client API calls!
            return messageRepository.findByTenantIdAndTicketIdAndVisibilityScopeOrderByCreatedAtAsc(tenantId, ticketId, "CLIENT_VISIBLE");
        } else {
            // Internal staff sees both CLIENT_VISIBLE and INTERNAL_NOTE
            return messageRepository.findByTenantIdAndTicketIdOrderByCreatedAtAsc(tenantId, ticketId);
        }
    }

    @Transactional
    public TicketMessage addTicketMessage(Long ticketId, String messageText, String visibilityScope, String senderEmail) {
        String tenantId = TenantContext.getCurrentTenant();

        TicketMessage msg = TicketMessage.builder()
                .tenantId(tenantId)
                .ticketId(ticketId)
                .senderEmail(senderEmail != null ? senderEmail : "support@apex.com")
                .messageText(messageText)
                .visibilityScope(visibilityScope != null ? visibilityScope : "CLIENT_VISIBLE")
                .build();

        msg = messageRepository.save(msg);

        // If client-visible message, notify client via Module 9
        if ("CLIENT_VISIBLE".equals(msg.getVisibilityScope())) {
            String idempotencyKey = "EVT-TKT-MSG-" + msg.getId() + "-" + System.currentTimeMillis();
            notificationCenterService.publishEvent(tenantId, "TicketMessageEvent", idempotencyKey, "NORMAL", Map.of("orderNumber", "TKT-" + ticketId, "stageName", "New Message Received"));
        }

        return msg;
    }

    // --- 4. CUSTOMER ACCOUNTS & EXPLAINABLE HEALTH CALCULATOR ---

    public List<CustomerAccount> getCustomerAccounts() {
        String tenantId = TenantContext.getCurrentTenant();
        List<CustomerAccount> accounts = accountRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);

        if (accounts.isEmpty()) {
            accounts = Arrays.asList(
                CustomerAccount.builder().tenantId(tenantId).clientCode("CLI-APEX-01").companyName("Apex Retail Apparel").accountManagerEmail("account.mgr@apex.com").successManagerEmail("csm.lead@apex.com").tier("ENTERPRISE").healthStatus("HEALTHY").onTimeDeliveryPct(95.5).openIssuesCount(1).slaBreachesCount(0).build(),
                CustomerAccount.builder().tenantId(tenantId).clientCode("CLI-NORDIC-02").companyName("Nordic Wear Co.").accountManagerEmail("account.mgr@apex.com").successManagerEmail("csm.lead@apex.com").tier("GROWTH").healthStatus("AT_RISK").onTimeDeliveryPct(88.0).openIssuesCount(3).slaBreachesCount(1).build()
            );
            accountRepository.saveAll(accounts);
        }

        return accounts;
    }

    @Transactional
    public CustomerAccount evaluateCustomerHealth(String clientCode) {
        String tenantId = TenantContext.getCurrentTenant();
        CustomerAccount account = accountRepository.findByTenantIdAndClientCode(tenantId, clientCode)
                .orElseThrow(() -> new NoSuchElementException("Customer account not found: " + clientCode));

        // Health rules and explanation building
        StringBuilder explanation = new StringBuilder("Health calculated based on performance metrics: ");
        explanation.append(String.format("On-Time Delivery (%.1f%%), ", account.getOnTimeDeliveryPct()));
        explanation.append(String.format("Open Issues (%d), ", account.getOpenIssuesCount()));
        explanation.append(String.format("SLA Breaches (%d). ", account.getSlaBreachesCount()));

        if (account.getSlaBreachesCount() > 0 || account.getOnTimeDeliveryPct() < 90.0) {
            account.setHealthStatus("AT_RISK");
            explanation.append("Status flagged as AT_RISK due to SLA breaches or delivery drop.");
        } else if (account.getOpenIssuesCount() > 4) {
            account.setHealthStatus("CRITICAL");
            explanation.append("Status flagged as CRITICAL due to excessively high open issues count.");
        } else {
            account.setHealthStatus("HEALTHY");
            explanation.append("Metrics are within healthy ranges.");
        }

        // Save a historical health snapshot
        CustomerHealthSnapshot snapshot = CustomerHealthSnapshot.builder()
                .tenantId(tenantId)
                .clientCode(clientCode)
                .healthStatus(account.getHealthStatus())
                .onTimeDeliveryPct(account.getOnTimeDeliveryPct())
                .openIssuesCount(account.getOpenIssuesCount())
                .slaBreachesCount(account.getSlaBreachesCount())
                .explanation(explanation.toString())
                .build();
        healthSnapshotRepository.save(snapshot);

        return accountRepository.save(account);
    }

    // --- 5. TICKET ESCALATION & SLA SCHEDULER LOGIC ---

    @Transactional
    public TicketEscalation escalateTicket(Long ticketId, int level, String email) {
        String tenantId = TenantContext.getCurrentTenant();
        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NoSuchElementException("Ticket not found: " + ticketId));

        ticket.setStatus("IN_PROGRESS");
        ticket.setAssignedToEmail(email);
        ticketRepository.save(ticket);

        TicketEscalation escalation = TicketEscalation.builder()
                .tenantId(tenantId)
                .ticketId(ticketId)
                .escalationLevel(level)
                .escalatedToEmail(email)
                .escalatedAt(LocalDateTime.now())
                .status("ESCALATED")
                .build();

        // Publish escalation alert via Module 9
        String idempotencyKey = "EVT-ESC-" + ticketId + "-" + level + "-" + System.currentTimeMillis();
        notificationCenterService.publishEvent(tenantId, "TicketEscalatedEvent", idempotencyKey, "HIGH", 
                Map.of("orderNumber", ticket.getTicketNumber(), "stageName", "Escalated to level " + level));

        return escalationRepository.save(escalation);
    }

    @Transactional
    public void scanAndEscalateTickets() {
        String tenantId = TenantContext.getCurrentTenant();
        List<SupportTicket> openTickets = ticketRepository.findByTenantIdOrderByCreatedAtDesc(tenantId).stream()
                .filter(t -> !"CLOSED".equals(t.getStatus()) && !"RESOLVED".equals(t.getStatus()))
                .collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();
        for (SupportTicket ticket : openTickets) {
            if (ticket.getResponseDueAt() != null && now.isAfter(ticket.getResponseDueAt())) {
                // Determine escalation level
                List<TicketEscalation> escHistory = escalationRepository.findByTenantIdAndTicketId(tenantId, ticket.getId());
                if (escHistory.isEmpty()) {
                    // Level 1: CSM
                    escalateTicket(ticket.getId(), 1, "csm.lead@apex.com");
                } else if (escHistory.size() == 1 && now.isAfter(ticket.getResponseDueAt().plusHours(1))) {
                    // Level 2: Owner
                    escalateTicket(ticket.getId(), 2, "factory.owner@apex.com");
                }
            }
        }
    }

    // --- 6. CUSTOMER SUCCESS PLAYBOOKS & AUTOMATION ---

    @Transactional
    public CustomerPlaybookExecution triggerPlaybook(String clientCode, String triggerType) {
        String tenantId = TenantContext.getCurrentTenant();
        
        Optional<CustomerPlaybook> playbookOpt = playbookRepository.findByTenantIdAndActive(tenantId, true).stream()
                .filter(p -> triggerType.equalsIgnoreCase(p.getTriggerType()))
                .findFirst();

        if (playbookOpt.isEmpty()) {
            // Seed default playbook
            CustomerPlaybook defaultPlaybook = CustomerPlaybook.builder()
                    .tenantId(tenantId)
                    .name("Onboarding for New Client")
                    .triggerType("ONBOARDING")
                    .stepsJson("[{\"step\": 1, \"title\": \"Configure Portal Credentials\"}, {\"step\": 2, \"title\": \"Verify Tech Pack Approvals\"}]")
                    .active(true)
                    .build();
            playbookRepository.save(defaultPlaybook);
            playbookOpt = Optional.of(defaultPlaybook);
        }

        CustomerPlaybook playbook = playbookOpt.get();

        CustomerPlaybookExecution execution = CustomerPlaybookExecution.builder()
                .tenantId(tenantId)
                .playbookId(playbook.getId())
                .clientCode(clientCode)
                .status("ACTIVE")
                .currentStepIndex(0)
                .build();
        execution = playbookExecutionRepository.save(execution);

        // Generate playbooks task
        CustomerSuccessTask task = CustomerSuccessTask.builder()
                .tenantId(tenantId)
                .clientCode(clientCode)
                .title(playbook.getName() + " - Step 1 Task")
                .description("Automated Task generated by Customer Success Playbook: " + playbook.getName())
                .status("PENDING")
                .dueDate(LocalDateTime.now().plusDays(2))
                .build();
        csTaskRepository.save(task);

        return execution;
    }

    // --- 7. CUSTOMER FEEDBACK, CSAT & NPS SURVEY ENGINE ---

    @Transactional
    public CustomerFeedback recordFeedback(CustomerFeedback feedback) {
        String tenantId = TenantContext.getCurrentTenant();
        feedback.setTenantId(tenantId);
        return feedbackRepository.save(feedback);
    }

    public List<CustomerFeedback> getCustomerFeedback(String clientCode) {
        String tenantId = TenantContext.getCurrentTenant();
        if (clientCode != null && !clientCode.isEmpty()) {
            return feedbackRepository.findByTenantIdAndClientCodeOrderByCreatedAtDesc(tenantId, clientCode);
        }
        return feedbackRepository.findByTenantId(tenantId);
    }
}
