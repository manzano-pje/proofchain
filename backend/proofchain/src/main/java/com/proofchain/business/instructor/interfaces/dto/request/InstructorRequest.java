package com.proofchain.business.instructor.interfaces.dto.request;

import com.proofchain.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

public record InstructorRequest(
     Long id_User,
     Long id_Institution,
     Long id_course
){}
