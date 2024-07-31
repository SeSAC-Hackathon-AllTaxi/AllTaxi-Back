package com.sesac.alltaxi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {
    private Long userId;
    private Long driverId;
    private String pickupLocation;
    private String destinationLocation;
    private String destinationName;
    private String destinationAddress;
    private String imageUrl;
    private String status;
}
