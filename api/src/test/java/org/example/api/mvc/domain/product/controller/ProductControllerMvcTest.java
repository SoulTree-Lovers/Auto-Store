package org.example.api.mvc.domain.product.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerMvcTest {
    /**
     * TODO: 상품 CRUD 테스트, 상품 여러 개 등록 시 성능 측정 로직 구현
     */


    @Autowired
    MockMvc mvc;

    private static final String BASE_URL = "/mvc/product/";


    @Test
    @DisplayName("상품 등록")
    void createProduct() {
        // given


        // when


        // then


    }

    @Test
    void findAllProduct() {
        // given


        // when


        // then


    }

    @Test
    void findProductById() {
        // given


        // when


        // then


    }

    @Test
    void deleteProduct() {
        // given


        // when


        // then


    }
}