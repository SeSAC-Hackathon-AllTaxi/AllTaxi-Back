package com.sesac.alltaxi.service;

import com.sesac.alltaxi.domain.Guardian;
import com.sesac.alltaxi.domain.User;
import com.sesac.alltaxi.dto.GuardianDto;
import com.sesac.alltaxi.repository.GuardianRepository;
import com.sesac.alltaxi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuardianService {

    @Autowired
    private GuardianRepository guardianRepository;

    @Autowired
    private UserRepository userRepository;

    public Guardian createGuardian(GuardianDto guardianDTO) {
        User user = userRepository.findById(guardianDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Guardian guardian = new Guardian();
        guardian.setUser(user);
        guardian.setName(guardianDTO.getName());
        guardian.setPhoneNumber(guardianDTO.getPhoneNumber());
        return guardianRepository.save(guardian);
    }

    public Guardian updateGuardian(Long id, GuardianDto guardianDTO) {
        Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guardian not found"));
        User user = userRepository.findById(guardianDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        guardian.setUser(user);
        guardian.setName(guardianDTO.getName());
        guardian.setPhoneNumber(guardianDTO.getPhoneNumber());
        return guardianRepository.save(guardian);
    }

    public void deleteGuardian(Long id) {
        guardianRepository.deleteById(id);
    }

    public Optional<Guardian> getGuardianById(Long id) {
        return guardianRepository.findById(id);
    }

    public List<Guardian> getAllGuardians() {
        return guardianRepository.findAll();
    }
}
