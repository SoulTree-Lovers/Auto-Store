package com.example.memorydb.ali.controller;

import com.example.memorydb.ali.db.entity.ProductEntityFromAli;
import com.example.memorydb.ali.db.repository.AliExpressRepository;
import com.example.memorydb.ali.service.AliService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ali")
@RequiredArgsConstructor
public class AliApiController {
    private final AliService aliService;
    private final AliExpressRepository aliExpressRepository;
    List<String> categoryIds = Arrays.asList(
            "200001085",
            "200293142",
            "200001726",
            "36",
            "200002005",
            "200003411",
            "200003427",
            "201159809",
            "1504"
    );
    @GetMapping("/loadProduct")
    public List<ProductEntityFromAli> loadProduct() {
        System.out.printf("LoadProduct()...");
        List<ProductEntityFromAli> productList = new ArrayList<>();
        try {
            // 각 categoryId에 대해 병렬로 상품 정보를 읽어오고 저장
            categoryIds.parallelStream().forEach(categoryId -> {
                try {
                    // 각 categoryId에 대한 상품 정보를 읽어온 후 리스트에 추가
                    List<ProductEntityFromAli> products = aliService.loadProduct(categoryId);
                    // 가져온 상품 정보를 DB에 저장
                    productList.addAll(products);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            // 에러 처리
            e.printStackTrace();
            return Collections.emptyList();
        }
        return productList;
    }





    @PostMapping("")
    public ProductEntityFromAli create(
            @RequestBody ProductEntityFromAli aliEntity
    ){
        return aliService.create(aliEntity);
    }

    @GetMapping("/all")
    public List<ProductEntityFromAli> findAll(

    ){
        return aliService.findAll();
    }

}
