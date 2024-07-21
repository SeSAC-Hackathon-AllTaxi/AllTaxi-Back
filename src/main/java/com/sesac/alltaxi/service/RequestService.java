package com.sesac.alltaxi.service;

import com.example.model.Request;
import com.example.model.User;
import com.example.model.TaxiDriver;
import com.example.repository.RequestRepository;
import com.example.repository.UserRepository;
import com.example.repository.TaxiDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    public Request createRequest(Long userId, String pickupLocation, String destination) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Request request = new Request();
        request.setUser(user);
        request.setPickupLocation(pickupLocation);
        request.setDestination(destination);
        request.setStatus("pending");
        return requestRepository.save(request);
    }

    public Request assignDriver(Long requestId, Long driverId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Driver not found"));
        request.setDriver(driver);
        request.setStatus("assigned");
        return requestRepository.save(request);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }
}