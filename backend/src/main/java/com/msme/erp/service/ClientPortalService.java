package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.*;
import com.msme.erp.dto.ClientPortalDto;
import com.msme.erp.dto.WorkflowLogDto;
import com.msme.erp.repository.*;
import com.msme.erp.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientPortalService {

    private final ProductionOrderRepository orderRepository;
    private final WorkflowEngineService workflowEngineService;
    private final ClientDocumentRepository documentRepository;
    private final ProductionPhotoRepository photoRepository;
    private final ClientIssueRepository issueRepository;
    private final SampleApprovalRepository approvalRepository;
    private final BrandChatRepository chatRepository;

    public ClientPortalService(ProductionOrderRepository orderRepository, 
                               WorkflowEngineService workflowEngineService,
                               ClientDocumentRepository documentRepository, 
                               ProductionPhotoRepository photoRepository, 
                               ClientIssueRepository issueRepository, 
                               SampleApprovalRepository approvalRepository, 
                               BrandChatRepository chatRepository) {
        this.orderRepository = orderRepository;
        this.workflowEngineService = workflowEngineService;
        this.documentRepository = documentRepository;
        this.photoRepository = photoRepository;
        this.issueRepository = issueRepository;
        this.approvalRepository = approvalRepository;
        this.chatRepository = chatRepository;
    }

    public ClientPortalDto getClientOrderSnapshot(String orderId) {
        String tenantId = TenantContext.getCurrentTenant();
        ProductionOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));

        // Security boundary check: client must only see their own orders
        String userBrandId = getCurrentUserBrandId();
        boolean isClient = getCurrentUserAuthorities().contains("ROLE_BRAND_CLIENT");
        if (isClient && userBrandId != null && !userBrandId.equals(order.getBrandId())) {
            throw new IllegalArgumentException("Access denied to orders from other brands.");
        }

        List<WorkflowLogDto> timeline = workflowEngineService.getOrderWorkflowHistory(orderId);
        List<ClientDocument> documents = documentRepository.findByTenantIdAndOrderId(tenantId, orderId);
        List<ProductionPhoto> photos = photoRepository.findByTenantIdAndOrderIdOrderByCreatedAtDesc(tenantId, orderId);
        List<ClientIssue> issues = issueRepository.findByTenantIdAndOrderIdOrderByCreatedAtDesc(tenantId, orderId);
        List<SampleApproval> approvals = approvalRepository.findByTenantIdAndOrderId(tenantId, orderId);

        return ClientPortalDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .status(order.getStatus().name())
                .currentStageName(order.getCurrentStageName())
                .estimatedDeliveryEta(order.getEstimatedDeliveryEta())
                .paymentStatus(order.getPaymentStatus())
                .totalContractValue(order.getTotalContractValue())
                .timeline(timeline)
                .documents(documents)
                .photos(photos)
                .issues(issues)
                .approvals(approvals)
                .build();
    }

    @Transactional
    public ClientIssue submitIssue(String orderId, String title, String description, String severity) {
        String tenantId = TenantContext.getCurrentTenant();
        String operator = getCurrentUserFullName();

        ClientIssue issue = ClientIssue.builder()
                .tenantId(tenantId)
                .orderId(orderId)
                .title(title)
                .description(description)
                .status("OPEN")
                .severity(severity != null ? severity : "MEDIUM")
                .reportedBy(operator)
                .build();

        return issueRepository.save(issue);
    }

    @Transactional
    public SampleApproval respondToSampleApproval(String approvalId, String status, String comments) {
        SampleApproval approval = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new NoSuchElementException("Sample approval record not found: " + approvalId));

        approval.setStatus(status); // APPROVED, REJECTED
        approval.setComments(comments);
        approval.setRespondedAt(LocalDateTime.now());

        return approvalRepository.save(approval);
    }

    @Transactional
    public BrandChat postChatMessage(String brandId, String message) {
        String tenantId = TenantContext.getCurrentTenant();
        String operator = getCurrentUserFullName();

        BrandChat chat = BrandChat.builder()
                .tenantId(tenantId)
                .brandId(brandId)
                .senderName(operator)
                .message(message)
                .build();

        return chatRepository.save(chat);
    }

    public List<BrandChat> getChatMessages(String brandId) {
        String tenantId = TenantContext.getCurrentTenant();
        return chatRepository.findByTenantIdAndBrandIdOrderByTimestampAsc(tenantId, brandId);
    }

    private String getCurrentUserBrandId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            return p.getBrandId();
        }
        return null;
    }

    private List<String> getCurrentUserAuthorities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private String getCurrentUserFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            return p.getFullName();
        }
        return "System Guest";
    }
}
