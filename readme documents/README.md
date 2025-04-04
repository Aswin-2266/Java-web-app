#  AWS S3 File Upload with Email Notification

This Spring Boot application allows users to upload files to AWS S3 and receive an email notification with a secure pre-signed URL to download the uploaded file.

---

## Features
✔ Upload files directly to AWS S3  
✔ Generate a pre-signed URL for secure access  
✔ Email notification with the file download link  
✔ Works with any SMTP email service (Gmail, Outlook, etc.)  
✔ Secure and configurable expiration time for download links  

---

## Prerequisites  
Before running the application, ensure you have the following:  

✅ Java 17 installed (JDK)  
✅ Maven installed (`mvn -version` to check)  
✅ An AWS S3 bucket set up  
✅ AWS Access Key and Secret Key  
✅ SMTP email credentials (e.g., Gmail App Password)  

---

## 📥 How to Install & Run

### 1. Clone the Repository  
Download the source code to your local system:  
```sh
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name
