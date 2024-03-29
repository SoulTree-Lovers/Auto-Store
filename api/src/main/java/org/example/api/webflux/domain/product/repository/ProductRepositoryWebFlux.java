package org.example.api.webflux.domain.product.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepositoryWebFlux extends ReactiveCrudRepository<ProductEntityWebFlux, Long> {


}
