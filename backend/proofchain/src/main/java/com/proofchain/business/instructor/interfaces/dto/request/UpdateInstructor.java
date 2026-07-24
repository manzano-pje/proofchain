package com.proofchain.business.instructor.interfaces.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateInstructor {

    private String specialty;
    private Instant hiringDate;
    private boolean isActive;
}
