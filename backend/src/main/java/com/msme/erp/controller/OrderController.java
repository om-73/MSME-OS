package com.msme.erp.controller;

import com.msme.erp.dto.*;
import com.msme.erp.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_OPERATOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PostMapping("/{id}/transition")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_OPERATOR', 'ROLE_QUALITY_INSPECTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<OrderDto> transitionStage(@PathVariable String id, @RequestBody TransitionStageRequest request) {
        return ResponseEntity.ok(orderService.transitionStage(id, request));
    }

    @PostMapping("/qc-outcome")
    @PreAuthorize("hasAnyAuthority('ROLE_FACTORY_OWNER', 'ROLE_QUALITY_INSPECTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<QCRecordDto> submitQCOutcome(@RequestBody QCSubmitRequest request) {
        return ResponseEntity.ok(orderService.submitQCOutcome(request));
    }
}
