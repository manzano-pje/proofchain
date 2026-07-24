package com.proofchain.business.instructor.interfaces.controller;

import com.proofchain.business.instructor.application.command.CreateInstructorCommand;
import com.proofchain.business.instructor.application.command.UpdateInstructorCommand;
import com.proofchain.business.instructor.application.handler.CreateInstructorHandler;
import com.proofchain.business.instructor.application.handler.DeleteInstructorHandler;
import com.proofchain.business.instructor.application.handler.UpdateInstructorHandler;
import com.proofchain.business.instructor.application.query.GetOneInstructorHandler;
import com.proofchain.business.instructor.application.query.ListAllInstructorHandler;
import com.proofchain.business.instructor.interfaces.dto.request.InstructorRequest;
import com.proofchain.business.instructor.interfaces.dto.request.UpdateInstructor;
import com.proofchain.business.instructor.interfaces.dto.response.InstructorResponse;
import com.proofchain.business.instructor.interfaces.dto.response.InstructorsSumaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
      private final ListAllInstructorHandler listAllInstructorHandler;
      private final GetOneInstructorHandler getOneInstructorHandler;
      private final UpdateInstructorHandler updateInstructoHandler;
      private final DeleteInstructorHandler deleteInstructorHandler;

    /*
     * =========================================================
     * ENDPOINT: CREATE INSTRUCTOR
     * =========================================================
     */

    @PostMapping("/register")
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    public ResponseEntity<Void> createInstructor(@Valid @RequestBody InstructorRequest dto){
        CreateInstructorCommand command = new CreateInstructorCommand(dto);
        createInstructorHandler.createInstructor(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    public List<InstructorsSumaryResponse> listAllInstructors(){
        return listAllInstructorHandler.listAllInstructors();
    }

    @GetMapping("/list/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    public InstructorResponse getOneInstructor(@PathVariable Long id){
        return getOneInstructorHandler.getOneInstructor(id);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    public ResponseEntity<Void> updateInstructor(@Valid @PathVariable Long id, @RequestBody UpdateInstructor dto){
        UpdateInstructorCommand command = new UpdateInstructorCommand(dto);
        updateInstructoHandler.updateInstructor(id, command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        deleteInstructorHandler.deleteInstructor(id);
        return ResponseEntity.ok().build();
    }

}
