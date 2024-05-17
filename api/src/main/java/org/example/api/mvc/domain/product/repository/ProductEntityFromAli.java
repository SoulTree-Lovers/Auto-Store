package org.example.api.mvc.domain.product.repository;

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
@EntityListeners(AuditingEntityListener.class) // @CreatedDate, @LastModifiedDate 사용 가능하도록 리스너 등록
public class ProductEntityFromAli {
    /**
     * DB에 넣을 상품들의 정보가 담긴 테이블 (알리 익스프레스 required 속성 저장)
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

    @Column(columnDefinition = "TEXT")
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

}