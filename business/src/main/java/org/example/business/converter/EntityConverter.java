package org.example.business.converter;

import jakarta.persistence.Converter;
import org.example.business.dto.NaverDto;
import org.example.business.repository.entity.ProductEntity;

@Converter
public class EntityConverter {

    /**
     * TODO: DB의 상품 정보 Entity를 NaverDto로 변환하는 코드 작성하기.
     */

    public NaverDto productEntityToNaverDto(ProductEntity productEntity) {
        return NaverDto.builder()
            // TODO: 속성 추가하기
            .build();
    }


}
