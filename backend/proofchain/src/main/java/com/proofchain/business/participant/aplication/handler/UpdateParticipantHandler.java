package com.proofchain.business.participant.aplication.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.participant.aplication.command.UpdateParticipantCommand;
import com.proofchain.business.participant.domain.model.Participant;
import com.proofchain.business.participant.infrastructure.repository.ParticipantRepository;
import com.proofchain.business.participant.interfaces.dto.response.ParticipantResponse;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UpdateParticipantHandler {

    private final ParticipantRepository participantRepository;
    private final InstitutionRepository institutionRepository;
    private final TenantValidation tenantValidation;

    public void updateParticipant(Long id, UpdateParticipantCommand command) {

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

        boolean participantExists = participantRepository.existsByCpfAndInstitutionIdAndInstitutionDeletedAtIsNull(command.getCpf(), institutionId);
        if (!participantExists) {
            throw new AlreadyExistsException("Participande não está cadastrado");
        }

        Optional<Participant> participantOptional = participantRepository.findByIdAndInstitutionId(id, institutionId);
        if (participantOptional.isEmpty()) {
            throw new NotFoundException("Participante não encontrado");
        }

        Participant participant = participantOptional.get();
        participant.setId(id);
        participantRepository.save(participant);
    }
}

