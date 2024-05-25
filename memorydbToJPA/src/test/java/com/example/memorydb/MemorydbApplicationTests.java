package com.example.memorydb;

import com.global.iop.api.IopClient;
import com.global.iop.api.IopClientImpl;
import com.global.iop.api.IopRequest;
import com.global.iop.api.IopResponse;
import com.global.iop.domain.Protocol;
import com.global.iop.util.ApiException;
import com.global.iop.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minidev.json.parser.ParseException;
import okhttp3.*;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SpringBootTest
class MemorydbApplicationTests {

	@Autowired
	private OkHttpClient okHttpClient;

	private String clientId = "7IhZZgD9MgJg9wvwvHTpIc";
	private String clientSecret = "$2a$04$QD6jfkmbqnEc/kQrayyU1.";

	@Test
	void registerProduct() throws IOException, JSONException, ParseException {
		OkHttpClient client = new OkHttpClient();
		String accessToken = getToken();
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(1).withMinute(0).withSecond(0).withNano(0);
		ZonedDateTime endDateTime = now.plusYears(1).withMinute(59).withSecond(0).withNano(0);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
		String startDate = now.format(formatter);
		String endDate = endDateTime.format(formatter);

		MediaType mediaType = MediaType.parse("application/json");

		// JSON 객체 생성
		JsonObject originProduct = new JsonObject();
		originProduct.addProperty("statusType", "SALE");
		originProduct.addProperty("saleType", "NEW");
		originProduct.addProperty("leafCategoryId", "50002936");
		originProduct.addProperty("name", "아이패드 프로 12.9 매트 필름");
		//originProduct.addProperty("detailContent", "아이패드 프로 12.9 매트 필름");
		originProduct.addProperty("detailContent", crawling("https://ko.aliexpress.com/item/1005006109804924.html?pdp_npi=4%40dis%21KRW%21217948%2188764%21%21%21154.54%2162.94%21%4021013dad17166492828783277d13f1%2112000035790969897%21affd%21%21%21"));


		JsonObject representativeImage = new JsonObject();
		String NaverURL = register("https://ae01.alicdn.com/kf/S2287e804968345e598a1841b7925e88ck.jpg");
		representativeImage.addProperty("url", NaverURL);
		JsonObject images = new JsonObject();
		images.add("representativeImage", representativeImage);

		// Optional images array
		String url;
		JsonArray optionalImages = new JsonArray();
//		url = register("https://ae01.alicdn.com/kf/S5bf2128a738f480094ee562e2fbe32b7s.jpg");
//		optionalImages.add(createImageObject(url));

		url = register("https://ae01.alicdn.com/kf/S48a1ac7ac0d94f65a701fb6aeba01cc13.jpg");
		optionalImages.add(createImageObject(url));

		url = register("https://ae01.alicdn.com/kf/Sf4dcc926da0d4ee684cfb93525ad317ct.jpg");
		optionalImages.add(createImageObject(url));

		url = register("https://ae01.alicdn.com/kf/Sbb9251ecf7e5466986b25ca0bdc6a694J.jpg");
		optionalImages.add(createImageObject(url));

//		url = register("https://ae01.alicdn.com/kf/Sfe9beac5b4194f6cae37151ae1deb4f3f.jpg");
//		optionalImages.add(createImageObject(url));

//		url = register("https://ae01.alicdn.com/kf/Sf26f2b756b94499ca48125d7c073b0c4O.jpg");
//		optionalImages.add(createImageObject(url));
		images.add("optionalImages", optionalImages);


		originProduct.add("images", images);
		originProduct.addProperty("saleStartDate", startDate);
		originProduct.addProperty("saleEndDate", endDate);
		originProduct.addProperty("salePrice", 999999990);
		originProduct.addProperty("stockQuantity", 50);

		JsonObject deliveryInfo = new JsonObject();
		deliveryInfo.addProperty("deliveryType", "DELIVERY");
		deliveryInfo.addProperty("deliveryAttributeType", "NORMAL");
		deliveryInfo.addProperty("deliveryCompany", "CJGLS");
		deliveryInfo.addProperty("deliveryBundleGroupUsable", false);

		JsonObject deliveryFee = new JsonObject();
		deliveryFee.addProperty("deliveryFeeType", "PAID");
		deliveryFee.addProperty("baseFee", 100000);
		deliveryFee.addProperty("deliveryFeePayType", "PREPAID");

		JsonObject deliveryFeeByArea = new JsonObject();
		deliveryFeeByArea.addProperty("deliveryAreaType", "AREA_2");
		deliveryFeeByArea.addProperty("area2extraFee", 200000);
		deliveryFee.add("deliveryFeeByArea", deliveryFeeByArea);

		deliveryFee.addProperty("differentialFeeByArea", "제주 및 도서산간 지역 추가 배송비 발생합니다");
		deliveryInfo.add("deliveryFee", deliveryFee);

		JsonObject claimDeliveryInfo = new JsonObject();
		claimDeliveryInfo.addProperty("returnDeliveryCompanyPriorityType", "PRIMARY");
		claimDeliveryInfo.addProperty("returnDeliveryFee", 1000000);
		claimDeliveryInfo.addProperty("exchangeDeliveryFee", 1000000);
		deliveryInfo.add("claimDeliveryInfo", claimDeliveryInfo);

		deliveryInfo.addProperty("businessCustomsClearanceSaleYn", true);
		originProduct.add("deliveryInfo", deliveryInfo);

		JsonObject detailAttribute = new JsonObject();
		JsonObject afterServiceInfo = new JsonObject();
		afterServiceInfo.addProperty("afterServiceTelephoneNumber", "010-1234-9876");
		afterServiceInfo.addProperty("afterServiceGuideContent", "AS 불가합니다.");
		detailAttribute.add("afterServiceInfo", afterServiceInfo);

		JsonObject originAreaInfo = new JsonObject();
		originAreaInfo.addProperty("originAreaCode", "0200037");
		originAreaInfo.addProperty("importer", "Keib");
		detailAttribute.add("originAreaInfo", originAreaInfo);

		JsonObject optionInfo = new JsonObject();
		optionInfo.addProperty("simpleOptionSortType", "CREATE");

		JsonArray optionSimple = new JsonArray();
		JsonObject simpleOption = new JsonObject();
		simpleOption.addProperty("groupName", "group1");
		simpleOption.addProperty("name", "option1");
		simpleOption.addProperty("usable", true);
		optionSimple.add(simpleOption);
		optionInfo.add("optionSimple", optionSimple);


		detailAttribute.add("optionInfo", optionInfo);
		detailAttribute.addProperty("minorPurchasable", true);  // minorPurchasable 필드 추가
//		originProduct.add("detailAttribute", detailAttribute);

		// productInfoProvidedNotice 설정
		JsonObject productInfoProvidedNotice = new JsonObject();
		productInfoProvidedNotice.addProperty("productInfoProvidedNoticeType", "ETC");

// ETC 타입에 필요한 세부 정보 추가
		JsonObject etcInfo = new JsonObject();
		etcInfo.addProperty("itemName", "아이패드 프로 12.9 매트 필름");
		etcInfo.addProperty("modelName", "아이패드 프로 12.9 매트 필름");
		etcInfo.addProperty("manufacturer", "Made In China");
		etcInfo.addProperty("customerServicePhoneNumber", "02-000-0000");

		productInfoProvidedNotice.add("etc", etcInfo);

// detailAttribute에 추가
		detailAttribute.add("productInfoProvidedNotice", productInfoProvidedNotice);
		originProduct.add("detailAttribute", detailAttribute);

		JsonObject root = new JsonObject();
		root.add("originProduct", originProduct);

		JsonObject smartstoreChannelProduct = new JsonObject();
		smartstoreChannelProduct.addProperty("naverShoppingRegistration", false);
		smartstoreChannelProduct.addProperty("channelProductDisplayStatusType", "ON");
		root.add("smartstoreChannelProduct", smartstoreChannelProduct);  // smartstoreChannelProduct 필드 추가

//		JsonObject windowChannelProduct = new JsonObject();
//		windowChannelProduct.addProperty("naverShoppingRegistration", false);
//		windowChannelProduct.addProperty("channelNo", 0);
//		windowChannelProduct.addProperty("best", false);
//		root.add("windowChannelProduct", windowChannelProduct);

		Gson gson = new Gson();
		String json = gson.toJson(root);

		RequestBody body = RequestBody.create(mediaType, json);

		Request request = new Request.Builder()
				.url("https://api.commerce.naver.com/external/v2/products")
				.post(body)
				.addHeader("Authorization", accessToken)
				.addHeader("content-type", "application/json")
				.build();

		try {
			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				System.out.println("Product registered successfully");

				// 응답 본문 파싱
				String responseBody = response.body().string();
				System.out.println(responseBody);
				JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
				int smartstoreChannelProductNo = jsonResponse.get("smartstoreChannelProductNo").getAsInt();

				System.out.println("Smartstore Channel Product Number: "+smartstoreChannelProductNo);
			} else {
				System.out.println("Failed to register product. Response code: " + response.code());
				System.out.println("Response body: " + response.body().string());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String downloadImage(String imageUrl) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(imageUrl).build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) {
			throw new IOException("이미지 다운로드 실패: " + response);
		}

		// 이미지 데이터를 파일로 저장
		File tempFile = File.createTempFile("upload", ".jpg");
		Files.write(Paths.get(tempFile.getAbsolutePath()), response.body().bytes());
		return tempFile.getAbsolutePath();
	}


