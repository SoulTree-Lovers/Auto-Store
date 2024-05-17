package org.example.api.mvc.domain.product.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseAliMvc {

    private Long id;

    private String productTitle;

    private String appSalePrice;

    private String originalPrice;

    private String targetSalePrice;

    private String productDetailUrl;

    private String url0;

    private String url1;

    private String url2;

    private String url3;

    private String url4;

    private String url5;

    private String productMainImageUrl;

    private String productVideoUrl;

    private String evaluateRate;

    private Integer lastestVolume;

    private String discount;

    private String shopUrl;

    private String firstLevelCategoryName;

    private String secondLevelCategoryName;

    private String promotionLink;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
