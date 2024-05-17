package org.example.api.mvc.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.mvc.domain.product.controller.model.*;
import org.example.api.mvc.domain.product.converter.ProductConverterAli;
import org.example.api.mvc.domain.product.service.ProductAliServiceMvc;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ali/mvc/product")
public class ProductAliControllerMvc {

    /**
     * TODO: 상품 CRUD 요청 구현
     */

    private final ProductAliServiceMvc productAliServiceMvc;
    private final ProductConverterAli productConverterAli;

    // Create
    @PostMapping("/create")
    public ProductResponseAliMvc createProduct(
        @RequestBody ProductRequestAliMvc request
    ) {

        var productEntityFromAli = productAliServiceMvc.create(
            request.getProductTitle(),
            request.getAppSalePrice(),
            request.getOriginalPrice(),
            request.getTargetSalePrice(),
            request.getProductDetailUrl(),
            request.getUrl0(),
            request.getUrl1(),
            request.getUrl2(),
            request.getUrl3(),
            request.getUrl4(),
            request.getUrl5(),
            request.getProductMainImageUrl(),
            request.getProductVideoUrl(),
            request.getEvaluateRate(),
            request.getLastestVolume(),
            request.getDiscount(),
            request.getShopUrl(),
            request.getFirstLevelCategoryName(),
            request.getSecondLevelCategoryName(),
            request.getPromotionLink()
        );

        return productConverterAli.toResponse(productEntityFromAli);

    }

    // Read
    @GetMapping("/find-all")
    public List<ProductResponseAliMvc> findAllProduct() {
        return productAliServiceMvc.findAll().stream()
            .map(productConverterAli::toResponse)
            .toList();
    }

    @GetMapping("/find/{id}")
    public ProductResponseAliMvc findProductById(
        @PathVariable Long id
    ) {
        var productEntityFromAli = productAliServiceMvc.findById(id);
        return productConverterAli.toResponse(productEntityFromAli);
    }

    // Update
    @PutMapping("/update/{id}")
    public ProductResponseAliMvc updateProductById(
        @PathVariable Long id,
        @RequestBody ProductUpdateRequestAliMvc productUpdateRequestAliMvc
    ) {
        var productEntityFromAli = productAliServiceMvc.update(
            id,
            productUpdateRequestAliMvc.getProductTitle(),
            productUpdateRequestAliMvc.getTargetSalePrice()
        );

        return productConverterAli.toResponse(productEntityFromAli);
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(
        @PathVariable Long id
    ) {
        productAliServiceMvc.deleteById(id);
    }
}