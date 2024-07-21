package com.sesac.alltaxi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {
    private Long id;
    private String pickupLocation;
    private String destination;
    private String status;
    private Long userId;
    private Long driverId;
}
