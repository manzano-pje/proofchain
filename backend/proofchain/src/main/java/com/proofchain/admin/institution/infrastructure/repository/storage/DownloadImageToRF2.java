package com.proofchain.admin.institution.infrastructure.repository.storage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class DownloadImageToRF2 {
    private S3Client s3Client;

    public byte[] downloadImageFromR2(String fileName) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket("proofchain-assets")
                .key(fileName)
                .build();

        try (ResponseInputStream<GetObjectResponse> input =
                     s3Client.getObject(request)) {
            return input.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao recuperar a imagem do R2.", e);
        }
    }
}
