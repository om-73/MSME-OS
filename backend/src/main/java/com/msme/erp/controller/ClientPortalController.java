package com.msme.erp.controller;

import com.msme.erp.domain.BrandChat;
import com.msme.erp.domain.ClientIssue;
import com.msme.erp.domain.SampleApproval;
import com.msme.erp.dto.ClientPortalDto;
import com.msme.erp.service.ClientPortalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/client")
public class ClientPortalController {

    private final ClientPortalService clientPortalService;

    public ClientPortalController(ClientPortalService clientPortalService) {
        this.clientPortalService = clientPortalService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ClientPortalDto> getClientOrderSnapshot(@PathVariable String orderId) {
        return ResponseEntity.ok(clientPortalService.getClientOrderSnapshot(orderId));
    }

    @PostMapping("/order/{orderId}/issue")
    @PreAuthorize("hasAnyAuthority('ROLE_BRAND_CLIENT', 'ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<ClientIssue> submitIssue(@PathVariable String orderId, @RequestBody Map<String, String> payload) {
        String title = payload.get("title");
        String description = payload.get("description");
        String severity = payload.getOrDefault("severity", "MEDIUM");
        return ResponseEntity.ok(clientPortalService.submitIssue(orderId, title, description, severity));
    }

    @PostMapping("/approval/{id}/respond")
    @PreAuthorize("hasAnyAuthority('ROLE_BRAND_CLIENT', 'ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<SampleApproval> respondToSampleApproval(@PathVariable String id, @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        String comments = payload.getOrDefault("comments", "");
        return ResponseEntity.ok(clientPortalService.respondToSampleApproval(id, status, comments));
    }

    @PostMapping("/chat")
    @PreAuthorize("hasAnyAuthority('ROLE_BRAND_CLIENT', 'ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<BrandChat> postChatMessage(@RequestBody Map<String, String> payload) {
        String brandId = payload.get("brandId");
        String message = payload.get("message");
        return ResponseEntity.ok(clientPortalService.postChatMessage(brandId, message));
    }

    @GetMapping("/chat/{brandId}")
    public ResponseEntity<List<BrandChat>> getChatMessages(@PathVariable String brandId) {
        return ResponseEntity.ok(clientPortalService.getChatMessages(brandId));
    }
}
