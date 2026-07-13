package com.proofchain.instructor.interfaces.dto;

import com.proofchain.course.domain.model.Course;
import com.proofchain.user.domain.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorRequest {


    @Column(nullable = false)

    private Long idUser; 
    private Long idCourse;
}

// lista de usuários
// qual o curso