package com.sesac.alltaxi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PickUpResponseDto {
    private String pickupLocation;
    private String destinationLocation;
}
