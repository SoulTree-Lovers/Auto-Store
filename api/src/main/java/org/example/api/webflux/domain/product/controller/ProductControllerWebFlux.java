package org.example.api.webflux.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.webflux.domain.product.controller.model.ProductRequestWebFlux;
import org.example.api.webflux.domain.product.controller.model.ProductResponseWebFlux;
import org.example.api.webflux.domain.product.service.ProductServiceWebFlux;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/web-flux/product")
@RequiredArgsConstructor
public class ProductControllerWebFlux {

    /**
     * TODO: 상품 CRUD 요청 구현
     */

    private final ProductServiceWebFlux productServiceWebFlux;

    // Create
    @PostMapping("/create")
    public Mono<ProductResponseWebFlux> createProduct(
        @RequestBody ProductRequestWebFlux request
        ) {

        return productServiceWebFlux.create(
            request.getAdminId(),
            request.getName(),
            request.getPrice(),
            request.getCategory(),
            request.getThumbnailUrl()
        ).map(ProductResponseWebFlux::of);

    }

    // Read
    @GetMapping("/find-all")
    public Flux<ProductResponseWebFlux> findAllProduct() {
        return productServiceWebFlux.findAll()
            .map(ProductResponseWebFlux::of);
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<ProductResponseWebFlux>> findProductById(
        @PathVariable Long id
    ) {
        return productServiceWebFlux.findById(id)
            .map(it -> ResponseEntity.ok().body(ProductResponseWebFlux.of(it)))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    // Update
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<ProductResponseWebFlux>> updateProduct(
        @PathVariable Long id,
        @RequestBody ProductRequestWebFlux productRequestWebFlux
    ) {

        return productServiceWebFlux.update(
                id,
                productRequestWebFlux.getAdminId(),
                productRequestWebFlux.getName(),
                productRequestWebFlux.getPrice(),
                productRequestWebFlux.getCategory(),
                productRequestWebFlux.getThumbnailUrl()
            )
            .map(product -> ResponseEntity.ok(ProductResponseWebFlux.of(product)))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<?>> deleteProduct(
        @PathVariable Long id
    ) {
        return productServiceWebFlux.deleteById(id)
            .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
