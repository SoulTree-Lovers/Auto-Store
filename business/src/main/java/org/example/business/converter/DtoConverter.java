package org.example.business.converter;

import jakarta.persistence.Converter;
import org.example.business.repository.entity.ProductEntity;

@Converter
public class DtoConverter {

    /**
     * TODO: 알리 익스프레스 DTO를 Product Entity로 변환하는 코드 작성하기.
     */

    public ProductEntity aliExpressDtoToProductEntity() {
        return ProductEntity.builder()
            // TODO: 속성 추가
            .build();
    }

    // Temu도 추가할 경우 메소드 추가
}
