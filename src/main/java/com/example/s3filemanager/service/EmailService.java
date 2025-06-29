package com.example.s3filemanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Duration;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final S3Presigner presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${email.recipient}")
    private String recipientEmail;

    public EmailService(JavaMailSender mailSender, S3Presigner presigner) {
        this.mailSender = mailSender;
        this.presigner = presigner;
    }

    public void sendFileUploadSuccessEmail(String fileName) {
        try {
            String fileUrl = generatePresignedUrl(fileName);
    
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("File Upload Successful");
    
            
            String emailBody = "<p>Your file <b>" + fileName + "</b> has been uploaded successfully.</p>"
                    + "<p><a href=\"" + fileUrl + "\">Click here to download the file</a></p>"
                    + "<p><small>This link will expire in 1 minutes.</small></p>";
    
            helper.setText(emailBody, true);
    
            mailSender.send(message);
            System.out.println("Email sent successfully with download link!");
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
        }
    }
    
    

    private String generatePresignedUrl(String key) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(1)) 
                .getObjectRequest(b -> b.bucket(bucketName).key(key))
                .build();
    
        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
        
        return presignedRequest.url().toString();
    }
    
}
