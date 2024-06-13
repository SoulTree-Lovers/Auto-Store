package com.example.memorydb.naver.service;

import com.example.memorydb.ali.db.entity.ProductEntityFromAli;
import com.example.memorydb.ali.db.repository.AliExpressRepository;
import com.example.memorydb.ali.service.AliService;
import com.example.memorydb.naver.db.entity.NaverEntity;
import com.example.memorydb.naver.db.repository.NaverRepository;
import com.global.iop.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minidev.json.parser.ParseException;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NaverService {
    @Autowired
    private OkHttpClient okHttpClient;
    @Autowired
    private NaverRepository naverRepository;
    private final AliService aliService;

    public NaverEntity saveOriginProductNo(String originProductNo) {
        NaverEntity naverEntity = NaverEntity.builder()
                .originProductNo(originProductNo)
                .build();
        return naverRepository.save(naverEntity);
    }

    private String clientId = "CLIENTID";
    private String clientSecret = "CLIENTSECRET";

    @Autowired
    public NaverService(AliService aliService) {
        this.aliService = aliService;
    }

    public void registerProduct() throws IOException, ParseException {
        OkHttpClient client = new OkHttpClient();
        String accessToken = getToken();

        List<ProductEntityFromAli> todayProducts = aliService.getRecentProducts(); // 최근 생성된 상품들(10분 이내) 가져오기
        System.out.println(todayProducts);
        for (ProductEntityFromAli product : todayProducts) {
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
            originProduct.addProperty("leafCategoryId", product.getCategoryID());
            // productTitle을 50자 이내로 자르기
            String productTitle = product.getProductTitle();
            if (productTitle.length() > 50) {
                productTitle = productTitle.substring(productTitle.length() - 49);
            }
            System.out.println("상품명: "+productTitle);
            originProduct.addProperty("name", productTitle);
            String DetailURL = product.getProductDetailUrl();
            DetailURL = crawling(DetailURL);
            if (DetailURL!=null)
                originProduct.addProperty("detailContent", DetailURL);
            else
                originProduct.addProperty("detailContent", productTitle);
            JsonObject representativeImage = new JsonObject();
            String NaverURL = registerWithDelay(product.getProductMainImageUrl());
            if (NaverURL == null) continue; // Main 이미지 등록 실패 시 스킵
            representativeImage.addProperty("url", NaverURL);
            JsonObject images = new JsonObject();
            images.add("representativeImage", representativeImage);

            // Optional images array
            JsonArray optionalImages = new JsonArray();
            addOptionalImageWithDelay(optionalImages, product.getUrl0());
            addOptionalImageWithDelay(optionalImages, product.getUrl1());
            addOptionalImageWithDelay(optionalImages, product.getUrl2());
            addOptionalImageWithDelay(optionalImages, product.getUrl3());
            addOptionalImageWithDelay(optionalImages, product.getUrl4());
            addOptionalImageWithDelay(optionalImages, product.getUrl5());
            images.add("optionalImages", optionalImages);

            originProduct.add("images", images);
            originProduct.addProperty("saleStartDate", startDate);
            originProduct.addProperty("saleEndDate", endDate);
            // 알리 판매가의 1.5배로 판매
            double salePrice = Math.ceil(Double.parseDouble(product.getTargetSalePrice()) * 1.5);
            long salePriceRounded = ((long) salePrice + 99) / 100 * 100;
            originProduct.addProperty("salePrice", salePriceRounded);
            originProduct.addProperty("stockQuantity", 50);

            JsonObject deliveryInfo = new JsonObject();
            deliveryInfo.addProperty("deliveryType", "DELIVERY");
            deliveryInfo.addProperty("deliveryAttributeType", "NORMAL");
            deliveryInfo.addProperty("deliveryCompany", "CJGLS");
            deliveryInfo.addProperty("deliveryBundleGroupUsable", false);

            JsonObject deliveryFee = new JsonObject();
            deliveryFee.addProperty("deliveryFeeType", "PAID");
            deliveryFee.addProperty("baseFee", 3000);
            deliveryFee.addProperty("deliveryFeePayType", "PREPAID");

            JsonObject deliveryFeeByArea = new JsonObject();
            deliveryFeeByArea.addProperty("deliveryAreaType", "AREA_2");
            deliveryFeeByArea.addProperty("area2extraFee", 10000);
            deliveryFee.add("deliveryFeeByArea", deliveryFeeByArea);

            deliveryFee.addProperty("differentialFeeByArea", "제주 및 도서산간 지역 추가 배송비 발생합니다");
            deliveryInfo.add("deliveryFee", deliveryFee);

            JsonObject claimDeliveryInfo = new JsonObject();
            claimDeliveryInfo.addProperty("returnDeliveryCompanyPriorityType", "PRIMARY");
            claimDeliveryInfo.addProperty("returnDeliveryFee", 50000);
            claimDeliveryInfo.addProperty("exchangeDeliveryFee", 50000);
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
            originAreaInfo.addProperty("importer", "AutoStore");
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
            etcInfo.addProperty("itemName", productTitle);
            etcInfo.addProperty("modelName", productTitle);
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

            Gson gson = new Gson();
            String json = gson.toJson(root);

            RequestBody body = RequestBody.create(mediaType, json);

            Request request = new Request.Builder()
                    .url("https://api.commerce.naver.com/external/v2/products")
                    .post(body)
                    .addHeader("Authorization", accessToken)
                    .addHeader("content-type", "application/json")
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    System.out.println("Product registered successfully");

                    String responseBody = response.body().string();
                    System.out.println("Response body: " + responseBody);

                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                    String smartstoreChannelProductNo = jsonResponse.get("smartstoreChannelProductNo").getAsString();
                    System.out.println("Parsed Smartstore Channel Product Number: " + smartstoreChannelProductNo);

                    // DB에 smartstoreChannelProductNo 저장
                    saveOriginProductNo(smartstoreChannelProductNo);
                    System.out.println("Smartstore Channel Product Number saved to database");
                } else {
                    System.out.println("Failed to register product. Response code: " + response.code());
                    System.out.println("Response body: " + response.body().string());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        }
    }

    public List<NaverEntity> getRegisteredProducts() {
        return naverRepository.findAll();
    }

    public void deleteProduct() throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();
        String accessToken = getToken();
        List<NaverEntity> registerProducts = getRegisteredProducts(); // 오늘 생성된 상품들 가져오기
        System.out.println(registerProducts);
        for (NaverEntity registerProduct : registerProducts) {
            String channelProductNo = registerProduct.getOriginProductNo();
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
            Thread.sleep(1000);
        }
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

    private String registerWithDelay(String imageUrl) throws IOException, ParseException {
        try {
            Thread.sleep(1000); // 1초 지연
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return register(imageUrl);
    }

    private void addOptionalImageWithDelay(JsonArray optionalImages, String imageUrl) throws IOException, ParseException {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return; // URL이 null이거나 비어있으면 스킵
        }

        try {
            String uploadedUrl = registerWithDelay(imageUrl);
            if (uploadedUrl != null) {
                optionalImages.add(createImageObject(uploadedUrl));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            // 오류 발생 시 해당 URL 스킵
        }
    }
    public void deleteAllProducts() {
        naverRepository.deleteAll();
    }

    private String generateSignature(String clientId, String clientSecret, Long timestamp) {
        String password = StringUtils.join(Arrays.asList(new String[]{clientId, timestamp.toString()}), "_");
        String hashedPw = BCrypt.hashpw(password, clientSecret);
        return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
    }

    private String extractAccessToken(String jsonResponse) {
        // Assuming the JSON response is in the format: {"access_token":"your_access_token","expires_in":3600,"token_type":"Bearer"}
        return jsonResponse.split("\"access_token\":\"")[1].split("\"")[0];
    }

    private JsonObject createImageObject(String url) {
        JsonObject image = new JsonObject();
        image.addProperty("url", url);
        return image;
    }

    private String downloadImage(String imageUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(imageUrl).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("이미지 다운로드 실패: " + response);
        }

        // 이미지 확장자 확인
        String extension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1).toLowerCase();
        // 허용된 확장자인지 확인
        if (!Arrays.asList("jpg", "jpeg", "png", "bmp", "gif").contains(extension)) {
            throw new IOException("지원되지 않는 이미지 형식: " + extension);
        }

        // 이미지 데이터를 파일로 저장
        File tempFile = File.createTempFile("upload", "." + extension);
        Files.write(Paths.get(tempFile.getAbsolutePath()), response.body().bytes());
        return tempFile.getAbsolutePath();
    }


    private String register(String imageUrl) throws IOException, ParseException {
        String API_URL = "https://api.commerce.naver.com/external/v1/product-images/upload";
        String ACCESS_TOKEN = getToken();
        String imageFilePath = downloadImage(imageUrl);
        String extension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1).toLowerCase();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        File imageFile = new File(imageFilePath);
        // 이미지 확장자에 따라 MediaType 설정
        RequestBody fileBody = null;
        if (extension.equals("png")) {
            fileBody = RequestBody.create(MediaType.parse("image/png"), imageFile);
        } else if(extension.equals("jpg")){
            fileBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        } else if(extension.equals("gif")){
            fileBody = RequestBody.create(MediaType.parse("image/gif"), imageFile);
        } else if(extension.equals("bmp")){
            fileBody = RequestBody.create(MediaType.parse("image/bmp"), imageFile);
        } else {
            System.err.println("지원되지 않는 이미지 형식: " + extension);
            return null; // 지원되지 않는 이미지 형식일 경우 null 반환
        }

        //RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);

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
            System.err.println("이미지 업로드 실패: " + response);
            response.close(); // response body를 사용 후 즉시 닫기
            return null; // 실패 시 null 반환
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
}
