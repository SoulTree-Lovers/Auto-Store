package org.example.api.mvc.domain.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.api.mvc.domain.product.controller.model.ProductRequestMvc;
import org.example.api.mvc.domain.product.controller.model.ProductResponseMvc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerMvcTest {
    /**
     * TODO: 상품 CRUD 테스트, 상품 여러 개 등록 시 성능 측정 로직 구현
     */
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    private static final String BASE_URL = "/mvc/product/";


    @Test
    @DisplayName("상품 등록")
    void createProduct() throws Exception {
        // given
        Long adminId = 1L;
        String name = "보조배터리";
        Long price = 10000L;
        String category = "전자기기";
        String thumbnailUrl = "http://abc.com";


        // when
        String body = objectMapper.writeValueAsString(ProductRequestMvc.builder()
            .adminId(adminId)
            .name(name)
            .price(price)
            .category(category)
            .thumbnailUrl(thumbnailUrl)
            .build());


        // then
        /*ProductResponseMvc productResponseMvc = ProductResponseMvc.builder()
            .id(1L)
            .adminId(adminId)
            .name(name)
            .price(price)
            .category(category)
            .thumbnailUrl(thumbnailUrl)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();*/

        mvc.perform(post(BASE_URL + "/create")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
//            .andExpect(content().string(objectMapper.writeValueAsString(productResponseMvc)));

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