package com.proofchain.controller;

import com.proofchain.Dtos.request.CourseDto;
import com.proofchain.Dtos.response.ApiResponse;
import com.proofchain.Dtos.response.CourseResponseDto;
import com.proofchain.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
//@NoArgsConstructor
@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService coursService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody CourseDto newCourse){
        coursService.createCourse(newCourse);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CourseResponseDto(
                        newCourse.getName(),
                        newCourse.getDescription(),
                        newCourse.getHours()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
    @PatchMapping("/update/{name}")
    public ResponseEntity<CourseResponseDto>updateCourse(@PathVariable String name,
                                            @RequestBody CourseDto courseDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CourseResponseDto(
                        courseDto.getName(),
                        courseDto.getDescription(),
                        courseDto.getHours()));
    }
}
