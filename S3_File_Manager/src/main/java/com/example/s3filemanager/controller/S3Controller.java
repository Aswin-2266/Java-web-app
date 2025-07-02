package com.example.s3filemanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.s3filemanager.service.S3Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service; 
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(s3Service.uploadFile(file));
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        return ResponseEntity.ok(s3Service.listFiles());
    }

    @GetMapping("/download/{key}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable String key) {
        return ResponseEntity.ok(s3Service.getPresignedUrl(key));
    }

    @DeleteMapping("/delete/{key}")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String key) {
        s3Service.deleteFile(key);
        Map<String, String> response = new HashMap<>();
        response.put("message", "File deleted successfully: " + key);
        return ResponseEntity.ok(response);
    }
}