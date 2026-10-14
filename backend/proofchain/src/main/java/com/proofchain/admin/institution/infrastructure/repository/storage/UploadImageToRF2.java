package com.proofchain.admin.institution.infrastructure.repository.storage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageToRF2 {

    private S3Client s3Client;

    public String uploadImageToRF2(MultipartFile file) throws IOException {

        // Implementação do upload para o Cloudflare R2 usando o s3Client
        if (file == null || file.isEmpty()) {
            throw new IOException("O Arquivo enviado está vazio ou é inválido.");
        }

        try {
            // Gera o nome do arquivo UUID + nome original para não haver conflito de nomes
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : ".jpg";
            String fullName = UUID.randomUUID().toString() + extension;

            // Monta a requisição do objeto para o Cloudflare R2
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("proofchain-assets")
                    .key(fullName)
                    .contentType(file.getContentType()) // Define o MIME-type correto (ex: image/jpeg)
                    .build();

            // Envia o fluxo de bytes diretamente para o R2
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));


            // Retorna o nome do arquivo gravado ou a URL pública/customizada configurada no R2
            return fullName;

        } catch (
                IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviable o arquivo: " + e.getMessage());
        }
    }
}