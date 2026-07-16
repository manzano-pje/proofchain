package com.proofchain.business.couseClass.application.command;

import com.proofchain.business.couseClass.interfaces.dto.request.RequestCourseClassDto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCourseClassCommand {

    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false)
    private Long idCourse;

    public RequestCourseClassCommand(RequestCourseClassDto dto){
        this.idUser = dto.getIdUser();
        this.idCourse = dto.getIdCourse();
    }

}

// lista de usuários
// qual o
