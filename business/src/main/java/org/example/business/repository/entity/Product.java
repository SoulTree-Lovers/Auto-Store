package org.example.business.repository.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    /**
     * DB에 넣을 상품들의 정보가 담긴 테이블
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * TODO: 이름, 별점, 가격 등에 대한 정보 속성 추가하기.
     */
}
