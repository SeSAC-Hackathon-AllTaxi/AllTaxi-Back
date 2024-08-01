package com.sesac.alltaxi.service;

import com.sesac.alltaxi.domain.Driver;
import com.sesac.alltaxi.domain.User;
import com.sesac.alltaxi.dto.DriverMatchResponseDto;
import com.sesac.alltaxi.repository.UserRepository;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverService driverService;

    public ApiResponse<Long> createRequestWithDestination(Long userId, String placeName, String address, double latitude, double longitude) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Request request = new Request();
        request.setUser(user);
        request.setDestinationName(placeName);
        request.setDestinationAddress(address);
        request.setDestinationLocation(latitude + "," + longitude);
        request.setStatus("created");

        requestRepository.save(request);

        updateRecentAddresses(user, placeName);

        return ApiResponse.ok(request.getId());
    }

    private void updateRecentAddresses(User user, String address) {
        List<String> recentAddresses = user.getRecentAddresses();
        if (recentAddresses == null) {
            recentAddresses = new ArrayList<>();
        }
        recentAddresses.remove(address); // 중복 제거
        recentAddresses.add(0, address); // 맨 앞에 추가
        if (recentAddresses.size() > 3) {
            recentAddresses.remove(3); // 최대 3개 유지
        }
        user.setRecentAddresses(recentAddresses);
        userRepository.save(user);
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

    public DriverMatchResponseDto matchTaxi(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            throw new RuntimeException("No available drivers found");
        }

        // 픽업 위치를 가져옴
        String[] pickupLocation = request.getPickupLocation().split(",");
        double pickupLatitude = Double.parseDouble(pickupLocation[0]);
        double pickupLongitude = Double.parseDouble(pickupLocation[1]);

        // 가장 가까운 드라이버 찾기
        Driver matchedDriver = availableDrivers.stream()
                .min(Comparator.comparingDouble(driver -> distance(pickupLatitude, pickupLongitude, driver.getLatitude(), driver.getLongitude())))
                .orElseThrow(() -> new RuntimeException("No available drivers found"));

        request.setDriver(matchedDriver);
        request.setStatus("matched");
        requestRepository.save(request);

        DriverMatchResponseDto responseDto = new DriverMatchResponseDto();
        responseDto.setRequestId(request.getId());
        responseDto.setUserId(request.getUser().getId());
        responseDto.setDriverId(matchedDriver.getId());
        responseDto.setDriverName(matchedDriver.getName());
        responseDto.setDriverPhoneNumber(matchedDriver.getPhoneNumber());
        responseDto.setDriverCarNumber(matchedDriver.getCarNumber());
        responseDto.setPickupLocation(request.getPickupLocation());
        responseDto.setDestinationLocation(request.getDestinationLocation());
        responseDto.setDestinationName(request.getDestinationName());
        responseDto.setDestinationAddress(request.getDestinationAddress());
        responseDto.setImageUrl(request.getImageUrl());
        responseDto.setStatus(request.getStatus());
        responseDto.setDriverLatitude(matchedDriver.getLatitude());
        responseDto.setDriverLongitude(matchedDriver.getLongitude());
        return responseDto;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}