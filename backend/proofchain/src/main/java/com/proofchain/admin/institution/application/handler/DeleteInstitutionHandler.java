package com.proofchain.admin.institution.application.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DeleteInstitutionHandler {

    private final InstitutionRepository institutionRepository;
    private final TenantValidation tenantValidation;

    @Transactional
    public void deleteinstitution(String cnpj){

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */
        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);


        Institution institution = institutionRepository.findByCnpjAndDeletedAtIsNull(cnpj)
                .orElseThrow(() -> new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND));
        institution.setActive(false);
        institution.setDeletedAt(Instant.now());
    }
}
