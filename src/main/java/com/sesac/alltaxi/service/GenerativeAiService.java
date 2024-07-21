package com.sesac.alltaxi.service;

import org.springframework.stereotype.Service;

@Service
public class GenerativeAiService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String aiApiUrl = "https://api.example.com/generate-text";
    public AiDestinationResponseDto getGenerativeAiResponse(String inputText) {
        String response = restTemplate.postForObject(aiApiUrl, inputText, String.class);
        // 받은 응답을 AiDestinationResponseDto 객체에 매핑
        AiDestinationResponseDto aiDestinationResponseDto = new AiDestinationResponseDto();
        // "name:주소, address:주소, phoneNumber:전화번호, openingTime:시간"
        // 이와 같은 형식으로 응답이 온다고 가정하고 파싱 진행
        String[] responseParts = response.split(", ");
        for (String part : responseParts) {
            String[] keyValue = part.split(":");
            String key = keyValue[0];
            String value = keyValue[1];

            switch (key) {
                case "name":
                    aiDestinationResponseDto.setName(value);
                    break;
                case "address":
                    aiDestinationResponseDto.setAddress(value);
                    break;
                case "phoneNumber":
                    aiDestinationResponseDto.setPhoneNumber(value);
                    break;
                case "openingTime":
                    aiDestinationResponseDto.setOpeningTime(value);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected key: " + key);
            }
        }
        return aiDestinationResponseDto;
    }
}