package org.example.api.webflux.domain.product.controller;

import org.example.api.webflux.domain.product.controller.model.ProductRequestWebFlux;
import org.example.api.webflux.domain.product.controller.model.ProductResponseWebFlux;
import org.example.api.webflux.domain.product.repository.ProductEntityWebFlux;
import org.example.api.webflux.domain.product.service.ProductServiceWebFlux;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductControllerWebFlux.class)
@AutoConfigureWebTestClient

class ProductControllerWebFluxTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductServiceWebFlux productServiceWebFlux;

    @Test
    void createProduct() {
        // given
        Long adminId = 1L;
        String name = "보조배터리";
        Long price = 10000L;
        String category = "전자기기";
        String thumbnailUrl = "http://abc.com";

        when(productServiceWebFlux.create(adminId, name, price, category, thumbnailUrl)).thenReturn(
            Mono.just(new ProductEntityWebFlux(1L, adminId, name, price, category, thumbnailUrl, LocalDateTime.now(), LocalDateTime.now()))
        );

        webTestClient.post().uri("/web-flux/product/create")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new ProductRequestWebFlux(adminId, name, price, category, thumbnailUrl))
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(ProductResponseWebFlux.class)
            .value(res -> {
                assertEquals(adminId, res.getAdminId());
                assertEquals(name, res.getName());
                assertEquals(price, res.getPrice());
                assertEquals(category, res.getCategory());
                assertEquals(thumbnailUrl, res.getThumbnailUrl());
            });
    }

    @Test
    void findAllProduct() {
    }

    @Test
    void findProductById() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }
}