package com.proofchain.admin.institution.infrastructure.repository.storage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Component
@AllArgsConstructor
public class DownloadImageToRF2 {
    private S3Client s3Client;
    private S3Presigner s3Presigner;
    private R2Config r2Config;

    public String generateDownloadUrl(String filename){
        if(filename == null || filename.isEmpty()){
            return null;
        }
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket("proofchain-assets")
                .key(filename)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(request)
                .build();

        return s3Presigner.presignGetObject(presignRequest)
                .url()
                .toExternalForm();
    }
}
