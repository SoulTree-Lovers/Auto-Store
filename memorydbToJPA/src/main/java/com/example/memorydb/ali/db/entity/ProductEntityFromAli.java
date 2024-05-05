package com.example.memorydb.ali.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
@Data
public class ProductEntityFromAli {
    /**
     * DB에 넣을 상품들의 정보가 담긴 테이블
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /**
     * TODO: 이름, 별점, 가격 등에 대한 정보 속성 추가하기.
     */
}