package com.msme.erp.config;

import com.msme.erp.domain.*;
import com.msme.erp.repository.*;
import com.msme.erp.service.WorkflowEngineService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final WorkflowRepository workflowRepository;
    private final WorkflowVersionRepository versionRepository;
    private final WorkflowStageRepository stageRepository;
    private final WorkflowEdgeRepository edgeRepository;
    private final ProductionOrderRepository orderRepository;
    private final OrderStageLogRepository logRepository;
    private final QCRecordRepository qcRepository;
    private final NotificationRepository notificationRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeAssignmentRepository employeeAssignmentRepository;
    private final WorkflowEngineService workflowEngineService;
    private final PasswordEncoder passwordEncoder;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final ClientDocumentRepository clientDocumentRepository;
    private final ProductionPhotoRepository productionPhotoRepository;
    private final ClientIssueRepository clientIssueRepository;
    private final SampleApprovalRepository sampleApprovalRepository;
    private final BrandChatRepository brandChatRepository;

    public DataSeeder(TenantRepository tenantRepository, UserRepository userRepository, BrandRepository brandRepository,
                      WorkflowRepository workflowRepository, WorkflowVersionRepository versionRepository,
                      WorkflowStageRepository stageRepository, WorkflowEdgeRepository edgeRepository,
                      ProductionOrderRepository orderRepository, OrderStageLogRepository logRepository,
                      QCRecordRepository qcRepository, NotificationRepository notificationRepository,
                      DepartmentRepository departmentRepository, EmployeeAssignmentRepository employeeAssignmentRepository,
                      WorkflowEngineService workflowEngineService, PasswordEncoder passwordEncoder,
                      InventoryItemRepository inventoryItemRepository, InventoryMovementRepository inventoryMovementRepository,
                      ClientDocumentRepository clientDocumentRepository, ProductionPhotoRepository productionPhotoRepository,
                      ClientIssueRepository clientIssueRepository, SampleApprovalRepository sampleApprovalRepository,
                      BrandChatRepository brandChatRepository) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.brandRepository = brandRepository;
        this.workflowRepository = workflowRepository;
        this.versionRepository = versionRepository;
        this.stageRepository = stageRepository;
        this.edgeRepository = edgeRepository;
        this.orderRepository = orderRepository;
        this.logRepository = logRepository;
        this.qcRepository = qcRepository;
        this.notificationRepository = notificationRepository;
        this.departmentRepository = departmentRepository;
        this.employeeAssignmentRepository = employeeAssignmentRepository;
        this.workflowEngineService = workflowEngineService;
        this.passwordEncoder = passwordEncoder;
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryMovementRepository = inventoryMovementRepository;
        this.clientDocumentRepository = clientDocumentRepository;
        this.productionPhotoRepository = productionPhotoRepository;
        this.clientIssueRepository = clientIssueRepository;
        this.sampleApprovalRepository = sampleApprovalRepository;
        this.brandChatRepository = brandChatRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Tenant tenant = tenantRepository.findByCompanyName("Apex Apparel & Textile Solutions")
                .orElseGet(() -> tenantRepository.save(Tenant.builder()
                        .companyName("Apex Apparel & Textile Solutions")
                        .subdomain("apex-textiles")
                        .industry("Apparel & Garment Manufacturing")
                        .subscriptionTier("ENTERPRISE_MSME")
                        .active(true)
                        .build()));
        String tenantId = tenant.getId();

        String encodedPass = passwordEncoder.encode("password123");

        if (userRepository.findByEmail("owner@apex.com").isEmpty()) {
            userRepository.save(User.builder()
                    .tenantId(tenantId)
                    .email("owner@apex.com")
                    .password(encodedPass)
                    .fullName("Rajesh Kumar")
                    .role(Role.ROLE_FACTORY_OWNER)
                    .active(true)
                    .build());
        }

        if (userRepository.findByEmail("operator@apex.com").isEmpty()) {
            userRepository.save(User.builder()
                    .tenantId(tenantId)
                    .email("operator@apex.com")
                    .password(encodedPass)
                    .fullName("Ramesh Sharma")
                    .role(Role.ROLE_OPERATOR)
                    .active(true)
                    .build());
        }

        if (userRepository.findByEmail("qc@apex.com").isEmpty()) {
            userRepository.save(User.builder()
                    .tenantId(tenantId)
                    .email("qc@apex.com")
                    .password(encodedPass)
                    .fullName("Priya Verma")
                    .role(Role.ROLE_QUALITY_INSPECTOR)
                    .active(true)
                    .build());
        }

        if (orderRepository.count() > 0) {
            return;
        }

        // 2. Create Departments
        Department cuttingDept = departmentRepository.save(new Department(null, tenantId, "Cutting Department", "CUTTING", false, null, null));
        Department printingDept = departmentRepository.save(new Department(null, tenantId, "Printing & Sublimation", "PRINTING", false, null, null));
        Department assemblyDept = departmentRepository.save(new Department(null, tenantId, "Assembly & Stitching", "ASSEMBLY", false, null, null));
        Department qcDept = departmentRepository.save(new Department(null, tenantId, "Quality Control", "QC", false, null, null));
        Department dispatchDept = departmentRepository.save(new Department(null, tenantId, "Logistics & Dispatch", "DISPATCH", false, null, null));

        // 5. Create Brands
        Brand brandNike = brandRepository.save(Brand.builder()
                .tenantId(tenantId)
                .name("Nike MSME Partner")
                .contactEmail("procurement@nike.com")
                .contactPhone("+1 800 555 0192")
                .portalAccessCode("NIKE-2026")
                .build());

        Brand brandAdidas = brandRepository.save(Brand.builder()
                .tenantId(tenantId)
                .name("Adidas Performance")
                .contactEmail("sourcing@adidas.com")
                .contactPhone("+49 9132 840")
                .portalAccessCode("ADI-2026")
                .build());

        // 6. Create Workflow & version with React Flow layouts
        Workflow workflow = workflowRepository.save(Workflow.builder()
                .tenantId(tenantId)
                .name("Garment Standard Workflow")
                .description("Production pipeline with fabric cutting, sublimation, assembly, quality control checkpoints, and dispatch")
                .industry("Garments")
                .currentVersion(1)
                .status("PUBLISHED")
                .build());

        String definitionJson = "{\"nodes\":[" +
                "{\"id\":\"n1\",\"type\":\"input\",\"data\":{\"label\":\"Order Received\"},\"position\":{\"x\":50,\"y\":150}}," +
                "{\"id\":\"n2\",\"type\":\"default\",\"data\":{\"label\":\"Fabric Cutting\"},\"position\":{\"x\":250,\"y\":150}}," +
                "{\"id\":\"n3\",\"type\":\"default\",\"data\":{\"label\":\"Printing & Sublimation\"},\"position\":{\"x\":450,\"y\":150}}," +
                "{\"id\":\"n4\",\"type\":\"default\",\"data\":{\"label\":\"Stitching & Assembly\"},\"position\":{\"x\":650,\"y\":150}}," +
                "{\"id\":\"n5\",\"type\":\"default\",\"data\":{\"label\":\"Quality Check (QC Gate)\"},\"position\":{\"x\":850,\"y\":150}}," +
                "{\"id\":\"n6\",\"type\":\"output\",\"data\":{\"label\":\"Dispatch\"},\"position\":{\"x\":1050,\"y\":150}}" +
                "],\"edges\":[" +
                "{\"id\":\"e1\",\"source\":\"n1\",\"target\":\"n2\"}," +
                "{\"id\":\"e2\",\"source\":\"n2\",\"target\":\"n3\"}," +
                "{\"id\":\"e3\",\"source\":\"n3\",\"target\":\"n4\"}," +
                "{\"id\":\"e4\",\"source\":\"n4\",\"target\":\"n5\"}," +
                "{\"id\":\"e5\",\"source\":\"n5\",\"target\":\"n6\"}" +
                "]}";

        WorkflowVersion version = versionRepository.save(WorkflowVersion.builder()
                .workflowId(workflow.getId())
                .versionNumber(1)
                .status("PUBLISHED")
                .definitionJson(definitionJson)
                .build());

        // Save stages
        WorkflowStage s1 = stageRepository.save(new WorkflowStage(null, version.getId(), "Order Received", "ORDER_RECEIVED", "Initial PO confirmation", 1, "START", "#3B82F6", 2, null, null));
        WorkflowStage s2 = stageRepository.save(new WorkflowStage(null, version.getId(), "Fabric Cutting", "CUTTING", "Raw fabric pattern slicing", 2, "NORMAL", "#8B5CF6", 12, null, cuttingDept.getId()));
        WorkflowStage s3 = stageRepository.save(new WorkflowStage(null, version.getId(), "Printing & Sublimation", "PRINTING", "Color sublimation printing", 3, "NORMAL", "#EC4899", 24, null, printingDept.getId()));
        WorkflowStage s4 = stageRepository.save(new WorkflowStage(null, version.getId(), "Stitching & Assembly", "ASSEMBLY", "Sewing panels together", 4, "NORMAL", "#F59E0B", 36, null, assemblyDept.getId()));
        WorkflowStage s5 = stageRepository.save(new WorkflowStage(null, version.getId(), "Quality Check (QC Gate)", "QC", "Dimensional and defect audit", 5, "QC", "#10B981", 6, null, qcDept.getId()));
        WorkflowStage s6 = stageRepository.save(new WorkflowStage(null, version.getId(), "Dispatch", "DISPATCH", "Courier handover", 6, "END", "#64748B", 2, null, dispatchDept.getId()));

        // Save edges
        edgeRepository.save(new WorkflowEdge(null, version.getId(), s1.getId(), s2.getId(), null));
        edgeRepository.save(new WorkflowEdge(null, version.getId(), s2.getId(), s3.getId(), null));
        edgeRepository.save(new WorkflowEdge(null, version.getId(), s3.getId(), s4.getId(), null));
        edgeRepository.save(new WorkflowEdge(null, version.getId(), s4.getId(), s5.getId(), null));
        edgeRepository.save(new WorkflowEdge(null, version.getId(), s5.getId(), s6.getId(), null));

        // 7. Save Sample Production Orders
        ProductionOrder order1 = orderRepository.save(ProductionOrder.builder()
                .tenantId(tenantId)
                .orderNumber("ORD-2026-101")
                .brandId(brandNike.getId())
                .productName("Dri-FIT Jerseys")
                .quantity(1000)
                .priority("HIGH")
                .status(OrderStatus.IN_PROGRESS)
                .currentStageId(s2.getId())
                .currentStageName(s2.getName())
                .currentStageSequence(s2.getSequenceOrder())
                .totalContractValue(15000.0)
                .paymentStatus("PARTIAL")
                .targetCompletionDate(LocalDateTime.now().plusDays(5))
                .estimatedDeliveryEta(LocalDateTime.now().plusDays(5))
                .notes("Critical order for Nike autumn promo.")
                .build());

        ProductionOrder order2 = orderRepository.save(ProductionOrder.builder()
                .tenantId(tenantId)
                .orderNumber("ORD-2026-102")
                .brandId(brandAdidas.getId())
                .productName("Primegreen Running Shorts")
                .quantity(500)
                .priority("MEDIUM")
                .status(OrderStatus.IN_PROGRESS)
                .currentStageId(s4.getId())
                .currentStageName(s4.getName())
                .currentStageSequence(s4.getSequenceOrder())
                .totalContractValue(8500.0)
                .paymentStatus("PAID")
                .targetCompletionDate(LocalDateTime.now().plusDays(3))
                .estimatedDeliveryEta(LocalDateTime.now().plusDays(3))
                .build());

        // Assign active production workflows
        workflowEngineService.assignWorkflowToOrder(order1.getId(), version.getId());
        workflowEngineService.assignWorkflowToOrder(order2.getId(), version.getId());

        // Setup some notifications
        notificationRepository.save(Notification.builder()
                .tenantId(tenantId)
                .category(NotificationCategory.MATERIAL_SHORTAGE)
                .title("Fabric shortage alert")
                .message("Low inventory of white dry-fit mesh fabric for order ORD-2026-101.")
                .orderNumber("ORD-2026-101")
                .readStatus(false)
                .build());

        // 8. Seed Inventory Items
        InventoryItem item1 = inventoryItemRepository.save(InventoryItem.builder()
                .tenantId(tenantId)
                .name("Dry-FIT Polyester Fabric (Roll)")
                .code("RM-POLY-01")
                .sku("SKU-RM-POLY-01")
                .barcode("BC-9921441")
                .category("RAW_MATERIAL")
                .supplierName("Apex Mills Corp")
                .unit("meters")
                .purchasePrice(8.50)
                .currentStock(800.0)
                .reservedStock(100.0)
                .warehouseName("Main Raw Warehouse")
                .rackLocation("Rack A-3")
                .batchNumber("B-99214")
                .safetyStock(100.0)
                .minStockAlert(50.0)
                .maxStockAlert(2000.0)
                .build());

        InventoryItem item2 = inventoryItemRepository.save(InventoryItem.builder()
                .tenantId(tenantId)
                .name("Sublimation Cyan Ink (kg)")
                .code("RM-CYAN-INK")
                .sku("SKU-RM-CYAN-INK")
                .barcode("BC-882104")
                .category("RAW_MATERIAL")
                .supplierName("DyeTech Solutions")
                .unit("kg")
                .purchasePrice(45.00)
                .currentStock(5.5) // triggers safety threshold alert
                .reservedStock(0.0)
                .warehouseName("Chemical Store Room")
                .rackLocation("Chemical Cabinet B")
                .batchNumber("B-7721")
                .safetyStock(10.0)
                .minStockAlert(5.0)
                .maxStockAlert(50.0)
                .build());

        InventoryItem item3 = inventoryItemRepository.save(InventoryItem.builder()
                .tenantId(tenantId)
                .name("Nike Swoosh Branding Patches")
                .code("CS-NIKE-PATCH")
                .sku("SKU-CS-NIKE-PATCH")
                .barcode("BC-1234567")
                .category("CLIENT_SUPPLIED")
                .supplierName("Nike Sourcing Division")
                .unit("pcs")
                .purchasePrice(0.0)
                .currentStock(400.0)
                .reservedStock(0.0)
                .warehouseName("Secured Trim Cage")
                .rackLocation("Box C-12")
                .clientBrandId(brandNike.getId())
                .safetyStock(50.0)
                .minStockAlert(10.0)
                .maxStockAlert(1000.0)
                .build());

        InventoryItem item4 = inventoryItemRepository.save(InventoryItem.builder()
                .tenantId(tenantId)
                .name("Shredded Polyester Trims (Scrap)")
                .code("SC-POLY-TRIM")
                .sku("SKU-SC-POLY-TRIM")
                .barcode("BC-SCRAP-99")
                .category("SCRAP")
                .supplierName("Internal Scrap Generation")
                .unit("kg")
                .purchasePrice(0.0)
                .currentStock(35.0)
                .reservedStock(0.0)
                .warehouseName("Scrap Yard")
                .rackLocation("Bin #4")
                .build());

        // 9. Seed Inventory Movements
        inventoryMovementRepository.save(InventoryMovement.builder()
                .tenantId(tenantId)
                .inventoryItemId(item1.getId())
                .movementType("RECEIVE")
                .quantity(800.0)
                .toWarehouse("Main Raw Warehouse")
                .operatorName("Ramesh Sharma")
                .remarks("Initial inventory seeder intake")
                .build());

        inventoryMovementRepository.save(InventoryMovement.builder()
                .tenantId(tenantId)
                .inventoryItemId(item2.getId())
                .movementType("RECEIVE")
                .quantity(15.0)
                .toWarehouse("Chemical Store Room")
                .operatorName("Ramesh Sharma")
                .remarks("Initial inventory seeder chemical intake")
                .build());

        inventoryMovementRepository.save(InventoryMovement.builder()
                .tenantId(tenantId)
                .inventoryItemId(item2.getId())
                .movementType("CONSUME")
                .quantity(9.5)
                .fromWarehouse("Chemical Store Room")
                .operatorName("Ramesh Sharma")
                .remarks("Batch run ORD-2026-101 cyan printing run consumption")
                .build());

        inventoryMovementRepository.save(InventoryMovement.builder()
                .tenantId(tenantId)
                .inventoryItemId(item3.getId())
                .movementType("RECEIVE")
                .quantity(400.0)
                .toWarehouse("Secured Trim Cage")
                .operatorName("Sarah Jenkins")
                .remarks("Client-supplied trim materials received for Dri-FIT Jerseys order")
                .build());

        // 10. Seed Brand Portal client resources (linked to order1 Nike Dri-FIT Jerseys)
        clientDocumentRepository.save(ClientDocument.builder()
                .tenantId(tenantId)
                .orderId(order1.getId())
                .name("Technical Spec Sheet - Dri-FIT Jersey.pdf")
                .type("SPEC_SHEET")
                .fileUrl("https://s3.amazonaws.com/mfgos/specs/nike-dri-fit.pdf")
                .uploadedBy("Rajesh Kumar")
                .build());

        clientDocumentRepository.save(ClientDocument.builder()
                .tenantId(tenantId)
                .orderId(order1.getId())
                .name("Apex Apparel Commercial Invoice #10214.pdf")
                .type("INVOICE")
                .fileUrl("https://s3.amazonaws.com/mfgos/invoices/nike-inv-10214.pdf")
                .uploadedBy("Rajesh Kumar")
                .build());

        productionPhotoRepository.save(ProductionPhoto.builder()
                .tenantId(tenantId)
                .orderId(order1.getId())
                .photoUrl("https://images.unsplash.com/photo-1558449028-b53a39d100fc?q=80&w=800")
                .caption("Fabric cutting room panel sliced patterns for Jersey chest sleeves.")
                .stageName("Fabric Cutting")
                .uploadedBy("Ramesh Sharma")
                .build());

        productionPhotoRepository.save(ProductionPhoto.builder()
                .tenantId(tenantId)
                .orderId(order1.getId())
                .photoUrl("https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=800")
                .caption("Dye sublimation ink prints applied to white dri-fit base chest panels.")
                .stageName("Printing & Sublimation")
                .uploadedBy("Ramesh Sharma")
                .build());

        clientIssueRepository.save(ClientIssue.builder()
                .tenantId(tenantId)
                .orderId(order1.getId())
                .title("Minor color mismatch on collar trims")
                .description("The blue collar rib trims received are slightly off-shade compared to the Pantone color specification (Pantone Reflex Blue). Please verify matches.")
                .status("OPEN")
                .severity("MEDIUM")
                .reportedBy("Sarah Jenkins")
                .build());

        sampleApprovalRepository.save(SampleApproval.builder()
                .tenantId(tenantId)
                .orderId(order1.getId())
                .sampleName("Jersey Front Panel Sublimation Logo Print Swatch")
                .status("PENDING")
                .build());

        brandChatRepository.save(BrandChat.builder()
                .tenantId(tenantId)
                .brandId(brandNike.getId())
                .senderName("Sarah Jenkins")
                .message("Hi Rajesh, do you have an updated ETA for the cutting batch handover?")
                .build());

        brandChatRepository.save(BrandChat.builder()
                .tenantId(tenantId)
                .brandId(brandNike.getId())
                .senderName("Rajesh Kumar")
                .message("Hi Sarah, fabric cutting is 80% complete. We expect to move to the sublimation section by tomorrow afternoon.")
                .build());
    }
}
