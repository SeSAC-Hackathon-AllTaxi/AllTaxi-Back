package com.sesac.alltaxi.service;

import org.springframework.stereotype.Service;

@Service
public class SpeechToTextService {

    public String convertSpeechToText(byte[] audioData) {

        return "텍스트 변환된 결과";
    }
}
