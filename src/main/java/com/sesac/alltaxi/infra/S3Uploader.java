package com.sesac.alltaxi.infra;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    // s3 버킷에 객체(이미지, ...) 집어넣는 메서드
    public String put(MultipartFile file) {
        // 객체 키 설정 로직 임시로 랜덤 설정
        String key = String.valueOf(ThreadLocalRandom.current().nextInt(10000000, 100000000));

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(
                    ByteBuffer.wrap(file.getBytes())));

            log.info("Picture uploaded successfully to S3");
            return key;
        }
        catch (IOException | S3Exception e) {
            log.error("Error uploading picture to S3: " + e.getMessage());
            return "error";
        }
    }

    // 객체 키를 기준으로 버킷의 객체를 삭제하는 메서드
    public boolean delete(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            log.info("Picture deleted successfully from S3");
            return true;
        }
        catch (Exception e) {
            log.error("Error deleting picture from S3: " + e.getMessage());
            return false;
        }
    }
}
