package com.sesac.alltaxi.service;

import com.sesac.alltaxi.domain.User;
import com.sesac.alltaxi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<String> getRecentAddresses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getRecentAddresses();
    }
}
