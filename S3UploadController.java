package com.example.awss3demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class S3UploadController {

    private static final String AWS_ACCESS_KEY = "AKIAEXAMPLEKEY123";
    private static final String AWS_SECRET_KEY = "abc123VerySecretKey+EXAMPLE";
    private static final String BUCKET_NAME = "your-s3-bucket-name";

    private final S3Client s3Client;

    public S3UploadController() {
        // Initialize S3 client with hardcoded credentials
        s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY)))
                .build();
    }

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String keyName = file.getOriginalFilename();

            // Upload file to S3
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(keyName)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));

            return "File uploaded successfully: " + keyName;
        } catch (IOException e) {
            return "Upload failed: " + e.getMessage();
        }
    }
}
