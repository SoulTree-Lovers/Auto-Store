package org.example.api.webflux.domain.storeadmin.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class StoreAdminEntityWebFlux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
