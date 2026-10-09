package com.proofchain.shared.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

public class R2ClientConfig {
    public S3Client s3Client() {
        String accountId = "SEU_ACCOUNT_ID_DO_CLOUDFLARE";
        String accessKey = "SUA_ACCESS_KEY_R2";
        String secretKey = "SUA_SECRET_KEY_R2";

        // O R2 exige uma definição de região para o SDK, embora ela seja ignorada na prática ("auto")
        return S3Client.builder()
                .endpointOverride(URI.create("https://" + accountId + ".r2.cloudflarestorage.com"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.US_EAST_1) // Use uma região padrão como us-east-1 ou "auto"
                .build();
    }
}
