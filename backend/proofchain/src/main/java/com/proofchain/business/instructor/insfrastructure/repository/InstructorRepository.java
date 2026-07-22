package com.proofchain.business.instructor.insfrastructure.repository;

import com.proofchain.business.instructor.domain.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    boolean existsByUserIdAndInstitutionIdAndInstitutionDeletedAtIsNull(Long InstUserId, Long InstitutionId );
}
