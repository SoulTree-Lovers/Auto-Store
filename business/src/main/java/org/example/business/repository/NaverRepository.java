package org.example.business.repository;

import org.example.business.repository.entity.NaverSaleInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaverRepository extends JpaRepository<NaverSaleInformation, Long> {

    /**
     * TODO: 네이버 스마트 스토어에서 가져온 상품 판매량 정보를 DB에 저장하는 로직 추가하기.
     */

}
