package com.sesac.alltaxi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCreateDto {
    private Long userId;
    private String placeName;
    private String address;
    private double latitude;
    private double longitude;
}