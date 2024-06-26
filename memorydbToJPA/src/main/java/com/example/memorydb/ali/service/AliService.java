package com.example.memorydb.ali.service;

import com.example.memorydb.ali.db.entity.ProductEntityFromAli;
import com.example.memorydb.ali.db.repository.AliExpressRepository;
import com.global.iop.api.IopClient;
import com.global.iop.api.IopClientImpl;
import com.global.iop.api.IopRequest;
import com.global.iop.api.IopResponse;
import com.global.iop.domain.Protocol;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AliService {

    /**
     * TODO: 알리 익스프레스에서 상품 정보를 가져와서 DB에 저장하기.
     */
    // 이미 등록된 이미지 URL을 추적하기 위한 Set
    private final Set<String> registeredImageUrls = new HashSet<>();
    private final AliExpressRepository aliExpressRepository;
    String url = "https://api-sg.aliexpress.com/";
    String SIGN_METHOD = "sha256";
    String appkey = "APPKEY";
    String appSecret = "APPSECRET";
    String CODE = "CODE";
    String TOKEN = "TOKEN";
    String appSignature="keib";
    String trackingId="keib";
    private IopClient iopClient;

    public AliService(AliExpressRepository aliExpressRepository){

        this.aliExpressRepository = aliExpressRepository;
    }


    // create , update
    public ProductEntityFromAli create(ProductEntityFromAli ali){
        return aliExpressRepository.save(ali);
    }

    // all
    public List<ProductEntityFromAli> findAll(){
        return aliExpressRepository.findAll();
    }

    // 마지막으로 읽어온 ID를 저장할 변수
    private static long lastReadId = 1; // 카테고리마다 마지막으로 읽은 ID를 추적하기 위한 변수

    // 모든 상품 로드
    public List<ProductEntityFromAli> loadAllProducts() throws Exception {
        List<String> categoryIds = Arrays.asList(
                "200001085",
                //"200001726", -> 아동용품이라 어린이 제품 인증 필요
                "36",
                //"200002005", -> LED라 KC 인증 필요
                "200003411",
                "200003427",
                "201296801",
                "200000298"
                //"1504"-> LED라 KC 인증 필요
        );
        List<ProductEntityFromAli> allProducts = new ArrayList<>();
        for (String categoryId : categoryIds) {
            try {
                List<ProductEntityFromAli> products = loadProduct(categoryId);
                allProducts.addAll(products);
            } catch (Exception e) {
                // 오류 처리 로직 추가
                // 예를 들어 로깅 또는 예외 던지기 등
                System.err.println("카테고리 " + categoryId + "의 상품을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
        //saveAllProducts(allProducts);
        return allProducts; // 처리된 모든 상품 목록 반환
    }

    // 카테고리별 상품 로드
    public List<ProductEntityFromAli> loadProduct(String categoryId) throws Exception {
        String NaverCategoryId = null;
        switch (categoryId) {
            case "200001085" -> {NaverCategoryId="50002936";
            }
//            case "200001726" -> {NaverCategoryId="50004643";
//            }
            case "36" -> {NaverCategoryId="50006910";
            }
//            case "200002005" -> {NaverCategoryId="50003915";
//            }
            case "200003411" -> {NaverCategoryId="50004086";
            }
            case "200003427" -> {NaverCategoryId="50004057";
            }
//            case "1504" -> {NaverCategoryId="50007247";
//            }
            case "201296801" -> {NaverCategoryId="50015340";
            }
            case "200000298" -> {NaverCategoryId="50003989";
            }
        }

        IopClient client = new IopClientImpl(url, appkey, appSecret);
        IopRequest request = new IopRequest();
        request.setApiName("aliexpress.affiliate.product.query");
        request.addApiParameter("app_signature", appSignature);
        request.addApiParameter("category_ids", categoryId);
        request.addApiParameter("page_no", "1");
        request.addApiParameter("page_size", "10");
        request.addApiParameter("sort", "LAST_VOLUME_DESC");
        request.addApiParameter("target_currency", "KRW");
        request.addApiParameter("target_language", "KO");

        IopResponse response = client.execute(request, Protocol.TOP);
        String responseBody = response.getGopResponseBody();
        System.out.println(responseBody);

        JSONParser jsonParser = new JSONParser();
        JSONObject obj = (JSONObject) jsonParser.parse(responseBody);
        JSONObject resp_result = (JSONObject) obj.get("resp_result");

        // resp_result가 null이거나 result가 null인 경우 처리
        if (resp_result == null || resp_result.get("result") == null) {
            // 오류 처리 로직 추가
            // 예를 들어 로깅 또는 예외 던지기 등
            throw new RuntimeException("API 응답에 유효한 결과가 없습니다.");
        }

        JSONObject result = (JSONObject) resp_result.get("result");
        JSONArray products = (JSONArray) result.get("products");
        List<ProductEntityFromAli> savedProducts = new ArrayList<>();

        // 각 상품에 고유한 ID 할당
        for (int i = 0; i < products.size(); i++) {
            JSONObject tmp = (JSONObject) products.get(i);
            String appSalePrice = (String) tmp.get("app_sale_price");
            String originalPrice = (String) tmp.get("original_price");
            String targetSalePrice = (String) tmp.get("target_sale_price");
            String evaluateRate = (String) tmp.get("evaluate_rate");
            double rate = Double.parseDouble(evaluateRate.replace("%", ""));
            if (rate<90) continue;  //평점이 90% 이하라면 저장 안함
            String productMainImageUrl = (String) tmp.get("product_main_image_url");
            registeredImageUrls.add(productMainImageUrl);
            String productDetailUrl = (String) tmp.get("product_detail_url");
            JSONArray product_small_image_urls = (JSONArray) tmp.get("product_small_image_urls");
            String url0 = "", url1 = "", url2 = "", url3 = "", url4 = "", url5 = "";
            for (int j = 0; j < product_small_image_urls.size(); j++) {
                String imageUrl = (String) product_small_image_urls.get(j);
                // 이미지 URL이 이미 등록되었는지 확인
                if (registeredImageUrls.contains(imageUrl)) {
                    System.out.println("이미 등록된 이미지: " + imageUrl);
                    continue; // 이미 등록된 이미지라면 스킵
                }
                // 이미지 URL을 등록된 이미지로 추가
                registeredImageUrls.add(imageUrl);
                if (j == 0) url0 = imageUrl;
                else if (j == 1) url1 = imageUrl;
                else if (j == 2) url2 = imageUrl;
                else if (j == 3) url3 = imageUrl;
                else if (j == 4) url4 = imageUrl;
                else if (j == 5) url5 = imageUrl;
            }
            // 상품 정보 저장
            String secondLevelCategoryName = (String) tmp.get("second_level_category_name");
            Integer lastestVolume = (Integer) tmp.get("lastest_volume");
            String discount = (String) tmp.get("discount");
            String shopUrl = (String) tmp.get("shop_url");
            String productVideoUrl = (String) tmp.get("product_video_url");
            String firstLevelCategoryName = (String) tmp.get("first_level_category_name");
            String promotionLink = (String) tmp.get("promotion_link");
            String productTitle = (String) tmp.get("product_title");

            ProductEntityFromAli productEntityObj = new ProductEntityFromAli(lastReadId++, productTitle, appSalePrice, originalPrice, targetSalePrice, productDetailUrl, url0, url1, url2, url3, url4, url5, productMainImageUrl, productVideoUrl, evaluateRate, lastestVolume, discount, shopUrl, NaverCategoryId, firstLevelCategoryName, secondLevelCategoryName, promotionLink);
            aliExpressRepository.save(productEntityObj);
            savedProducts.add(productEntityObj);
            registeredImageUrls.clear();
        }
        return savedProducts;
    }

    // 오늘 생성된 상품들을 가져오는 메소드 추가
    public List<ProductEntityFromAli> getRecentProducts() {   LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesAgo = now.minusMinutes(10);
        return aliExpressRepository.findByCreatedAtBetween(tenMinutesAgo, now);
    }

    public void deleteAllProducts() {
        aliExpressRepository.deleteAll();
    }

    public void saveAllProducts(List<ProductEntityFromAli> products) {
        aliExpressRepository.saveAll(products);
    }

    public long countProducts() {
        return aliExpressRepository.count();
    }

    @Transactional
    public void deleteOldestDateProducts() {
        List<LocalDateTime> oldestDates = aliExpressRepository.findOldestDates();
        if (!oldestDates.isEmpty()) {
            LocalDateTime oldestDate = oldestDates.get(0);
            List<ProductEntityFromAli> oldestProducts = aliExpressRepository.findByCreatedAt(oldestDate);
            aliExpressRepository.deleteAll(oldestProducts);
        }
    }
}
