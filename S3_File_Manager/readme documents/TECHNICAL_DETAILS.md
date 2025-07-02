
---

## TECHNICAL_DETAILS.md (Developer Guide)
This file will contain technical setup instructions.

```md

## System Architecture
The project consists of the following key components:

1. Spring Boot Backend - Handles file uploads and email notifications.
2. AWS S3 Storage - Stores uploaded files securely.
3. Pre-Signed URLs - Used for controlled file access.
4. SMTP Email Service - Sends notifications via JavaMailSender.


# Technical Details: AWS S3 File Upload with Email Notification

## Tech Stack
- Java 17 (Core programming language)
- Spring Boot (Web & backend framework)
- Spring Mail (JavaMailSender) (For sending email notifications)
- AWS SDK for Java (For S3 file handling)
- Maven (Dependency management & build tool)

---

## Configuration Setup

### 1. AWS S3 Setup
Create an S3 bucket and obtain the credentials:
- Bucket Name: `your-bucket-name`
- AWS Region: `your-region`
- Access Key & Secret Key

### 2. SMTP Email Setup
To send emails, configure SMTP settings in `application.properties`:

```properties
# AWS S3 Configuration
aws.s3.bucket-name=your-bucket-name
aws.s3.region=your-region
aws.access-key=your-access-key
aws.secret-key=your-secret-key

# Email Configuration (Google SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

