package com.proofchain.admin.institution.application.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.admin.institution.interfaces.dtos.request.UpdateInstitutionRequest;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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


    private S3Client s3Client;
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

    public void updateCurrentInstitution(UpdateInstitutionRequest request, MultipartFile logo, MultipartFile signature) {
        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);

        Institution institution = institutionRepository.findByIdAndDeletedAtIsNull(institutionId)
                .orElseThrow(() -> new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND));
        institution.updateFrom(request);
        String logoKey = uploadImageToRF2(logo);

        if(logoKey.isEmpty()){
            throw new NotFoundException("Erro ao gravar a imagem do logotipo..");
        }
        String signatureKey = uploadImageToRF2(signature);
        if(signatureKey.isEmpty()){
            throw new NotFoundException("Erro ao gravar a imagem da assinatura.");
        }

        institution.setLogoKey(logoKey);
        institution.setSignatureKey(signatureKey);

        institutionRepository.save(institution);
    }

    /*
     * =========================================================
     * ENVIO DE IMAGENS PARA CLOUDFLARE R2 (S3 COMPATÍVEL)
     * =========================================================
     */

    private String uploadImageToRF2(MultipartFile file) {
        // Implementação do upload para o Cloudflare R2 usando o s3Client

        if(file.isEmpty()){
            throw new NotFoundException("Arquivo " + file.getName()+ " não pode ser vazio.");
        }

        try{
            // Gera o nome do arquivo UUID + nome original para não havr conflito de nomes
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
            return null;
        }
    }
}
