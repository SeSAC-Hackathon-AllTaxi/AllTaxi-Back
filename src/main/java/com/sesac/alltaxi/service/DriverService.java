package com.sesac.alltaxi.service;

import com.sesac.alltaxi.domain.Driver;
import com.sesac.alltaxi.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    public Driver saveTaxiDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public Optional<Driver> getTaxiDriverById(Long id) {
        return driverRepository.findById(id);
    }
}
