package com.proofchain.business.participant.aplication.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.participant.domain.model.Participant;
import com.proofchain.business.participant.infrastructure.repository.ParticipantRepository;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DeleteParticipantHandler {

    private final TenantValidation tenantValidation;
    private final ParticipantRepository participantRepository;
    private final InstitutionRepository institutionRepository;

    public void deleteParticipant(Long id) {
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

        Optional<Participant> participantOptional = participantRepository.findByIdAndInstitutionId(id, institution.getId());
        if (participantOptional.isEmpty()) {
            throw new NotFoundException("Participante não encontrado");
        }
        Participant participant = participantOptional.get();
        participant.setDeletedAt(Instant.now());
        participant.setActive(false);
        participantRepository.save(participant);
    }
}