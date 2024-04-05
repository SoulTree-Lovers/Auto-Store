package org.example.api.mvc.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.mvc.domain.product.controller.model.ProductRequestMvc;
import org.example.api.mvc.domain.product.controller.model.ProductResponseMvc;
import org.example.api.mvc.domain.product.converter.ProductConverter;
import org.example.api.mvc.domain.product.service.ProductServiceMvc;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mvc/product")
public class ProductControllerMvc {

    /**
     * TODO: 상품 CRUD 요청 구현
     */

    private final ProductServiceMvc productServiceMvc;
    private final ProductConverter productConverter;

    // Create
    @PostMapping("/create")
    public ProductResponseMvc createProduct(
        @RequestBody ProductRequestMvc request
        ) {

        var productEntity = productServiceMvc.create(
            request.getAdminId(),
            request.getName(),
            request.getPrice(),
            request.getCategory(),
            request.getThumbnailUrl()
        );

        return productConverter.toResponse(productEntity);

    }

    // Read
    @GetMapping("/find-all")
    public List<ProductResponseMvc> findAllProduct() {
        return productServiceMvc.findAll().stream()
            .map(productConverter::toResponse)
            .toList();
    }

    @GetMapping("/find/{id}")
    public ProductResponseMvc findProductById(
        @PathVariable Long id
    ) {
        var productEntity = productServiceMvc.findById(id);
        return productConverter.toResponse(productEntity);
    }

    // Update
    @PutMapping("/update/{id}")
    public ProductResponseMvc updateProductById(
        @PathVariable Long id,
        @RequestBody ProductRequestMvc productRequestMvc
    ) {
        var productEntity = productServiceMvc.update(
            id,
            productRequestMvc.getAdminId(),
            productRequestMvc.getName(),
            productRequestMvc.getPrice(),
            productRequestMvc.getCategory(),
            productRequestMvc.getThumbnailUrl()
        );

        return productConverter.toResponse(productEntity);
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(
        @PathVariable Long id
    ) {
        productServiceMvc.deleteById(id);
    }
}
