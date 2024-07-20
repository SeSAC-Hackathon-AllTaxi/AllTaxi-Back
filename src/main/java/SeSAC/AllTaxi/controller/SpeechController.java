package SeSAC.AllTaxi.controller;

import SeSAC.AllTaxi.service.SpeechToTextService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/speech")
public class SpeechController {

    private final SpeechToTextService speechToTextService;

    public SpeechController(SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    @PostMapping("/convert")
    public String convertSpeechToText(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] audioBytes = file.getBytes();
        return speechToTextService.convertSpeechToText(audioBytes);
    }
}
