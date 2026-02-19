package com.proofchain.Dtos.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDto {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @Size(max = 200)
    private String description;
    private int hours;
}
