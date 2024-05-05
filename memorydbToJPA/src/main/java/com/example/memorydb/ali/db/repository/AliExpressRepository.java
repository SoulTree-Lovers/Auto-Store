package com.example.memorydb.ali.db.repository;

import com.example.memorydb.ali.db.entity.ProductEntityFromAli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AliExpressRepository extends JpaRepository<ProductEntityFromAli, Long> {

    /**
     * TODO: 알리 익스프레스에서 가져온 상품 정보를 DB에 저장하는 로직 추가하기
     */

}