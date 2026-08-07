package com.msme.erp.service;

import com.msme.erp.domain.*;
import com.msme.erp.dto.*;
import com.msme.erp.repository.*;
import com.msme.erp.security.JwtUtils;
import com.msme.erp.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final WorkflowRepository workflowRepository;
    private final WorkflowVersionRepository versionRepository;
    private final WorkflowStageRepository stageRepository;
    private final WorkflowEdgeRepository edgeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(TenantRepository tenantRepository, UserRepository userRepository, BrandRepository brandRepository,
                       WorkflowRepository workflowRepository, WorkflowVersionRepository versionRepository,
                       WorkflowStageRepository stageRepository, WorkflowEdgeRepository edgeRepository,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.brandRepository = brandRepository;
        this.workflowRepository = workflowRepository;
        this.versionRepository = versionRepository;
        this.stageRepository = stageRepository;
        this.edgeRepository = edgeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public LoginResponse registerTenant(RegisterTenantRequest request) {
        if (tenantRepository.findByCompanyName(request.getCompanyName()).isPresent()) {
            throw new IllegalArgumentException("Company name is already registered");
        }
        if (tenantRepository.findBySubdomain(request.getSubdomain()).isPresent()) {
            throw new IllegalArgumentException("Subdomain is already taken");
        }
        if (userRepository.existsByEmail(request.getAdminEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // 1. Create Tenant
        Tenant tenant = Tenant.builder()
                .companyName(request.getCompanyName())
                .subdomain(request.getSubdomain())
                .industry(request.getIndustry())
                .subscriptionTier("ENTERPRISE_MSME")
                .active(true)
                .build();
        tenant = tenantRepository.save(tenant);

        // 2. Create Factory Owner Admin User
        User admin = User.builder()
                .tenantId(tenant.getId())
                .email(request.getAdminEmail())
                .password(passwordEncoder.encode(request.getAdminPassword()))
                .fullName(request.getAdminFullName())
                .role(Role.ROLE_FACTORY_OWNER)
                .active(true)
                .build();
        userRepository.save(admin);

        // 3. Create Default Drag-and-Drop Manufacturing Pipeline Stages
        Workflow workflow = workflowRepository.save(Workflow.builder()
                .tenantId(tenant.getId())
                .name("Standard MSME Manufacturing Pipeline")
                .description("Default pipeline with standard manufacturing operations")
                .industry(request.getIndustry() != null ? request.getIndustry() : "Garments")
                .currentVersion(1)
                .status("PUBLISHED")
                .build());

        WorkflowVersion version = versionRepository.save(WorkflowVersion.builder()
                .workflowId(workflow.getId())
                .versionNumber(1)
                .status("PUBLISHED")
                .build());

        WorkflowStage s1 = stageRepository.save(new WorkflowStage(null, version.getId(), "Order Received", "ORDER_RECEIVED", "Initial PO confirmation", 1, "START", "#3B82F6", 2, null, null));
        WorkflowStage s2 = stageRepository.save(new WorkflowStage(null, version.getId(), "Cutting", "CUTTING", "Raw pattern slicing", 2, "NORMAL", "#8B5CF6", 12, null, null));
        WorkflowStage s3 = stageRepository.save(new WorkflowStage(null, version.getId(), "Printing", "PRINTING", "Color printing", 3, "NORMAL", "#EC4899", 24, null, null));
        WorkflowStage s4 = stageRepository.save(new WorkflowStage(null, version.getId(), "Assembly", "ASSEMBLY", "Sewing and finishing", 4, "NORMAL", "#F59E0B", 36, null, null));
        WorkflowStage s5 = stageRepository.save(new WorkflowStage(null, version.getId(), "Quality Control", "QC", "Dimensional and defect audit", 5, "QC", "#10B981", 6, null, null));
        WorkflowStage s6 = stageRepository.save(new WorkflowStage(null, version.getId(), "Packing", "PACKING", "Eco packaging", 6, "NORMAL", "#06B6D4", 4, null, null));
        WorkflowStage s7 = stageRepository.save(new WorkflowStage(null, version.getId(), "Dispatch", "DISPATCH", "Courier handover", 7, "END", "#64748B", 2, null, null));

        edgeRepository.save(new WorkflowEdge(null, version.getId(), s1.getId(), s2.getId(), null));
        edgeRepository.save(new WorkflowEdge(null, version.getId(), s2.getId(), s3.getId(), null));
        edgeRepository.save(new WorkflowEdge(null, version.getId(), s3.getId(), s4.getId(), null));
        edgeRepository.save(new WorkflowEdge(null, version.getId(), s4.getId(), s5.getId(), null));
        edgeRepository.save(new WorkflowEdge(null, version.getId(), s5.getId(), s6.getId(), null));
        edgeRepository.save(new WorkflowEdge(null, version.getId(), s6.getId(), s7.getId(), null));

        // 4. Authenticate & Return JWT
        UserPrincipal principal = UserPrincipal.create(admin);
        String token = jwtUtils.generateToken(principal);

        return LoginResponse.builder()
                .token(token)
                .userId(admin.getId())
                .email(admin.getEmail())
                .fullName(admin.getFullName())
                .role(admin.getRole().name())
                .tenantId(tenant.getId())
                .tenantName(tenant.getCompanyName())
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtUtils.generateToken(principal);

        Tenant tenant = tenantRepository.findById(principal.getTenantId()).orElse(null);
        String tenantName = tenant != null ? tenant.getCompanyName() : "MSME Factory";

        return LoginResponse.builder()
                .token(token)
                .userId(principal.getId())
                .email(principal.getEmail())
                .fullName(principal.getFullName())
                .role(principal.getAuthorities().iterator().next().getAuthority())
                .tenantId(principal.getTenantId())
                .tenantName(tenantName)
                .brandId(principal.getBrandId())
                .build();
    }
}
