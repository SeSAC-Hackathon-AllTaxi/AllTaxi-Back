package com.sesac.alltaxi.controller;

import com.drew.imaging.ImageProcessingException;
import com.sesac.alltaxi.response.ApiResponse;
import com.sesac.alltaxi.dto.*;
import com.sesac.alltaxi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class MainController {

//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private DriverService driverService;
    @Autowired
    private GuardianService guardianService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private AiController aiController;

//    @Autowired
//    private GenerativeAiService generativeAiService;

//    @Autowired
//    private SpeechToTextService speechToTextService;



//    @PostMapping("/user")
//    public User createUser(@RequestBody User user) {
//        return userService.saveUser(user);
//    }
//
//    @PostMapping("/driver")
//    public Driver createDriver(@RequestBody Driver driver) {
//        return driverService.saveDriver(driver);
//    }

//    @PostMapping("/request")
//    public AiDestinationResponseDto createRequest(@RequestBody UserVoiceDto userVoiceDto) {
//        // 음성 -> 텍스트 변환 및 생성형 AI 호출 로직 추가
//        String convertedText = speechToTextService.convertSpeechToText(userVoiceDto.getAudioData());
//        // 도출한 목적지 정보(이름, 전화번호, 주소, 영업 시간 등) 받아옴
//        AiDestinationResponseDto aiDestinationResponse = generativeAiService.getGenerativeAiResponse(convertedText);
//        // client에 전달해 유추한 목적지 정보 노출
//        return aiDestinationResponse;
//        // 추후 고객의 수락/재설정 여부에 따라 request 생성하는 부분 구현
//        // requestService.saveRequest(request);
//    }

    @PostMapping("/set-pickup-point/{requestId}")
    public PickUpResponseDto setPickupPoint(@RequestPart(value = "file") MultipartFile image, @PathVariable("requestId") Long requestId) throws ImageProcessingException, IOException {
        String imageKey = aiController.PutS3(image);
        return requestService.setPickupPoint(image, requestId, imageKey);
    }

    @PostMapping("/send-sms/{requestId}")
    public void sendSMS(@PathVariable("requestId") Long requestId){
        guardianService.sendSMS(requestId);
        return ;
    }

    @PostMapping("/set-destination-point/{requestId}")
    public ResponseEntity<ApiResponse<Void>> setDestinationPoint(@PathVariable("requestId") Long requestId,
                                                                 @RequestParam("placeName") String placeName,
                                                                 @RequestParam("address") String address,
                                                                 @RequestParam("latitude") double latitude,
                                                                 @RequestParam("longitude") double longitude) {
        ApiResponse<Void> response = requestService.setDestinationPoint(requestId, placeName, address, latitude, longitude);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}