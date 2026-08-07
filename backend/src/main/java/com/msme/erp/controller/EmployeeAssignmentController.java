package com.msme.erp.controller;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.EmployeeAssignment;
import com.msme.erp.domain.User;
import com.msme.erp.domain.Department;
import com.msme.erp.dto.EmployeeAssignmentDto;
import com.msme.erp.repository.EmployeeAssignmentRepository;
import com.msme.erp.repository.UserRepository;
import com.msme.erp.repository.DepartmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees/assignments")
public class EmployeeAssignmentController {

    private final EmployeeAssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeAssignmentController(EmployeeAssignmentRepository assignmentRepository,
                                        UserRepository userRepository,
                                        DepartmentRepository departmentRepository) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeAssignmentDto>> getAllAssignments() {
        String tenantId = TenantContext.getCurrentTenant();
        List<EmployeeAssignment> list = assignmentRepository.findByTenantId(tenantId);
        List<EmployeeAssignmentDto> response = list.stream().map(a -> {
            String userName = userRepository.findById(a.getUserId()).map(User::getFullName).orElse("Unknown");
            String deptName = departmentRepository.findById(a.getDepartmentId()).map(Department::getName).orElse("Unknown");
            return new EmployeeAssignmentDto(a.getId(), a.getUserId(), userName, a.getDepartmentId(), deptName);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<EmployeeAssignmentDto> createAssignment(@RequestBody Map<String, String> payload) {
        String tenantId = TenantContext.getCurrentTenant();
        String userId = payload.get("userId");
        String departmentId = payload.get("departmentId");

        EmployeeAssignment assign = assignmentRepository.findByUserIdAndDepartmentId(userId, departmentId)
                .orElseGet(() -> {
                    EmployeeAssignment a = EmployeeAssignment.builder()
                            .tenantId(tenantId)
                            .userId(userId)
                            .departmentId(departmentId)
                            .build();
                    return assignmentRepository.save(a);
                });

        String userName = userRepository.findById(userId).map(User::getFullName).orElse("Unknown");
        String deptName = departmentRepository.findById(departmentId).map(Department::getName).orElse("Unknown");

        return ResponseEntity.ok(new EmployeeAssignmentDto(assign.getId(), userId, userName, departmentId, deptName));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> removeAssignment(@RequestParam String userId, @RequestParam String departmentId) {
        assignmentRepository.deleteByUserIdAndDepartmentId(userId, departmentId);
        return ResponseEntity.noContent().build();
    }
}
