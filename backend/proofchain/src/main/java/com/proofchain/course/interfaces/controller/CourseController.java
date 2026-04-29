package com.proofchain.course.interfaces.controller;

import com.proofchain.course.application.command.CreateCourseCommand;
import com.proofchain.course.application.command.UpdateCourseCommand;
import com.proofchain.course.application.handler.*;
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
@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    private final UpdateCourseHandler updateCourseHandler;
    private final CreateCourseHandler createCourseHandler;
    private final ListAllCourseHandler listAllCourses;
    private final ListOneCourseHandler listOneCourse;
    private final DeleteCourseHandler deleteCourse;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createCourse(@Valid @RequestBody CourseRequestDto dto) {
        CreateCourseCommand command = new CreateCourseCommand(dto);
        createCourseHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping
    public List<FullCourseResponse> listAllCourses(){
        return listAllCourses.listAllCourses();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public CourseResponse listOneCourse(@PathVariable Long id){
        return listOneCourse.listOneCourse(id);
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
