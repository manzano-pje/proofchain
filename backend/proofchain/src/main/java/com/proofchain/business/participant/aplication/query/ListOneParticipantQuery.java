package com.proofchain.business.participant.aplication.query;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.participant.infrastructure.repository.ParticipantRepository;
import com.proofchain.business.participant.interfaces.dto.response.ParticipantResponse;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ListOneParticipantQuery {


    private final ParticipantRepository participantRepository;
    private final InstitutionRepository institutionRepository;
    private final TenantValidation tenantValidation;

    public ParticipantResponse listOneParticipant(Long id) {
        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);
        Institution institution = institutionRepository.findByIdAndDeletedAtIsNull(institutionId)
                .orElseThrow(() -> new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND));

        /*
         * =========================================================
         * CARREGAR DADOS DE PARTICIPANT
         * =========================================================
         */

        Optional<ParticipantResponse> participantOptional = participantRepository.findByIdAndInstitutionId(id, institutionId);
        if (participantOptional.isEmpty()) {
            throw new NotFoundException("Aluno não localizado.");
        }
        ParticipantResponse participant = new ParticipantResponse(participantOptional.get());
        return participant;
    }
}
