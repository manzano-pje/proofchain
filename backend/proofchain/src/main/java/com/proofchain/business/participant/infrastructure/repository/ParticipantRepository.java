package com.proofchain.business.participant.infrastructure.repository;

import com.proofchain.business.participant.domain.model.Participant;
import com.proofchain.business.participant.interfaces.dto.response.ParticipantSumaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    boolean existsByCpfAndInstitutionIdAndInstitutionDeletedAtIsNull(String cpf, Long institutionId);

    @Query("SELECT p.name, p.email, p.phone, p.isActive, cc.course.name FROM Participant p " +
            "JOIN p.courseClasses cc WHERE p.deletedAt IS NULL AND p.institution.id = :institutionId")
    Page<ParticipantSumaryResponse> listParticipantSumary(@Param("institutionId") Long institutionId,  Pageable pageable);
}
