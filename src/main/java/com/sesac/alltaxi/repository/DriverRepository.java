package com.sesac.alltaxi.repository;

import com.example.model.TaxiDriver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<TaxiDriver, Long> {
}