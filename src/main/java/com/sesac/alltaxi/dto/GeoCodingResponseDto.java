package com.sesac.alltaxi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoCodingResponseDto {
    private double latitude;
    private double longitude;
    private String formattedAddress;
    private List<GeoCodingDto> results;

    public GeoCodingResponseDto(double latitude, double longitude, String formattedAddress) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.formattedAddress = formattedAddress;
    }
}
