package com.proofchain.business.instructor.interfaces.controller;

import com.proofchain.business.instructor.application.command.CreateInstructorCommand;
import com.proofchain.business.instructor.application.handler.CreateInstructorHandler;
import com.proofchain.business.instructor.interfaces.dto.request.InstructorRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/instructor")
public class InstructorController {

    /*
     * =========================================================
     * DEPENDÊNCIAS (APPLICATION LAYER)
     * =========================================================
     */
      private final CreateInstructorHandler createInstructorHandler;
//    private final ListAllInstructorHandler listAllInstructorHandler;
//    private final ListOneInstructorHandler listOneInstructorHandler;
//    private final UpdateInstructorHandler updateInstructoHandler;
//    private final DeleteInstructorHandler deleteInstructorHandler;

    /*
     * =========================================================
     * ENDPOINT: CREATE INSTRUCTOR
     * =========================================================
     */

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    public ResponseEntity<Void> createInstructor(@Valid @RequestBody InstructorRequest dto){
        CreateInstructorCommand command = new CreateInstructorCommand(dto);
        createInstructorHandler.createInstructor(command);
        return ResponseEntity.ok().build();
    }

}
