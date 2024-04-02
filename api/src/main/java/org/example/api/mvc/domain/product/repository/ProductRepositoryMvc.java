package org.example.api.mvc.domain.product.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier(value = "product_mvc")
public interface ProductRepositoryMvc extends JpaRepository<ProductEntityMvc, Long> {

}
