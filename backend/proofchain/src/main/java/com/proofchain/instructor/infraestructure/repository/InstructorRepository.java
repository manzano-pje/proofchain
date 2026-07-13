package com.proofchain.instructor.infraestructure.repository;

import com.proofchain.instructor.domain.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
