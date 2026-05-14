package com.proofchain.course.infrastructure.repository;

import com.proofchain.course.domain.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByIdAndInstitutionId(Long id, Long instituition);
    boolean existsByIdAndInstitutionId(Long id, Long institutionId);
}
