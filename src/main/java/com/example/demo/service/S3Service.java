package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {
    
    private final S3Client s3;
    private final S3Presigner presigner;
    private final String bucketName;

    private final EmailService emailService;

public S3Service(@Value("${aws.region}") String region,
                 @Value("${aws.s3.bucket}") String bucketName,
                 EmailService emailService) {
    this.bucketName = bucketName;
    this.emailService = emailService;

    this.s3 = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    this.presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
}


    public String uploadFile(MultipartFile file) {
        String key = file.getOriginalFilename();
        try {
            s3.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
    
            emailService.sendFileUploadSuccessEmail(key);
    
            return "File uploaded successfully: " + key;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }
    

    public List<String> listFiles() {
        try {
            return s3.listObjectsV2(ListObjectsV2Request.builder()
                            .bucket(bucketName)
                            .build())
                    .contents()
                    .stream()
                    .map(S3Object::key)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to list files: " + e.getMessage(), e);
        }
    }

    public String getPresignedUrl(String key) {
        try {
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(1)) // URL expires in 1 minutes
                    .getObjectRequest(b -> b.bucket(bucketName).key(key))
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

            return presignedRequest.url().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate pre-signed URL: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String key) {
        try {
            s3.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage(), e);
        }
    }
    
}
