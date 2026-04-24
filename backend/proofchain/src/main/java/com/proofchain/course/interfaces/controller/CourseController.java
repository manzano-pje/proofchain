package com.proofchain.course.interfaces.controller;

import com.proofchain.course.CourseService;
import com.proofchain.course.application.command.CreateCourseCommand;
import com.proofchain.course.application.command.UpdateCourseCommand;
import com.proofchain.course.application.handler.CreateCourseHandler;
import com.proofchain.course.application.handler.UpdateCourseHandler;
import com.proofchain.course.domain.model.Course;
import com.proofchain.course.interfaces.dto.request.CourseRequestDto;
import com.proofchain.course.interfaces.dto.response.CourseResponse;
import com.proofchain.course.interfaces.dto.response.FullCourseResponse;
import jakarta.validation.Valid;
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

    private final UpdateCourseHandler updateCourseHandler;

    private final CreateCourseHandler createCourseHandler;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createCourse(@RequestBody CourseRequestDto dto) {
        CreateCourseCommand command = new CreateCourseCommand(dto);
        createCourseHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @RequestBody @Valid CourseRequestDto courseDto) {

        UpdateCourseCommand command = new UpdateCourseCommand(courseDto);

        Course updated = updateCourseHandler.updateCourse(id, command);

        return ResponseEntity.ok(
                new CourseResponse(
                        updated.getName(),
                        updated.getDescription(),
                        updated.getHours()
                )
        );
    }



}
