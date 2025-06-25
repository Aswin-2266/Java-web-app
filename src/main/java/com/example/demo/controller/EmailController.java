package com.example.demo.controller;

import com.example.demo.service.EmailService;
import com.example.demo.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<String> uploadFileAndSendEmail(@RequestParam("file") MultipartFile file) {
        String fileName = s3Service.uploadFile(file);
        emailService.sendFileUploadSuccessEmail(file.getOriginalFilename());
        return ResponseEntity.ok("File uploaded successfully and notification email sent.");
    }
}
