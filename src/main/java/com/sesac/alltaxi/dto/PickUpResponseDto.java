package com.sesac.alltaxi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PickUpResponseDto {
    private List<String> destination = new ArrayList<>();
    private List<String> pickupLocation = new ArrayList<>();
}
