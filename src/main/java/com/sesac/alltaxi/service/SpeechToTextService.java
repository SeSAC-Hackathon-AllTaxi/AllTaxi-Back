package com.sesac.alltaxi.service;

import org.springframework.stereotype.Service;

@Service
public class SpeechToTextService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String voiceToTextApiUrl = "https://api.example.com/voice-to-text";

    public String convertSpeechToText(byte[] audioData) {
        String response = restTemplate.postForObject(voiceToTextApiUrl, audioData, String.class);
        return "텍스트 변환된 결과";
    }
}
