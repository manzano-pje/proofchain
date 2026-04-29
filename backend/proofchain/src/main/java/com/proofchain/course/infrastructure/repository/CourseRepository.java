package com.proofchain.course.infrastructure.repository;

import com.proofchain.course.domain.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByIdCourseAndInstitutionId(Long id, Long instituition);
    boolean existsByIdCourseAndInstitutionId(Long id, Long institutionId);
}
