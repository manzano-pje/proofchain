package com.proofchain.business.participant.aplication.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.participant.aplication.command.CreateParticipantCommand;
import com.proofchain.business.participant.domain.model.Participant;
import com.proofchain.business.participant.infrastructure.repository.ParticipantRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.exception.messages.ParticipantMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CreateParticipantHandler {

    private final ParticipantRepository participantRepository;
    private final InstitutionRepository institutionRepository;
    private final TenantValidation tenantValidation;

    public void createParticipant(CreateParticipantCommand command) {
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
        if(participantExists){
            throw new AlreadyExistsException(ParticipantMessages.PARTICIPANT_ALREAY_EXISTS);
        }

        Participant participant = new Participant();
        participant.setName(command.getName());
        participant.setCpf(command.getCpf());
        participant.setEmail(command.getEmail());
        participant.setPhone(command.getPhone());
        participant.setAddress(command.getAddress());
        participant.setNumber(command.getNumber());
        participant.setComplement(command.getComplement());
        participant.setNeighborhood(command.getNeighborhood());
        participant.setCity(command.getCity());
        participant.setState(command.getState());
        participant.setPostalCode(command.getPostalCode());
        participant.setCreatedAt(Instant.now());
        participant.setActive(command.isActive());
        participant.setInstitution(institution);

        participantRepository.save(participant);
    }
}
