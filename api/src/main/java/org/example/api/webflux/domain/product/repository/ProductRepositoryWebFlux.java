package org.example.api.webflux.domain.product.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Qualifier(value = "product")
@Repository
public interface ProductRepositoryWebFlux extends ReactiveCrudRepository<ProductEntityWebFlux, Long> {


}
