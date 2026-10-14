package com.proofchain.admin.institution.application.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.institution.infrastructure.repository.storage.UploadImageToRF2;
import com.proofchain.admin.institution.interfaces.dtos.request.UpdateInstitutionRequest;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateInstitutionHandler {


    @Autowired
    private S3Client s3Client;
    private UploadImageToRF2 uploadImage;
    private final InstitutionRepository institutionRepository;
    private final TenantValidation tenantValidation;

    public void updateinstitution(String cnpj, UpdateInstitutionRequest UpdateInstitutionRequest){

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */
        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);


        Institution institution = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj)
                .orElseThrow(() -> new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND));

        institution.updateFrom(UpdateInstitutionRequest);
        institutionRepository.save(institution);
    }

    public void updateCurrentInstitution(UpdateInstitutionRequest request, MultipartFile logo, MultipartFile signature) throws IOException {
        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);

        Institution institution = institutionRepository.findByIdAndDeletedAtIsNull(institutionId)
                .orElseThrow(() -> new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND));
        institution.updateFrom(request);

        //Faz upload de imagens para o cloudFlare R2
        String logoKey = uploadImage.uploadImageToRF2(logo);

        // Se a instituição já tinha um logo antigo, deleta do R2 após gravação com sucesso
        if (institution.getLogoKey() != null) {
            s3Client.deleteObject(d -> d.bucket("proofchain-assets").key(institution.getLogoKey()));
        }

        String signatureKey = uploadImage.uploadImageToRF2(logo);

        // Se a instituição já tinha um logo antigo, deleta do R2 após gravação com sucesso
        if (institution.getSignatureKey() != null) {
            s3Client.deleteObject(d -> d.bucket("proofchain-assets").key(institution.getSignatureKey()));
        }

        institution.updateFrom(request);
        institution.setLogoKey(logoKey);
        institution.setSignatureKey(signatureKey);

        institutionRepository.save(institution);
    }

    /*
     * =========================================================
     * ENVIO DE IMAGENS PARA CLOUDFLARE R2 (S3 COMPATÍVEL)
     * =========================================================
     */

    private String uploadImageToRF2(MultipartFile file) throws IOException {
        // Implementação do upload para o Cloudflare R2 usando o s3Client
        if(file == null || file.isEmpty()){
            throw new IOException("O Arquivo enviado está vazio ou é inválido.");
        }

        try{
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

        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviable o arquivo: " + e.getMessage());
        }
    }
}
