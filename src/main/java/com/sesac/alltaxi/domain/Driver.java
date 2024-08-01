package com.sesac.alltaxi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;
    private String carNumber;
    private String status;

    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "driver")
    private List<Request> requests;
}
