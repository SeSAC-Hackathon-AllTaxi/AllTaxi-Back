package SeSAC.AllTaxi.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SpeechToTextService {

    @Value("${google.cloud.credentials.path}")
    private String credentialsPath;

    public String convertSpeechToText(byte[] audioBytes) throws IOException {
        try (InputStream credentialsStream = new ClassPathResource(credentialsPath).getInputStream()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
            SpeechClient speechClient = SpeechClient.create(SpeechSettings.newBuilder().setCredentialsProvider(() -> credentials).build());

            // Configure the request
            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();

            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(audioBytes))
                    .build();

            // Detect speech
            List<SpeechRecognitionResult> results = speechClient.recognize(recognitionConfig, recognitionAudio)
                    .getResultsList();

            // Extract text from results
            StringBuilder transcript = new StringBuilder();
            for (SpeechRecognitionResult result : results) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                transcript.append(alternative.getTranscript());
            }

            return transcript.toString();
        }
    }
}
