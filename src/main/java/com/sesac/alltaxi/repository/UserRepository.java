package com.sesac.alltaxi.repository;

import com.sesac.alltaxi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}