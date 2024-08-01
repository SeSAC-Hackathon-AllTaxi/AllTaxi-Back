package com.sesac.alltaxi.service;

import com.sesac.alltaxi.response.ApiResponse;
import com.sesac.alltaxi.domain.Request;
import com.sesac.alltaxi.dto.PickUpResponseDto;
import com.sesac.alltaxi.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

import java.io.IOException;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public PickUpResponseDto setPickupPoint(MultipartFile image, Long requestId, String imageKey) throws ImageProcessingException, IOException {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        // InputStream을 사용하여 이미지 메타데이터 읽기
        Metadata metadata = ImageMetadataReader.readMetadata(image.getInputStream());
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if (gpsDirectory == null || gpsDirectory.getGeoLocation() == null) {
            throw new RuntimeException("GPS data not found in image");
        }
        double longitude = gpsDirectory.getGeoLocation().getLongitude();
        double latitude = gpsDirectory.getGeoLocation().getLatitude();
        // 픽업 위치 설정
        request.setPickupLocation(String.valueOf(longitude) + "," + String.valueOf(latitude));
        // 이미지 key 저장
        request.setImageUrl(imageKey);
        // 응답 DTO 설정
        PickUpResponseDto pickUpResponseDto = new PickUpResponseDto();
        pickUpResponseDto.setPickupLocation(request.getPickupLocation());
        pickUpResponseDto.setDestinationLocation(request.getDestinationLocation());
        // 요청 저장
        requestRepository.save(request);
        return pickUpResponseDto;
    }

    public ApiResponse<Long> setDestinationPoint(Long requestId, String placeName, String address, double latitude, double longitude) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        // 도착지 정보 설정
        request.setDestinationName(placeName);
        request.setDestinationAddress(address);
        request.setDestinationLocation(latitude + "," + longitude);
        // 요청 저장
        requestRepository.save(request);
        Long id = request.getId();
        return ApiResponse.ok(id);
    }
}