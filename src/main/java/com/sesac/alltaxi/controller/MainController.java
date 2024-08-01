package com.sesac.alltaxi.controller;

import com.drew.imaging.ImageProcessingException;
import com.sesac.alltaxi.response.ApiResponse;
import com.sesac.alltaxi.dto.*;
import com.sesac.alltaxi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.sesac.alltaxi.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sesac.alltaxi.infra.S3Uploader;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private GuardianService guardianService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private S3Uploader s3Uploader;

    @Autowired
    private UserService userService;

    @PostMapping("/create-request")
    public ResponseEntity<ApiResponse<Long>> createRequest(@RequestBody RequestCreateDto requestCreateDto) {
        ApiResponse<Long> response = requestService.createRequestWithDestination(
                requestCreateDto.getUserId(),
                requestCreateDto.getPlaceName(),
                requestCreateDto.getAddress(),
                requestCreateDto.getLatitude(),
                requestCreateDto.getLongitude()
        );
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/user/{userId}/recent-addresses")
    public ResponseEntity<List<String>> getRecentAddresses(@PathVariable Long userId) {
        List<String> recentAddresses = userService.getRecentAddresses(userId);
        return ResponseEntity.ok(recentAddresses);
    }

    @PostMapping("/set-pickup-point/{requestId}")
    public PickUpResponseDto setPickupPoint(@RequestPart(value = "file") MultipartFile image, @PathVariable("requestId") Long requestId) throws ImageProcessingException, IOException {
        String imageKey = s3Uploader.put(image);
        return requestService.setPickupPoint(image, requestId, imageKey);
    }

    @PostMapping("/send-sms/{requestId}")
    public void sendSMS(@PathVariable("requestId") Long requestId){
        guardianService.sendSMS(requestId);
        return ;
    }

    @PostMapping("/match-taxi/{requestId}")
    public ResponseEntity<DriverMatchResponseDto> matchTaxi(@PathVariable("requestId") Long requestId) {
        DriverMatchResponseDto response = requestService.matchTaxi(requestId);
        return ResponseEntity.ok(response);
    }

}