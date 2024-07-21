package com.sesac.alltaxi.repository;

import com.sesac.alltaxi.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
