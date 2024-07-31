package com.sesac.alltaxi.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.sesac.alltaxi.dto.GeoCodingDto;
import com.sesac.alltaxi.dto.GeoCodingResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Service
public class GeoCodingService {

    @Value("${google.api.key}")
    private String apiKey;

    private static final Logger logger = Logger.getLogger(GeoCodingService.class.getName());

    public GeoCodingResponseDto getCoordinates(String address) {
        try {
            // Google Translate API 설정
            Translate translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
            Translation translation = translate.translate(address, Translate.TranslateOption.sourceLanguage("ko"), Translate.TranslateOption.targetLanguage("en"));

            String translatedAddress = translation.getTranslatedText();
            logger.info("Translated Address: " + translatedAddress);

            // 한글 주소를 URL 인코딩
            String encodedAddress = URLEncoder.encode(translatedAddress, StandardCharsets.UTF_8.toString());
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + apiKey;

            logger.info("Request URL: " + url);

            RestTemplate restTemplate = new RestTemplate();
            GeoCodingResponseDto response = restTemplate.getForObject(url, GeoCodingResponseDto.class);

            if (response != null && !response.getResults().isEmpty()) {
                GeoCodingDto result = response.getResults().get(0);
                String formattedAddress = result.getFormattedAddress();
                double lat = result.getGeometryDto().getLocationDto().getLat();
                double lng = result.getGeometryDto().getLocationDto().getLng();

                logger.info("Response: " + response);

                return new GeoCodingResponseDto(lat, lng, formattedAddress);
            } else {
                logger.warning("No results found for the given address: " + address);
                throw new RuntimeException("No results found for the given address: " + address);
            }
        } catch (Exception e) {
            logger.severe("Error while getting coordinates for address: " + address + ", message: " + e.getMessage());
            throw new RuntimeException("Error while getting coordinates for address: " + address + ", message: " + e.getMessage(), e);
        }
    }
}
