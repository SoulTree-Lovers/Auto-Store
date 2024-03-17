package org.example.business.repository.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "saleInformation")
public class NaverSaleInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * TODO: 네이버 스마트스토어에서 가져온 판매량에 대한 정보 속성 추가하기.
     */
}
