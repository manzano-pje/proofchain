package com.proofchain.business.participant.aplication.query;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.participant.infrastructure.repository.ParticipantRepository;
import com.proofchain.business.participant.interfaces.dto.response.ParticipantSumaryResponse;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListAllSumaryParticipantQuery {

    private final ParticipantRepository participantRepository;
    private final InstitutionRepository institutionRepository;
    private final TenantValidation tenantValidation;

    public Page<ParticipantSumaryResponse> listAll(){
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

        Pageable pageable = PageRequest.of(
                0,               // página
                10,                         // registros por página
                Sort.by("name").ascending() // ordenação
        );

        Page<ParticipantSumaryResponse> result = participantRepository.listParticipantSumary(institutionId, pageable );
        if(result.isEmpty()){
            throw new NotFoundException("Não existem alunos cadastrados.");
        }
        return result;


    }

}
