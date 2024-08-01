package com.sesac.alltaxi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverMatchResponseDto {
    private Long requestId;
    private Long userId;
    private Long driverId;
    private String driverName;
    private String driverPhoneNumber;
    private String driverCarNumber;
    private String pickupLocation;
    private String destinationLocation;
    private String destinationName;
    private String destinationAddress;
    private String imageUrl;
    private String status;
}
