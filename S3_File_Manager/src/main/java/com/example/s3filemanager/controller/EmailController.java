package com.example.s3filemanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.s3filemanager.service.EmailService;
import com.example.s3filemanager.service.S3Service;

import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final S3Service s3Service;

    public EmailController(EmailService emailService, S3Service s3Service) {
        this.emailService = emailService;
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFileAndSendEmail(@RequestParam("file") MultipartFile file) {
        Map<String, String> result = s3Service.uploadFile(file);
        return ResponseEntity.ok(result);
    }
}