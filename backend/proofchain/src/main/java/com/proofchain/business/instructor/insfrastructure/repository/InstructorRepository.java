package com.proofchain.business.instructor.insfrastructure.repository;

import com.proofchain.business.instructor.domain.model.Instructor;
import com.proofchain.business.instructor.interfaces.dto.response.InstructorsSumaryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    boolean existsByUserIdAndInstitutionIdAndInstitutionDeletedAtIsNull(Long InstUserId, Long InstitutionId );
    List<Instructor> findAllByIsActiveIsTrue();
}