	private String register(String imageUrl) throws IOException, JSONException, ParseException {
//		String imageUrl = "https://ae01.alicdn.com/kf/Sde5640813b6b4750ae6e144770264d9aU.jpg";
		String API_URL = "https://api.commerce.naver.com/external/v1/product-images/upload";
		String ACCESS_TOKEN = getToken();
		String imageFilePath = downloadImage(imageUrl);

		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.retryOnConnectionFailure(true)
				.build();

		File imageFile = new File(imageFilePath);
		RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);

		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("imageFiles", imageFile.getName(), fileBody)
				.build();

		Request request = new Request.Builder()
				.url(API_URL)
				.post(requestBody)
				.addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
				.addHeader("Content-Type", "multipart/form-data")
				.build();

		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) {
			throw new IOException("이미지 업로드 실패: " + response);
		}
		String rst = response.body().string();

		System.out.println("이미지 업로드 성공: " + rst);
		response.close();  // response body를 사용 후 즉시 닫기

		JsonObject jsonResponse = JsonParser.parseString(rst).getAsJsonObject();
		JsonArray imagesArray = jsonResponse.getAsJsonArray("images");
		String uploadedImageUrl = imagesArray.get(0).getAsJsonObject().get("url").getAsString();

		System.out.println("추출된 이미지 URL: " + uploadedImageUrl);
		return uploadedImageUrl;
	}

	private JsonObject createImageObject(String url) {
		JsonObject image = new JsonObject();
		image.addProperty("url", url);
		return image;
	}

	@Test
	void deleteProduct() throws IOException {
		OkHttpClient client = new OkHttpClient();
		String accessToken = getToken();
		String channelProductNo = "10349697956";
		String url = "https://api.commerce.naver.com/external/v2/products/channel-products/" + channelProductNo;

		Request request = new Request.Builder()
				.url(url)
				.delete(RequestBody.create(null, new byte[0]))
				.addHeader("Authorization", "Bearer " + accessToken)
				.build();

		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			System.out.println("Product deleted successfully");
		} else {
			System.out.println("Failed to delete product. Response code: " + response.code());
			System.out.println("Response body: " + response.body().string());
		}
	}



	@Test
	void mathTest(){
		String s = "9885";
		double targetSalePrice = Double.parseDouble(s.replace(",", "").replace("₩", ""));
		double salePrice = targetSalePrice * 1.5;
		System.out.println(salePrice);
	}


	@Test
	void originArea() throws IOException {
		OkHttpClient client = new OkHttpClient();
		String accessToken = getToken();
		Request request = new Request.Builder()
				.url("https://api.commerce.naver.com/external/v1/product-origin-areas")
				.get()
				.addHeader("Authorization", accessToken)
				.build();

		Response response = client.newCall(request).execute();
		String responseBody = response.body().string();
		System.out.println(responseBody);
	}


	@Test
	void crawl() {
		String initialUrl = "https://ko.aliexpress.com/item/1005006109804924.html?pdp_npi=4%40dis%21KRW%21217948%2188764%21%21%21154.54%2162.94%21%4021013dad17166492828783277d13f1%2112000035790969897%21affd%21%21%21";
		String descriptionHtml = crawling(initialUrl);
		if (descriptionHtml != null) {
			System.out.println("HTML Content:\n" + descriptionHtml);
		}
	}

	private String crawling(String url) {
		try {
			// Document 객체를 통해 URL로부터 HTML을 파싱합니다.
			Document doc = Jsoup.connect(url).get();

			// 스크립트 요소에서 텍스트 추출
			Elements scriptElements = doc.select("script");
			String scriptContent = "";
			for (Element script : scriptElements) {
				if (script.html().contains("descriptionUrl")) {
					scriptContent = script.html();
					break;
				}
			}

			// 정규식을 사용하여 descriptionUrl 추출
			Pattern pattern = Pattern.compile("descriptionUrl\":\"(.*?)\"");
			Matcher matcher = pattern.matcher(scriptContent);
			if (matcher.find()) {
				String descriptionUrl = matcher.group(1);
				System.out.println("Description URL: " + descriptionUrl);

				// 추출된 descriptionUrl을 사용하여 HTML을 파싱하고 반환합니다.
				return getHtmlContent(descriptionUrl);
			} else {
				System.out.println("Description URL not found.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getHtmlContent(String url) throws IOException {
		try {
			// Document 객체를 통해 URL로부터 HTML을 파싱합니다.
			Document doc = Jsoup.connect(url).get();

			// HTML을 문자열로 반환합니다.
			return doc.html();
		} catch (org.jsoup.HttpStatusException e) {
			System.out.println("HTTP error fetching URL. Status=" + e.getStatusCode() + ", URL=" + e.getUrl());
			return null;
		}
	}

	@Test
	void findCategory() throws IOException {
		OkHttpClient client = new OkHttpClient();
		String accessToken = getToken();
		System.out.println(accessToken);
		Request request = new Request.Builder()
				.url("https://api.commerce.naver.com/external/v1/categories?last=true")
				.get()
				.addHeader("Authorization", accessToken)
				.build();

		Response response = client.newCall(request).execute();
		String responseBody = response.body().string(); // 응답 본문을 문자열로 변환
		System.out.println(responseBody); // 카테고리 목록 출력
	}

	String url = "https://api-sg.aliexpress.com/";
	String SIGN_METHOD = "sha256";
	String appkey = "505450";
	String appSecret = "wvnyRtRuvXPKThi4bsX5SzzXwxK2sAOs";
	String CODE = "3_505450_Z5v32pqtE4jTtfDBxeZq0nAn6351";
	String TOKEN = "50000200c10umQ9mwoQecd18ee0528IzClSfuvHADAw8L0uDnfPkXFtWCHwvNBNLGAZs";
	String appSignature="keib";
	String trackingId="keib";
	private IopClient iopClient;
	@Test
	void AliCategory() throws IOException, ApiException {
		IopClient client = new IopClientImpl(url, appkey, appSecret);
		IopRequest request = new IopRequest();
		request.setApiName("aliexpress.affiliate.category.get");
		request.addApiParameter("app_signature", TOKEN);
		IopResponse response = client.execute(request, Protocol.GOP);
		System.out.println(response.getGopResponseBody());
	} // Ali 카테고리


	private String generateSignature(String clientId, String clientSecret, Long timestamp) {
		String password = StringUtils.join(Arrays.asList(new String[]{clientId, timestamp.toString()}), "_");
		String hashedPw = BCrypt.hashpw(password, clientSecret);
		return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
	}

	private String extractAccessToken(String jsonResponse) {
		// Assuming the JSON response is in the format: {"access_token":"your_access_token","expires_in":3600,"token_type":"Bearer"}
		return jsonResponse.split("\"access_token\":\"")[1].split("\"")[0];
	}

	private String getToken() throws IOException {
		Long timestamp = System.currentTimeMillis();
		String signature = generateSignature(clientId, clientSecret, timestamp);

		String url = "https://api.commerce.naver.com/external/v1/oauth2/token" +
				"?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
				"&timestamp=" + timestamp +
				"&client_secret_sign=" + URLEncoder.encode(signature, StandardCharsets.UTF_8) +
				"&grant_type=client_credentials" +
				"&type=SELF" +
				"&account_id=maskingmaksing";

		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setDoOutput(true);

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					response.append(line);
				}
				return extractAccessToken(response.toString());
			}
		} else {
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					response.append(line);
				}
				throw new IOException("HTTP error code: " + responseCode + ", Response: " + response.toString());
			}
		}
	}
}
