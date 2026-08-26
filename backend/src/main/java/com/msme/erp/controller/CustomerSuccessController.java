package com.msme.erp.controller;

import com.msme.erp.domain.*;
import com.msme.erp.service.CustomerSuccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

@RestController
@RequestMapping("/api/v1")
public class CustomerSuccessController {

    private final CustomerSuccessService customerSuccessService;

    public CustomerSuccessController(CustomerSuccessService customerSuccessService) {
        this.customerSuccessService = customerSuccessService;
    }

    @GetMapping("/client-portal/dashboard")
    public ResponseEntity<Map<String, Object>> getClientDashboard(@RequestParam(required = false, defaultValue = "CLI-APEX-01") String clientCode) {
        return ResponseEntity.ok(customerSuccessService.getClientDashboard(clientCode));
    }

    @GetMapping("/client-portal/approvals")
    public ResponseEntity<List<ClientApprovalRequest>> getClientApprovals(@RequestParam(required = false, defaultValue = "CLI-APEX-01") String clientCode) {
        return ResponseEntity.ok(customerSuccessService.getClientApprovals(clientCode));
    }

    @PostMapping("/client-portal/approvals/{id}/decide")
    public ResponseEntity<ClientApprovalRequest> decideApproval(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String decision = payload.getOrDefault("decision", "APPROVED");
        String comments = payload.get("comments");
        String approvedBy = payload.getOrDefault("approvedBy", "client@brand.com");
        return ResponseEntity.ok(customerSuccessService.decideApproval(id, decision, comments, approvedBy));
    }

    @PostMapping("/client-portal/approvals/{id}/approve")
    public ResponseEntity<ClientApprovalRequest> approveApproval(@PathVariable Long id, @RequestBody(required = false) Map<String, String> payload) {
        String comments = payload != null ? payload.getOrDefault("comments", "Approved") : "Approved";
        String approvedBy = payload != null ? payload.getOrDefault("approvedBy", "client@brand.com") : "client@brand.com";
        return ResponseEntity.ok(customerSuccessService.decideApproval(id, "APPROVED", comments, approvedBy));
    }

    @PostMapping("/client-portal/approvals/{id}/request-changes")
    public ResponseEntity<ClientApprovalRequest> requestChanges(@PathVariable Long id, @RequestBody(required = false) Map<String, String> payload) {
        String comments = payload != null ? payload.getOrDefault("comments", "Changes requested") : "Changes requested";
        String approvedBy = payload != null ? payload.getOrDefault("approvedBy", "client@brand.com") : "client@brand.com";
        return ResponseEntity.ok(customerSuccessService.decideApproval(id, "CHANGES_REQUESTED", comments, approvedBy));
    }

    @GetMapping("/client-portal/tickets")
    public ResponseEntity<List<SupportTicket>> getSupportTickets(@RequestParam(required = false) String clientCode) {
        return ResponseEntity.ok(customerSuccessService.getSupportTickets(clientCode));
    }

    @PostMapping("/client-portal/tickets")
    public ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicket ticket) {
        return ResponseEntity.ok(customerSuccessService.createTicket(ticket));
    }

    @GetMapping("/client-portal/tickets/{id}/messages")
    public ResponseEntity<List<TicketMessage>> getTicketMessages(@PathVariable Long id, @RequestParam(required = false, defaultValue = "true") boolean isClientUser) {
        return ResponseEntity.ok(customerSuccessService.getTicketMessages(id, isClientUser));
    }

    @PostMapping("/client-portal/tickets/{id}/messages")
    public ResponseEntity<TicketMessage> addTicketMessage(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String messageText = payload.getOrDefault("messageText", "Message response");
        String visibilityScope = payload.getOrDefault("visibilityScope", "CLIENT_VISIBLE");
        String senderEmail = payload.getOrDefault("senderEmail", "support@apex.com");
        return ResponseEntity.ok(customerSuccessService.addTicketMessage(id, messageText, visibilityScope, senderEmail));
    }

    @PostMapping("/client-portal/feedback")
    public ResponseEntity<CustomerFeedback> recordFeedback(@RequestBody CustomerFeedback feedback) {
        return ResponseEntity.ok(customerSuccessService.recordFeedback(feedback));
    }

    @GetMapping("/customer-success/accounts")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<CustomerAccount>> getCustomerAccounts() {
        return ResponseEntity.ok(customerSuccessService.getCustomerAccounts());
    }

    @PostMapping("/customer-success/accounts/{clientCode}/health")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<CustomerAccount> evaluateCustomerHealth(@PathVariable String clientCode) {
        return ResponseEntity.ok(customerSuccessService.evaluateCustomerHealth(clientCode));
    }

    @GetMapping("/customer-success/health")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<CustomerAccount>> getCustomerHealth() {
        return ResponseEntity.ok(customerSuccessService.getCustomerAccounts());
    }

    @GetMapping("/customer-success/sla")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> getSlaMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("slaCompliancePct", 98.4);
        metrics.put("breachedCount", 0);
        metrics.put("averageResolutionTimeHours", 4.2);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/customer-success/analytics")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Map<String, Object>> getCustomerSuccessAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("csatAvg", 4.8);
        analytics.put("npsScore", 82);
        analytics.put("onTimeDeliveryAvg", 95.5);
        analytics.put("ticketVolumeTrend", Arrays.asList(5, 3, 2, 4, 1));
        return ResponseEntity.ok(analytics);
    }

    @PostMapping("/customer-success/playbooks/configure")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<CustomerPlaybookExecution> triggerPlaybook(@RequestBody Map<String, String> payload) {
        String clientCode = payload.getOrDefault("clientCode", "CLI-APEX-01");
        String triggerType = payload.getOrDefault("triggerType", "ONBOARDING");
        return ResponseEntity.ok(customerSuccessService.triggerPlaybook(clientCode, triggerType));
    }

    @PostMapping("/customer-success/escalate/{ticketId}")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<TicketEscalation> manualEscalate(@PathVariable Long ticketId, @RequestBody Map<String, String> payload) {
        int level = Integer.parseInt(payload.getOrDefault("level", "1"));
        String email = payload.getOrDefault("email", "csm.lead@apex.com");
        return ResponseEntity.ok(customerSuccessService.escalateTicket(ticketId, level, email));
    }
}
