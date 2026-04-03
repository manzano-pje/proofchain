package com.proofchain.controller;

import com.proofchain.Dtos.request.CourseRequestDto;
import com.proofchain.Dtos.response.CourseResponse;
import com.proofchain.Dtos.response.FullCourseResponse;
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
@RequestMapping("/api/v1/course")
public class CourseController {

    private final CourseService coursService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequestDto newCourse){
        coursService.createCourse(newCourse);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CourseResponse(
                        newCourse.getName(),
                        newCourse.getDescription(),
                        newCourse.getHours()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping
    public List<FullCourseResponse> listAllCourses(){
        return coursService.listAllCourses();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{name}")
    public CourseResponse listOneCourse(@PathVariable String name){
        return coursService.listOneCourse(name);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
    @PatchMapping("/update/{name}")
    public ResponseEntity<CourseResponse>updateCourse(@PathVariable String name,
                                                      @RequestBody CourseRequestDto courseDto) {
        coursService.updateCourse(name, courseDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CourseResponse(
                        courseDto.getName(),
                        courseDto.getDescription(),
                        courseDto.getHours()));
    }



}
