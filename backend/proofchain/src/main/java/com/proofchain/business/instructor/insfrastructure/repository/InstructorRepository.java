package com.proofchain.business.instructor.insfrastructure.repository;

import com.proofchain.business.instructor.domain.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    boolean existsByUserIdAndInstitutionIdAndInstitutionDeletedAtIsNull(Long InstUserId, Long InstitutionId );
    Optional<Instructor> findByUserIdAndInstitutionIdAndInstitutionDeletedAtIsNull(Long InstUserId, Long InstitutionId );
    List<Instructor> findAllByIsActiveIsTrue();
    boolean existsByIdInstructorAndIsActiveIsTrue(Long idInstructor);
}
