package com.sesac.alltaxi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestForDriverDto {
    private String destinationName;
    private String destinationAddress;
    private String imageUrl;
    private String imageDescription;
}
