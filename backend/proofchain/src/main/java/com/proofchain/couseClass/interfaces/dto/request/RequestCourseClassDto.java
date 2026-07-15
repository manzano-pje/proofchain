package com.proofchain.couseClass.interfaces.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCourseClassDto {

    @Column(nullable = false)
    Long idUser;

    @Column(nullable = false)
    Long idCourse;
}

// lista de usuários
// qual o curso