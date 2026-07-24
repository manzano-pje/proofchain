package com.proofchain.business.instructor.interfaces.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateInstructor {

    private String specialty;
    private Instant hiringDate;
    private boolean active;
}
