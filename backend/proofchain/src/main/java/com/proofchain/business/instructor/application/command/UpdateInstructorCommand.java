package com.proofchain.business.instructor.application.command;

import com.proofchain.business.instructor.interfaces.dto.request.UpdateInstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateInstructorCommand {

    private String specialty;
    private Instant hiringDate;
    private boolean isActive;

    public UpdateInstructorCommand(UpdateInstructor dto) {
        this.specialty = dto.getSpecialty();
        this.hiringDate = dto.getHiringDate();
        this.isActive = dto.isActive();
    }
}
