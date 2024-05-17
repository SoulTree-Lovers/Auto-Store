package org.example.api.mvc.domain.product.converter;

import jakarta.persistence.Converter;
import org.example.api.mvc.domain.product.controller.model.ProductResponseAliMvc;
import org.example.api.mvc.domain.product.controller.model.ProductResponseMvc;
import org.example.api.mvc.domain.product.repository.ProductEntityFromAli;
import org.example.api.mvc.domain.product.repository.ProductEntityMvc;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Converter
@Component
public class ProductConverterAli {

    public ProductResponseAliMvc toResponse(ProductEntityFromAli productEntityFromAli) {

        return Optional.ofNullable(productEntityFromAli)
            .map(it -> {
                return ProductResponseAliMvc.builder()
                    .id(it.getId())
                    .productTitle(it.getProductTitle())
                    .appSalePrice(it.getAppSalePrice())
                    .originalPrice(it.getOriginalPrice())
                    .targetSalePrice(it.getTargetSalePrice())
                    .productDetailUrl(it.getProductDetailUrl())
                    .url0(it.getUrl0())
                    .url1(it.getUrl1())
                    .url2(it.getUrl2())
                    .url3(it.getUrl3())
                    .url4(it.getUrl4())
                    .url5(it.getUrl5())
                    .productMainImageUrl(it.getProductMainImageUrl())
                    .productVideoUrl(it.getProductVideoUrl())
                    .evaluateRate(it.getEvaluateRate())
                    .lastestVolume(it.getLastestVolume())
                    .discount(it.getDiscount())
                    .shopUrl(it.getShopUrl())
                    .firstLevelCategoryName(it.getFirstLevelCategoryName())
                    .secondLevelCategoryName(it.getSecondLevelCategoryName())
                    .promotionLink(it.getPromotionLink())
                    .createdAt(it.getCreatedAt())
                    .updatedAt(it.getUpdatedAt())
                    .build();
            })
            .orElseThrow(() -> new RuntimeException("Ali Converter to Response() Error"));

    }

}
