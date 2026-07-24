package com.proofchain.business.instructor.interfaces.dto.request;

import com.proofchain.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InstructorRequest {

    private Long id_User;
    private String specialty;
    private Instant hiringDate;

}
