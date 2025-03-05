package com.plx.app.itf.service;

import com.plx.app.itf.controller.WebsocketHandler;
import com.plx.app.itf.vo.ApiResponseBody;
import com.plx.app.itf.vo.GeoLocationRequestDTO;
import com.plx.app.itf.vo.InplabRequestDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

@Service
public class GeoLocationService {

    protected Log logger = LogFactory.getLog(getClass());

    @Value("#{globalProp['pluxity.api.key']}")
    private String plxApiKey;

    @Value("#{globalProp['geoplan.api.uri']}")
    private String geoPlanApiUri;

    @Value("#{globalProp['geoplan.api.key']}")
    private String geoPlanApiKey;


    public ApiResponseBody postGeoLocation(GeoLocationRequestDTO dto) throws IOException {

        ApiResponseBody result;

        String paramKey = dto.getApikey();

        if(!StringUtils.isEmpty(paramKey) && plxApiKey.equals(paramKey)) {
            logger.info("API KEY 검증 완료");
        } else {
            throw new RuntimeException("API KEY 누락 혹은 맞지 않음");
        }

        String id = dto.getId();
        String location = dto.getLocation();
        String floor = dto.getFloor();
        String buildingCode = dto.getBuildingCode();

        if(StringUtils.isAnyBlank(id, buildingCode, location, floor)) {
            throw new RuntimeException("필수 파라미터 누락");
        }

        result = ApiResponseBody.builder()
                .result(HttpStatus.OK)
                .message("SUCCESS").
                build();

//        WebsocketHandler.broadcastMessage(dto.toString());
        WebsocketHandler.broadcastMessage("GEOPLAN", dto.toString());

        return result;
    }

    public ApiResponseBody postInplabGeoLocation(InplabRequestDTO dto) throws IOException {

        ApiResponseBody result;

        String paramKey = dto.getApikey();

        if(!StringUtils.isEmpty(paramKey) && plxApiKey.equals(paramKey)) {
            logger.info("API KEY 검증 완료");
        } else {
            throw new RuntimeException("API KEY 누락 혹은 맞지 않음");
        }

        String id = dto.getId();
        Double lon = dto.getLon();
        Double lat = dto.getLat();
        String floor = dto.getFloor();
        String buildingCode = dto.getBuildingCode();

        if(StringUtils.isAnyBlank(id, buildingCode, floor)) {
            throw new RuntimeException("필수 파라미터 누락");
        }

        if(lon.isNaN() || lat.isNaN()) {
            throw new RuntimeException("좌표 파라미터 이상 (" + lon + ", " + lat + ")");
        }

        WebsocketHandler.broadcastMessage("INPLAB", dto.toString());

        result = ApiResponseBody.builder()
                .result(HttpStatus.OK)
                .message("SUCCESS").
                build();

        return result;
    }

    public String getLocations() {
        String response = sendGetRequest(geoPlanApiUri);
        return response;
    }

    private String sendGetRequest(String apiUrl) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            disableSSLVerification();

            // URL 객체 생성
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();

            // 요청 설정
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000); // 연결 타임아웃 (밀리초)
            connection.setReadTimeout(3000);    // 읽기 타임아웃 (밀리초)

            connection.setRequestProperty("Authorization", "Bearer " + geoPlanApiKey);

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                // 응답 데이터 읽기
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                return "HTTP 요청 실패: " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "예외 발생: " + e.getMessage();
        } finally {
            // 리소스 정리
            try {
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void disableSSLVerification() throws Exception {
        // TrustManager 설정
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
        };

        // SSLContext 초기화
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // HostnameVerifier 설정
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}
