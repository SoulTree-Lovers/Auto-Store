package org.example.api.mvc.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositoryMvc extends JpaRepository<ProductEntityMvc, Long> {
}
