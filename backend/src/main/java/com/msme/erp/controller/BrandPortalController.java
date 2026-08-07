package com.msme.erp.controller;

import com.msme.erp.domain.Brand;
import com.msme.erp.dto.OrderDto;
import com.msme.erp.service.BrandPortalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brand-portal")
public class BrandPortalController {

    private final BrandPortalService brandPortalService;

    public BrandPortalController(BrandPortalService brandPortalService) {
        this.brandPortalService = brandPortalService;
    }

    @GetMapping("/brands")
    public ResponseEntity<List<Brand>> getBrands() {
        return ResponseEntity.ok(brandPortalService.getBrandsForTenant());
    }

    @GetMapping("/brands/{brandId}/orders")
    public ResponseEntity<List<OrderDto>> getOrdersForBrand(@PathVariable String brandId) {
        return ResponseEntity.ok(brandPortalService.getOrdersForClientBrand(brandId));
    }
}
