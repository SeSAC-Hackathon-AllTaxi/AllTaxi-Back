package com.sesac.alltaxi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DestinationResponseDto {
    private String destinationLocation;
    private String destinationName;
    private String destinationAddress;
}
