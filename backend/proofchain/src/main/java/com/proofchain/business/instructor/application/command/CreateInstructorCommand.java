package com.proofchain.business.instructor.application.command;

import com.proofchain.business.instructor.interfaces.dto.request.InstructorRequest;
import com.proofchain.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateInstructorCommand {

    private Long id_User;
    private Long id_Institution;
    private Long id_course;

    public CreateInstructorCommand(InstructorRequest dto){
        this.id_User = dto.id_User();
        this.id_Institution = dto.id_Institution();
        this.id_course = dto.id_course();
    }
}
