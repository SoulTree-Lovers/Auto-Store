package org.example.api.mvc.domain.product.repository;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "product_mvc")
@EntityListeners(AuditingEntityListener.class) // @CreatedDate, @LastModifiedDate 사용 가능하도록 리스너 등록
public class ProductEntityMvc {

    @jakarta.persistence.Id
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id", nullable = false)
    private Long adminId; // 판매자 아이디

    @Column(length = 100, nullable = false)
    private String name; // 상품 이름

    @Column(nullable = false)
    private Long price; // 상품 가격

    @Column(length = 100, nullable = false)
    private String category; // 상품 카테고리

    @Column(name = "thumbnail_url", length = 200, nullable = false)
    private String thumbnailUrl; // 상품 썸네일 이미지 주소

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
