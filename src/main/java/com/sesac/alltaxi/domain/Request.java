package com.sesac.alltaxi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> pickupLocation = new ArrayList<>();
    @ElementCollection
    private List<String> destination = new ArrayList<>();
    // s3에 올라간 이미지 key 저장
    private String imageKey;
    private String status;

    @ManyToOne
    private User user;

    @ManyToOne
    private Driver driver;
}
