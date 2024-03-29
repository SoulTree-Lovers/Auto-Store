package org.example.api.webflux.domain.product.repository;


import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class ProductEntityWebFlux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
