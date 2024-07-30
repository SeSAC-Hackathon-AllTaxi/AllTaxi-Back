package com.sesac.alltaxi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

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
