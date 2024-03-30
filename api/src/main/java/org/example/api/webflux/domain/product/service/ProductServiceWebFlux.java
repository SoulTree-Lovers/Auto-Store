package org.example.api.webflux.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.example.api.webflux.domain.product.repository.ProductEntityWebFlux;
import org.example.api.webflux.domain.product.repository.ProductRepositoryWebFlux;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceWebFlux {

    /**
     * TODO: 상품 CRUD 서비스 로직 구현
     */

    private final ProductRepositoryWebFlux productRepositoryWebFlux;

    // Create
    public Mono<ProductEntityWebFlux> create(
        Long adminId,
        String name,
        Long price,
        String category,
        String thumbnailUrl
    ) {

        return productRepositoryWebFlux.save(
            ProductEntityWebFlux.builder()
                .adminId(adminId)
                .name(name)
                .price(price)
                .category(category)
                .thumbnailUrl(thumbnailUrl)
                .build()
        );
    }

    // Read
    public Flux<ProductEntityWebFlux> findAll() {
        return productRepositoryWebFlux.findAll();
    }

    // Update
    public Mono<ProductEntityWebFlux> findById(Long id) {
        return productRepositoryWebFlux.findById(id);
    }

    // Delete
    public Mono<Void> deleteById(Long id) {
        return productRepositoryWebFlux.deleteById(id);
    }

}
