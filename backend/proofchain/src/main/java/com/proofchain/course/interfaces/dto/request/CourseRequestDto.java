package com.proofchain.course.interfaces.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseRequestDto {

    @Size(max = 100)
    private String name;
    @Size(max = 200)
    private String description;
    private int hours;
}
