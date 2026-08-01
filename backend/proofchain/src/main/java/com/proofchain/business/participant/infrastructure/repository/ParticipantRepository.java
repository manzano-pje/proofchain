package com.proofchain.business.participant.infrastructure.repository;

import com.proofchain.business.participant.domain.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    boolean existsByCpfAndInstitutionIdAndInstitutionDeletedAtIsNull(String cpf, Long institutionId);
}
