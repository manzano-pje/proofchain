package com.proofchain.instructor.application.command;

import com.proofchain.instructor.interfaces.dto.request.RequestInstructorDto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestInstructorCommand {

    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false)
    private Long idCourse;

    public RequestInstructorCommand(RequestInstructorDto dto){
        this.idUser = dto.getIdUser();
        this.idCourse = dto.getIdCourse();
    }

}

// lista de usuários
// qual o
