package com.sesac.alltaxi.controller;

import com.sesac.alltaxi.domain.Driver;
import com.sesac.alltaxi.domain.Request;
import com.sesac.alltaxi.domain.User;
import com.sesac.alltaxi.dto.AiDestinationResponseDto;
import com.sesac.alltaxi.dto.UserVoiceDto;
import com.sesac.alltaxi.service.DriverService;
import com.sesac.alltaxi.service.RequestService;
import com.sesac.alltaxi.service.UserService;
import com.sesac.alltaxi.service.GenerativeAiService;
import com.sesac.alltaxi.service.SpeechToTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private GenerativeAiService generativeAiService;

    @Autowired
    private SpeechToTextService speechToTextService;

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/driver")
    public Driver createDriver(@RequestBody Driver driver) {
        return driverService.saveDriver(driver);
    }

    @PostMapping("/request")
    public AiDestinationResponseDto createRequest(@RequestBody UserVoiceDto userVoiceDto) {
        // 음성 -> 텍스트 변환 및 생성형 AI 호출 로직 추가
        String convertedText = speechToTextService.convertSpeechToText(userVoiceDto.getAudioData());
        // 도출한 목적지 정보(이름, 전화번호, 주소, 영업 시간 등) 받아옴
        AiDestinationResponseDto aiDestinationResponse = generativeAiService.getGenerativeAiResponse(convertedText);
        // client에 전달해 유추한 목적지 정보 노출
        return aiDestinationResponse;
        // 추후 고객의 수락/재설정 여부에 따라 request 생성하는 부분 구현
        // requestService.saveRequest(request);
    }

    @GetMapping("/request/{id}")
    public Request getRequest(@PathVariable Long id) {
        return requestService.getRequestById(id).orElse(null);
    }
}
