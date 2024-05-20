package com.example.memorydb;

import com.example.memorydb.ali.db.entity.ProductEntityFromAli;
import com.example.memorydb.ali.service.AliService;
import com.example.memorydb.naver.service.NaverService;
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
	private final NaverService naverService;

	@Autowired
	public MemorydbApplication(AliService aliService, NaverService naverService) {
		this.aliService = aliService;
        this.naverService = naverService;
    }

	public static void main(String[] args) {
		SpringApplication.run(MemorydbApplication.class, args);
	}

	@Scheduled(fixedRate = 3600000) // 60분마다 실행
	//@Scheduled(cron = "0 0 0 1 * ?") // 매달 1일 자정에 실행
	public void loadData() throws Exception {
		// 상품 DB 초기화
		aliService.deleteAllProducts();
		// 등록 상품 삭제
		naverService.deleteProduct();
		// 네이버 DB(등록 상품 ID DB) 초기화
		naverService.deleteAllProducts();
		// 각 카테고리에 대해 상품 데이터를 불러와서 저장
		List<ProductEntityFromAli> productList = aliService.loadAllProducts();
		// 상품 등록(호출 기준 10분 이내 생성된 데이터만 등록=>과거의 데이터 등록 X)
		naverService.registerProduct();

	}
}
