package com.msme.erp.controller;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.Department;
import com.msme.erp.repository.DepartmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        String tenantId = TenantContext.getCurrentTenant();
        return ResponseEntity.ok(departmentRepository.findByTenantIdAndDeletedFalse(tenantId));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Department> createDepartment(@RequestBody Map<String, String> payload) {
        String tenantId = TenantContext.getCurrentTenant();
        String name = payload.get("name");
        String code = payload.getOrDefault("code", name.toUpperCase().replace(" ", "_"));

        Department dept = Department.builder()
                .tenantId(tenantId)
                .name(name)
                .code(code)
                .build();
        return ResponseEntity.ok(departmentRepository.save(dept));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable String id) {
        String tenantId = TenantContext.getCurrentTenant();
        Department dept = departmentRepository.findByIdAndTenantIdAndDeletedFalse(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        dept.setDeleted(true);
        departmentRepository.save(dept);
        return ResponseEntity.noContent().build();
    }
}
