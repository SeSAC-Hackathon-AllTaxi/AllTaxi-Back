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
    private String pickupLocation;
    private String destinationLocation;

    private String destinationName;
    private String destinationAddress;
    private String imageUrl;
    private String status;

    @ManyToOne
    private User user;

    @ManyToOne
    private Driver driver;
}
