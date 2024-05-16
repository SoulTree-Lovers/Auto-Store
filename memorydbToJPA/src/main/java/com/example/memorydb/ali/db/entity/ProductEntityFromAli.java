package com.example.memorydb.ali.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
@Data
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class ProductEntityFromAli {
    /**
     * DB에 넣을 상품들의 정보가 담긴 테이블
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "app_sale_price")
    private String appSalePrice;

    @Column(name = "original_price")
    private String originalPrice;

    @Column(name = "target_sale_price")
    private String targetSalePrice;

    @Column(name = "product_detail_url", columnDefinition = "TEXT")
    private String productDetailUrl;

    @Column(columnDefinition = "TEXT")
    private String url0;

    @Column(columnDefinition = "TEXT")
    private String url1;

    @Column(columnDefinition = "TEXT")
    private String url2;

    @Column(columnDefinition = "TEXT")
    private String url3;

    @Column(columnDefinition = "TEXT")
    private String url4;

    @Column(columnDefinition = "TEXT")
    private String url5;

    @Column(name = "product_main_image_url", columnDefinition = "TEXT")
    private String productMainImageUrl;

    @Column(name = "product_video_url", columnDefinition = "TEXT")
    private String productVideoUrl;

    @Column(name = "evaluate_rate")
    private String evaluateRate;

    @Column(name = "lastest_volume")
    private Integer lastestVolume;

    private String discount;

    @Column(name = "shop_url", columnDefinition = "TEXT")
    private String shopUrl;

    @Column(name = "first_level_category_name")
    private String firstLevelCategoryName;

    @Column(name = "second_level_category_name")
    private String secondLevelCategoryName;

    @Column(name = "promotion_link", columnDefinition = "TEXT")
    private String promotionLink;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ProductEntityFromAli(Long id, String productTitle, String appSalePrice, String originalPrice, String targetSalePrice, String productDetailUrl, String url0, String url1, String url2, String url3, String url4, String url5, String productMainImageUrl, String productVideoUrl, String evaluateRate, Integer lastestVolume, String discount, String shopUrl, String firstLevelCategoryName, String secondLevelCategoryName, String promotionLink) {
        this.id = id;
        this.productTitle = productTitle;
        this.appSalePrice = appSalePrice;
        this.originalPrice = originalPrice;
        this.targetSalePrice = targetSalePrice;
        this.productDetailUrl = productDetailUrl;
        this.url0 = url0;
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
        this.url4 = url4;
        this.url5 = url5;
        this.productMainImageUrl = productMainImageUrl;
        this.productVideoUrl = productVideoUrl;
        this.evaluateRate = evaluateRate;
        this.lastestVolume = lastestVolume;
        this.discount = discount;
        this.shopUrl = shopUrl;
        this.firstLevelCategoryName = firstLevelCategoryName;
        this.secondLevelCategoryName = secondLevelCategoryName;
        this.promotionLink = promotionLink;
    }


    /**
     * TODO: 이름, 별점, 가격 등에 대한 정보 속성 추가하기.
     */
}