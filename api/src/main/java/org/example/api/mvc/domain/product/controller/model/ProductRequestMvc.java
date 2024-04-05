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
public class ProductRequestMvc {

    private Long adminId; // 판매자 아이디

    private String name; // 상품 이름

    private Long price; // 상품 가격

    private String category; // 상품 카테고리

    private String thumbnailUrl; // 상품 썸네일 이미지 주소

//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;

}
