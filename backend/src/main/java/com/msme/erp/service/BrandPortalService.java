package com.msme.erp.service;

import com.msme.erp.config.TenantContext;
import com.msme.erp.domain.Brand;
import com.msme.erp.domain.ProductionOrder;
import com.msme.erp.dto.OrderDto;
import com.msme.erp.repository.BrandRepository;
import com.msme.erp.repository.ProductionOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandPortalService {

    private final BrandRepository brandRepository;
    private final ProductionOrderRepository orderRepository;
    private final OrderService orderService;

    public BrandPortalService(BrandRepository brandRepository, ProductionOrderRepository orderRepository, OrderService orderService) {
        this.brandRepository = brandRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public List<Brand> getBrandsForTenant() {
        String tenantId = TenantContext.getCurrentTenant();
        return brandRepository.findByTenantId(tenantId);
    }

    public List<OrderDto> getOrdersForClientBrand(String brandId) {
        String tenantId = TenantContext.getCurrentTenant();
        List<ProductionOrder> orders = orderRepository.findByTenantIdAndBrandId(tenantId, brandId);
        return orders.stream().map(orderService::mapToOrderDto).collect(Collectors.toList());
    }
}
