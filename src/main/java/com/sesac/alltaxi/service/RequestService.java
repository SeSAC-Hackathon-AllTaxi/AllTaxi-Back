package com.sesac.alltaxi.service;

import com.sesac.alltaxi.domain.Request;
import com.sesac.alltaxi.domain.User;
import com.sesac.alltaxi.domain.Driver;
import com.sesac.alltaxi.dto.RequestDto;
import com.sesac.alltaxi.repository.RequestRepository;
import com.sesac.alltaxi.repository.UserRepository;
import com.sesac.alltaxi.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    public Request createRequest(RequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Request request = new Request();
        request.setUser(user);
        request.setPickupLocation(requestDto.getPickupLocation());
        request.setDestinationLocation(requestDto.getDestinationLocation());
        request.setDestinationName(requestDto.getDestinationName());
        request.setDestinationAddress(requestDto.getDestinationAddress());
        request.setImageUrl(requestDto.getImageUrl());
        request.setStatus("pending");
        return requestRepository.save(request);
    }

    public Request assignDriver(Long requestId, Long driverId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        request.setDriver(driver);
        request.setStatus("assigned");
        return requestRepository.save(request);
    }

    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }
}
