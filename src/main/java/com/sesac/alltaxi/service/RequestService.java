package com.sesac.alltaxi.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.sesac.alltaxi.domain.Request;
import com.sesac.alltaxi.domain.User;
import com.sesac.alltaxi.domain.Driver;
import com.sesac.alltaxi.dto.PickUpResponseDto;
import com.sesac.alltaxi.dto.RequestDto;
import com.sesac.alltaxi.repository.RequestRepository;
import com.sesac.alltaxi.repository.UserRepository;
import com.sesac.alltaxi.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    public Request createRequest(RequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Request request = new Request();
        request.setUser(user);
        request.setPickupLocation(requestDto.getPickupLocation());
        request.setDestinationLocation(requestDto.getDestinationLocation());
        request.setDestinationName(requestDto.getDestinationName());
        request.setDestinationAddress(requestDto.getDestinationAddress());
        request.setImageUrl(requestDto.getImageUrl());
        request.setStatus("pending");
        return requestRepository.save(request);
    }

    public Request assignDriver(Long requestId, Long driverId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        request.setDriver(driver);
        request.setStatus("assigned");
        return requestRepository.save(request);
    }

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
        request.setPickupLocation(String.valueOf(longitude)+","+String.valueOf(latitude));
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

    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }
}
