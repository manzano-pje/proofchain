package com.proofchain.controller;

import com.proofchain.Dtos.CourseRequestDto;
import com.proofchain.configuration.FormatarTexto;
import com.proofchain.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
//@NoArgsConstructor
@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService coursService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createCourse(@RequestBody CourseRequestDto newCourse){
        coursService.createCourse(newCourse);
        return ResponseEntity.ok().build();
    }
}
