package org.example.api.mvc.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.example.api.mvc.domain.product.repository.ProductAliRepositoryMvc;
import org.example.api.mvc.domain.product.repository.ProductEntityFromAli;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductAliServiceMvc {

    /**
     * TODO: 상품 CRUD 서비스 로직 구현
     */

    private final ProductAliRepositoryMvc productAliRepositoryMvc;

    // Create
    public ProductEntityFromAli create(
        String productTitle,
        String appSalePrice,
        String originalPrice,
        String targetSalePrice,
        String productDetailUrl,
        String url0,
        String url1,
        String url2,
        String url3,
        String url4,
        String url5,
        String productMainImageUrl,
        String productVideoUrl,
        String evaluateRate,
        Integer lastestVolume,
        String discount,
        String shopUrl,
        String firstLevelCategoryName,
        String secondLevelCategoryName,
        String promotionLink
    ) {
        return productAliRepositoryMvc.save(
            ProductEntityFromAli.builder()
                .productTitle(productTitle)
                .appSalePrice(appSalePrice)
                .originalPrice(originalPrice)
                .targetSalePrice(targetSalePrice)
                .productDetailUrl(productDetailUrl)
                .url0(url0)
                .url1(url1)
                .url2(url2)
                .url3(url3)
                .url4(url4)
                .url5(url5)
                .productMainImageUrl(productMainImageUrl)
                .productVideoUrl(productVideoUrl)
                .evaluateRate(evaluateRate)
                .lastestVolume(lastestVolume)
                .discount(discount)
                .shopUrl(shopUrl)
                .firstLevelCategoryName(firstLevelCategoryName)
                .secondLevelCategoryName(secondLevelCategoryName)
                .promotionLink(promotionLink)
                .build()
        );
    }

    // Read
    public List<ProductEntityFromAli> findAll() {
        return productAliRepositoryMvc.findAll();
    }

    public ProductEntityFromAli findById(Long id) {
        return Optional.ofNullable(productAliRepositoryMvc.findById(id))
            .orElseThrow(() -> new RuntimeException("요청한 상품 ID가 존재하지 않습니다."))
            .get();
    }

    // Update
    public ProductEntityFromAli update(
        Long id,
        String productTitle,
        String targetSalePrice
    ) {
        ProductEntityFromAli product = productAliRepositoryMvc.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        });

        product.setProductTitle(productTitle);
        product.setTargetSalePrice(targetSalePrice);

        return productAliRepositoryMvc.save(product);
    }

    // Delete
    public void deleteById(Long id) {
        productAliRepositoryMvc.deleteById(id);
    }
}
