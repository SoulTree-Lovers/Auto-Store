package org.example.api.webflux.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.example.api.mvc.domain.product.repository.ProductEntityMvc;
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

    public Mono<ProductEntityWebFlux> findById(Long id) {
        return productRepositoryWebFlux.findById(id);
    }

    // Update
    public Mono<ProductEntityWebFlux> update(
        Long id,
        Long adminId,
        String name,
        Long price,
        String category,
        String thumbnailUrl
    ) {

        return productRepositoryWebFlux.findById(id)
                .flatMap(product -> {
                    product.setAdminId(adminId);
                    product.setName(name);
                    product.setPrice(price);
                    product.setCategory(category);
                    product.setThumbnailUrl(thumbnailUrl);

                    return productRepositoryWebFlux.save(product);
                });
    }

    // Delete
    public Mono<Void> deleteById(Long id) {
        return productRepositoryWebFlux.deleteById(id);
    }

}
