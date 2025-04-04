package com.example.demo.controller;

import com.example.demo.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service; 
    }

    // Upload File
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String key = s3Service.uploadFile(file);
        return ResponseEntity.ok("File uploaded successfully: " + key);
    }

    // List Files
    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        return ResponseEntity.ok(s3Service.listFiles());
    }

    // Generate Pre-Signed URL
    @GetMapping("/download/{key}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable String key) {
        return ResponseEntity.ok(s3Service.getPresignedUrl(key));
    }
}
