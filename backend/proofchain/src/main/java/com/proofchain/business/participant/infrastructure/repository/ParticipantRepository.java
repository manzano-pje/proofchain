package com.proofchain.business.participant.infrastructure.repository;

import com.proofchain.business.participant.domain.model.Participant;
import com.proofchain.business.participant.interfaces.dto.response.ParticipantResponse;
import com.proofchain.business.participant.interfaces.dto.response.ParticipantSumaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    boolean existsByCpfAndInstitutionIdAndInstitutionDeletedAtIsNull(String cpf, Long institutionId);
    Optional<ParticipantResponse> findByIdAndInstitutionId(Long id, Long institutionId);

    @Query("""
        SELECT new com.proofchain.business.participant.interfaces.dto.response.ParticipantSumaryResponse(
            p.id,
            p.name,
            p.email,
            p.phone,
            p.isActive,
            COALESCE(c.name, '')
        )
        FROM Participant p
        LEFT JOIN p.courseClasses cl
        LEFT JOIN cl.course c
        WHERE p.deletedAt IS NULL
          AND p.institution.id = :institutionId
        """)
    Page<ParticipantSumaryResponse> listParticipantSumary(@Param("institutionId") Long institutionId, Pageable pageable);
}
