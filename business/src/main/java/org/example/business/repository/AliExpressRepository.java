package org.example.business.repository;

import org.example.business.repository.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AliExpressRepository extends JpaRepository<ProductEntity, Long> {

    /**
     * TODO: 알리 익스프레스에서 가져온 상품 정보를 DB에 저장하는 로직 추가하기
     */

}
