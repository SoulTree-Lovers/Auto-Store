package org.example.api.webflux.domain.product.controller;

import org.example.api.mvc.domain.product.repository.ProductRepositoryMvc;
import org.example.api.mvc.domain.product.service.ProductServiceMvc;
import org.example.api.webflux.domain.product.controller.model.ProductRequestWebFlux;
import org.example.api.webflux.domain.product.controller.model.ProductResponseWebFlux;
import org.example.api.webflux.domain.product.repository.ProductEntityWebFlux;
import org.example.api.webflux.domain.product.repository.ProductRepositoryWebFlux;
import org.example.api.webflux.domain.product.service.ProductServiceWebFlux;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
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

//    @BeforeEach
//    void setUp() {
//        // 각 테스트 실행 전에 데이터베이스를 초기화.
//        productRepositoryWebFlux.deleteAll();
//    }

    @Test
    void createProduct() {
        // given
        Long adminId = 1L;
        String name = "보조배터리";
        Long price = 10000L;
        String category = "전자기기";
        String thumbnailUrl = "http://abc.com";

        // when
        when(productServiceWebFlux.create(adminId, name, price, category, thumbnailUrl)).thenReturn(
            Mono.just(new ProductEntityWebFlux(1L, adminId, name, price, category, thumbnailUrl, LocalDateTime.now(), LocalDateTime.now()))
        );

        // then
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
        // given
        // when
        when(productServiceWebFlux.findAll()).thenReturn(
            Flux.just(
                new ProductEntityWebFlux(1L, 1L, "상품1", 10000L, "전자기기", "https://naver.com?213asdabfd", LocalDateTime.now(), LocalDateTime.now()),
                new ProductEntityWebFlux(2L, 1L, "상품2", 15000L, "생활용품", "https://naver.com?213asdaa", LocalDateTime.now(), LocalDateTime.now()),
                new ProductEntityWebFlux(3L, 1L, "상품3", 20000L, "주방용품", "https://naver.com?213asdadsagg", LocalDateTime.now(), LocalDateTime.now())
            )
        );

        // then
        webTestClient.get().uri("/web-flux/product/find-all")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBodyList(ProductResponseWebFlux.class)
            .hasSize(3);

    }

    @Test
    void findProductById() {
        // given
        // when
        when(productServiceWebFlux.findById(1L)).thenReturn(
            Mono.just(new ProductEntityWebFlux(1L, 1L, "상품1", 10000L, "전자기기", "https://naver.com?213asdabfd", LocalDateTime.now(), LocalDateTime.now()))
        );

        // then
        webTestClient.get().uri("/web-flux/product/find/1")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(ProductResponseWebFlux.class)
            .value( res -> {
                assertEquals(1L, res.getAdminId());
                assertEquals("상품1", res.getName());
                assertEquals(10000L, res.getPrice());
                assertEquals("전자기기", res.getCategory());
                assertEquals("https://naver.com?213asdabfd", res.getThumbnailUrl());
            });
    }

    @Test
    void notFoundUser() {

        when(productServiceWebFlux.findById(1L)).thenReturn(
            Mono.empty()
        );

        webTestClient.get().uri("/web-flux/product/find/1")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }
}