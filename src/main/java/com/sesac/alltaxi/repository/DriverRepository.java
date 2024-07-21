package com.sesac.alltaxi.repository;

import com.sesac.alltaxi.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}