package com.example.memorydb;

import com.example.memorydb.ali.db.entity.ProductEntityFromAli;
import com.example.memorydb.ali.service.AliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class MemorydbApplication {

	private final AliService aliService;

	@Autowired
	public MemorydbApplication(AliService aliService) {
		this.aliService = aliService;
	}

	public static void main(String[] args) {
		SpringApplication.run(MemorydbApplication.class, args);
	}

	@Scheduled(fixedRate = 3600000) // 60분마다 실행
	public void loadData() throws Exception {
		// 데이터베이스 초기화
		//aliService.deleteAllProducts();

		// 각 카테고리에 대해 상품 데이터를 불러와서 저장
		List<ProductEntityFromAli> productList = aliService.loadAllProducts();
//		aliService.saveAllProducts(productList);
	}
}
