package org.example.api.mvc.domain.product.converter;

import jakarta.persistence.Converter;
import org.example.api.mvc.domain.product.controller.model.ProductResponseMvc;
import org.example.api.mvc.domain.product.repository.ProductEntityMvc;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Converter
@Component
public class ProductConverter {

    public ProductResponseMvc toResponse(ProductEntityMvc productEntityMvc) {

        return Optional.ofNullable(productEntityMvc)
            .map(it -> {
                return ProductResponseMvc.builder()
                    .id(productEntityMvc.getId())
                    .adminId(productEntityMvc.getAdminId())
                    .name(productEntityMvc.getName())
                    .price(productEntityMvc.getPrice())
                    .category(productEntityMvc.getCategory())
                    .thumbnailUrl(productEntityMvc.getThumbnailUrl())
                    .createdAt(productEntityMvc.getCreatedAt())
                    .updatedAt(productEntityMvc.getUpdatedAt())
                    .build();
            })
            .orElseThrow(() -> new RuntimeException("Converter toRespose() Error"));

    }

}
