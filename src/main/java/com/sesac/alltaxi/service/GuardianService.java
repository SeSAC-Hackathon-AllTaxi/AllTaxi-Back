package com.sesac.alltaxi.service;

import com.sesac.alltaxi.domain.Driver;
import com.sesac.alltaxi.domain.Guardian;
import com.sesac.alltaxi.domain.Request;
import com.sesac.alltaxi.domain.User;
import com.sesac.alltaxi.dto.GuardianDto;
import com.sesac.alltaxi.repository.GuardianRepository;
import com.sesac.alltaxi.repository.RequestRepository;
import com.sesac.alltaxi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuardianService {
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    private DefaultMessageService messageService;

    @Autowired
    private GuardianRepository guardianRepository;

    @Autowired
    private RequestRepository requestRepository;

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
    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    public void sendSMS(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        Driver driver = request.getDriver();
        Message message = new Message();
        message.setFrom("01062713407");
        message.setTo("01062713407");
        message.setText("[올택시 보호자 택시 탑승정보 안내]\n목적지명:"+request.getDestinationName()+
                "\n목적지 주소:"+request.getDestinationAddress()
                +"\n택시기사 전화번호:"+driver.getPhoneNumber()
                +"\n택시번호:"+driver.getCarNumber());
        this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return ;
    }
}
