package org.example.api.mvc.domain.product.repository;


import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class ProductEntityMvc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
