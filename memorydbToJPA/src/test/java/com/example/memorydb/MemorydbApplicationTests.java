//package com.example.memorydb;
//
//import com.global.iop.util.StringUtils;
//import com.squareup.okhttp.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.aop.scope.ScopedProxyUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCrypt;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.OffsetDateTime;
//import java.time.ZoneOffset;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//import java.util.Base64;
//
//@SpringBootTest
//class MemorydbApplicationTests {
//
//	@Autowired
//	private OkHttpClient okHttpClient;
//
//	private String clientId = "7IhZZgD9MgJg9wvwvHTpIc";
//	private String clientSecret = "$2a$04$QD6jfkmbqnEc/kQrayyU1.";
//
//
//	@Test
//	void productRegister() throws IOException {
//		String accessToken = getToken();
//		OkHttpClient client = new OkHttpClient();
//
//		// 시작 판매일과 종료 판매일 계산
//		LocalDateTime now = LocalDateTime.now();
//		LocalDateTime startSaleDateTime = now.plusDays(2).withHour(0).withMinute(0).withSecond(0).withNano(0);
//		LocalDateTime endSaleDateTime = now.plusYears(1).withHour(23).withMinute(59).withSecond(59).withNano(999);
//
//		// ISO 8601 형식으로 변환하여 요청 본문에 사용
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
//		String isoStartSaleDate = startSaleDateTime.format(formatter);
//		String isoEndSaleDate = endSaleDateTime.format(formatter);
//
//		// JSON 형식의 요청 본문 생성
//		String requestBody = "{\n" +
//				"  \"originProduct\": {\n" +
//				"    \"statusType\": \"SALE\",\n" +
//				"    \"leafCategoryId\": \"50002936\",\n" +
//				"    \"name\": \"애플 펜슬 2 1 아이패드 펜슬 GOOJODOQ 블루투스 스타일러스 펜, 아이패드 펜 프로 11 12 9 에어 4 에어 5 2018-2023\",\n" +
//				"    \"detailContent\": \"애플 펜슬 2 1 아이패드 펜슬 GOOJODOQ 블루투스 스타일러스 펜, 아이패드 펜 프로 11 12 9 에어 4 에어 5 2018-2023\",\n" +
//				"    \"images\": {\n" +
//				"      \"representativeImage\": {\n" +
//				"        \"url\": \"https://ae01.alicdn.com/kf/Sb17bcdce1d1c4d129d052c029c03f144V.jpg\"\n" +
//				"      },\n" +
//				"      \"optionalImages\": [\n" +
//				"        {\"url\": \"https://ae01.alicdn.com/kf/Sccbe099ca4324e49adb19a6f28886151w.jpg\"},\n" +
//				"        {\"url\": \"https://ae01.alicdn.com/kf/Sdd8163581f364f3f8a49f7e071281e5dE.jpg\"},\n" +
//				"        {\"url\": \"https://ae01.alicdn.com/kf/S16e5949c520a4468af62a7cb76568addM.jpg\"},\n" +
//				"        {\"url\": \"https://ae01.alicdn.com/kf/S57187b87271b4333b232bd6b630df97b4.jpg\"},\n" +
//				"        {\"url\": \"https://ae01.alicdn.com/kf/S2e1d6ed21cb544819f7512699d0736c8k.jpg\"},\n" +
//				"        {\"url\": \"https://video.aliexpress-media.com/play/u/ae_sg_item/221208907/p/1/e/6/t/10301/1100081049858.mp4\"}\n" +
//				"      ]\n" +
//				"    },\n" +
//				"    \"saleStartDate\": \"" + isoStartSaleDate + "\",\n" +
//				"    \"saleEndDate\": \"" + isoEndSaleDate + "\",\n" +
//				"    \"salePrice\": 29000,\n" +
//				"    \"stockQuantity\": 1000,\n" +
//				"    \"deliveryInfo\": {\n" +
//				"      \"deliveryType\": \"DELIVERY\",\n" +
//				"      \"deliveryAttributeType\": \"NORMAL\",\n" +
//				"      \"deliveryCompany\": \"string\",\n" +
//				"      \"deliveryBundleGroupUsable\": false,\n" +
//				"      \"deliveryBundleGroupId\": 0,\n" +
//				"      \"deliveryFee\": {\n" +
//				"        \"deliveryFeeType\": \"PAID\",\n" +
//				"        \"baseFee\": 3000,\n" +
//				"        \"deliveryFeePayType\": \"PREPAID\",\n" +
//				"        \"deliveryFeeByArea\": {\n" +
//				"          \"deliveryAreaType\": \"AREA_2\",\n" +
//				"          \"area2extraFee\": 3000,\n" +
//				"          \"differentialFeeByArea\": \"제주 및 도서산간 추가 배송비 부담\"\n" +
//				"        },\n" +
//				"        \"claimDeliveryInfo\": {\n" +
//				"          \"returnDeliveryCompanyPriorityType\": \"PRIMARY\",\n" +
//				"          \"returnDeliveryFee\": 100000,\n" +
//				"          \"exchangeDeliveryFee\": 100000,\n" +
//				"          \"shippingAddressId\": 03048,\n" +
//				"          \"returnAddressId\": 03048,\n" +
//				"          \"freeReturnInsuranceYn\": false\n" +
//				"        },\n" +
//				"        \"businessCustomsClearanceSaleYn\": true\n" +
//				"      },\n" +
//				"      \"afterServiceInfo\": {\n" +
//				"        \"afterServiceTelephoneNumber\": \"02-202-0000\",\n" +
//				"        \"afterServiceGuideContent\": \"02-202-0000\"\n" +
//				"      },\n" +
//				"      \"originAreaInfo\": {\n" +
//				"        \"originAreaCode\": \"string\"\n" +
//				"      }\n" +
//				"    }\n" +
//				"  }\n" +
//				"}";
//
//		// 요청 생성
//		Request request = new Request.Builder()
//				.url("https://api.commerce.naver.com/external/v2/products")
//				.post(RequestBody.create(MediaType.parse("application/json"), requestBody))
//				.addHeader("Authorization", accessToken)
//				.addHeader("content-type", "application/json")
//				.build();
//
//		// 요청 실행 및 응답 처리
//		Response response = client.newCall(request).execute();
//		System.out.println(response.body().string()); // 응답 내용 출력
//	}
//
//
//
//
//	@Test
//	void findCategory() throws IOException {
//		OkHttpClient client = new OkHttpClient();
//		String accessToken = getToken();
//		System.out.println(accessToken);
//		Request request = new Request.Builder()
//				.url("https://api.commerce.naver.com/external/v1/categories?last=true")
//				.get()
//				.addHeader("Authorization", accessToken)
//				.build();
//
//		Response response = client.newCall(request).execute();
//		String responseBody = response.body().string(); // 응답 본문을 문자열로 변환
//		System.out.println(responseBody); // 카테고리 목록 출력
//	}
//
//
//	private String generateSignature(String clientId, String clientSecret, Long timestamp) {
//		String password = StringUtils.join(Arrays.asList(new String[]{clientId, timestamp.toString()}), "_");
//		String hashedPw = BCrypt.hashpw(password, clientSecret);
//		return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
//	}
//
//	private String extractAccessToken(String jsonResponse) {
//		// Assuming the JSON response is in the format: {"access_token":"your_access_token","expires_in":3600,"token_type":"Bearer"}
//		return jsonResponse.split("\"access_token\":\"")[1].split("\"")[0];
//	}
//
//	private String getToken() throws IOException {
//		Long timestamp = System.currentTimeMillis();
//		String signature = generateSignature(clientId, clientSecret, timestamp);
//
//		String url = "https://api.commerce.naver.com/external/v1/oauth2/token" +
//				"?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
//				"&timestamp=" + timestamp +
//				"&client_secret_sign=" + URLEncoder.encode(signature, StandardCharsets.UTF_8) +
//				"&grant_type=client_credentials" +
//				"&type=SELF" +
//				"&account_id=maskingmaksing";
//
//		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//		connection.setRequestMethod("POST");
//		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		connection.setDoOutput(true);
//
//		int responseCode = connection.getResponseCode();
//		if (responseCode == HttpURLConnection.HTTP_OK) {
//			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//				StringBuilder response = new StringBuilder();
//				String line;
//				while ((line = bufferedReader.readLine()) != null) {
//					response.append(line);
//				}
//				return extractAccessToken(response.toString());
//			}
//		} else {
//			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
//				StringBuilder response = new StringBuilder();
//				String line;
//				while ((line = bufferedReader.readLine()) != null) {
//					response.append(line);
//				}
//				throw new IOException("HTTP error code: " + responseCode + ", Response: " + response.toString());
//			}
//		}
//	}
//}
