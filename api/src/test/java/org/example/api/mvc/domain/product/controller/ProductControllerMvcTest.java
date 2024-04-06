package org.example.api.mvc.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.api.mvc.domain.product.controller.model.ProductRequestMvc;
import org.example.api.mvc.domain.product.controller.model.ProductResponseMvc;
import org.example.api.mvc.domain.product.repository.ProductEntityMvc;
import org.example.api.mvc.domain.product.repository.ProductRepositoryMvc;
import org.example.api.mvc.domain.product.service.ProductServiceMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductControllerMvcTest {
    /**
     * TODO: 상품 CRUD 테스트, 상품 여러 개 등록 시 성능 측정 로직 구현
     */
    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    ProductServiceMvc productServiceMvc;

    @Autowired
    ProductRepositoryMvc productRepositoryMvc;

    @BeforeEach
    void setUp() {
        // 각 테스트 실행 전에 데이터베이스를 초기화.
        productRepositoryMvc.deleteAll();
    }

    @Test
    @DisplayName("상품 1개 등록 - TestRestTemplate 기반")
    void createProduct() {
        // given
        Long adminId = 1L;
        String name = "보조배터리";
        Long price = 10000L;
        String category = "전자기기";
        String thumbnailUrl = "http://abc.com";

        ProductRequestMvc productRequestMvc = ProductRequestMvc.builder()
            .adminId(adminId)
            .name(name)
            .price(price)
            .category(category)
            .thumbnailUrl(thumbnailUrl)
            .build();

        String url = "http://localhost:" + port + "/mvc/product/create";

        // when
        ResponseEntity<ProductResponseMvc> responseEntity = testRestTemplate.postForEntity(url, productRequestMvc, ProductResponseMvc.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ProductEntityMvc savedProduct = productServiceMvc.findById(responseEntity.getBody().getId());
        assertThat(savedProduct.getAdminId()).isEqualTo(adminId);
        assertThat(savedProduct.getName()).isEqualTo(name);
        assertThat(savedProduct.getPrice()).isEqualTo(price);
        assertThat(savedProduct.getCategory()).isEqualTo(category);
        assertThat(savedProduct.getThumbnailUrl()).isEqualTo(thumbnailUrl);


    }

    @Test
    void findAllProduct() throws Exception {
        // given
        // 상품 저장을 위한 데이터
        Long adminId1 = 1L;
        String name1 = "상품1";
        Long price1 = 10000L;
        String category1 = "전자기기";
        String thumbnailUrl1 = "https://naver.com?213asdabfd";

        Long adminId2 = 2L;
        String name2 = "상품2";
        Long price2 = 15000L;
        String category2 = "생활용품";
        String thumbnailUrl2 = "https://naver.com?213asdaa";

        Long adminId3 = 3L;
        String name3 = "상품3";
        Long price3 = 20000L;
        String category3 = "주방용품";
        String thumbnailUrl3 = "https://naver.com?213asdadsagg";

        String url = "http://localhost:" + port + "/mvc/product/find-all";

        ProductEntityMvc entity1 = ProductEntityMvc.builder()
            .adminId(adminId1)
            .name(name1)
            .price(price1)
            .category(category1)
            .thumbnailUrl(thumbnailUrl1)
            .build();

        ProductEntityMvc entity2 = ProductEntityMvc.builder()
            .adminId(adminId2)
            .name(name2)
            .price(price2)
            .category(category2)
            .thumbnailUrl(thumbnailUrl2)
            .build();

        ProductEntityMvc entity3 = ProductEntityMvc.builder()
            .adminId(adminId3)
            .name(name3)
            .price(price3)
            .category(category3)
            .thumbnailUrl(thumbnailUrl3)
            .build();

        List<ProductEntityMvc> entities = List.of(entity1, entity2, entity3);

        // when
        // 상품 3개 저장
        productRepositoryMvc.save(entity1);
        productRepositoryMvc.save(entity2);
        productRepositoryMvc.save(entity3);

        ResponseEntity<List<ProductResponseMvc>> responseEntity = testRestTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ProductResponseMvc>>() {} // ProductResponseMvc가 담긴 리스트 형식 반환
        );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<ProductResponseMvc> products = (List<ProductResponseMvc>) responseEntity.getBody();

        for (int i = 0; i < entities.size(); i++) {
            assertThat(products.get(i).getAdminId()).isEqualTo(entities.get(i).getAdminId());
            assertThat(products.get(i).getName()).isEqualTo(entities.get(i).getName());
            assertThat(products.get(i).getPrice()).isEqualTo(entities.get(i).getPrice());
            assertThat(products.get(i).getCategory()).isEqualTo(entities.get(i).getCategory());
            assertThat(products.get(i).getThumbnailUrl()).isEqualTo(entities.get(i).getThumbnailUrl());
        }

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