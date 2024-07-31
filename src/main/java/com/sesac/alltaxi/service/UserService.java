package com.sesac.alltaxi.service;

import com.sesac.alltaxi.domain.Request;
import com.sesac.alltaxi.domain.User;
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

import java.util.Optional;

@Service
public class UserService {
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    private DefaultMessageService messageService;
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    // 단일 메시지 발송 예제
    public void sendSMS(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01062713407");
        message.setTo("010수신번호");
        message.setText("[올택시]\n이용자 택시 탑승 안내\n 출발지:"+request.getPickupLocation()+"\n 목적지:"+request.getDestination());
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        return ;
    }
}
