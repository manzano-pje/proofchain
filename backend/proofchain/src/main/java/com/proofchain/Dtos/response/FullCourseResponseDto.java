package com.proofchain.Dtos.response;

import com.proofchain.identities.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FullCourseResponseDto {

    private String name;
    private String description;
    private int hours;
    private Instant createdAt;
    private Instant updatedAt;

    public FullCourseResponseDto(Course course) {
    }
}
