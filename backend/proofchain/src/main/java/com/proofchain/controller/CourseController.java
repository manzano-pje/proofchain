package com.proofchain.controller;

import com.proofchain.Dtos.request.CourseRequestDto;
import com.proofchain.Dtos.response.CourseResponseDto;
import com.proofchain.Dtos.response.FullCourseResponseDto;
import com.proofchain.identities.Course;
import com.proofchain.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
//@NoArgsConstructor
@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService coursService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody CourseRequestDto newCourse){
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
                                            @RequestBody CourseRequestDto courseDto) {
        coursService.updateCourse(name, courseDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CourseResponseDto(
                        courseDto.getName(),
                        courseDto.getDescription(),
                        courseDto.getHours()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping
    public List<FullCourseResponseDto> listAllCourses(){
      return coursService.listAllCourses();
    }

}
