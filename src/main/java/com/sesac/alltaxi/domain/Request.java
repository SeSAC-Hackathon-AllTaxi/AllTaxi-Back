package com.sesac.alltaxi.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pickupLocation;
    private String destination;
    private String status;

    @ManyToOne
    private User user;

    @ManyToOne
    private TaxiDriver taxiDriver;

}
