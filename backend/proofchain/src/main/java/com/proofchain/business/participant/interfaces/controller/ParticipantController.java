package com.proofchain.business.participant.interfaces.controller;

import com.proofchain.business.participant.aplication.command.CreateParticipantCommand;
import com.proofchain.business.participant.aplication.command.UpdateParticipantCommand;
import com.proofchain.business.participant.aplication.handler.CreateParticipantHandler;
import com.proofchain.business.participant.aplication.handler.DeleteParticipantHandler;
import com.proofchain.business.participant.aplication.handler.UpdateParticipantHandler;
import com.proofchain.business.participant.aplication.query.ListAllSumaryParticipantQuery;
import com.proofchain.business.participant.aplication.query.ListOneParticipantQuery;
import com.proofchain.business.participant.interfaces.dto.request.ParticipantRequest;
import com.proofchain.business.participant.interfaces.dto.request.ParticipantUpdate;
import com.proofchain.business.participant.interfaces.dto.response.ParticipantResponse;
import com.proofchain.business.participant.interfaces.dto.response.ParticipantSumaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/participants")
@RequiredArgsConstructor
public class ParticipantController {

    /*
     * =========================================================
     * DEPENDÊNCIAS (APPLICATION LAYER)
     * =========================================================
     */
    private final CreateParticipantHandler createParticipantHandler;
    private final ListOneParticipantQuery listOneParticipantQuery;
    private final ListAllSumaryParticipantQuery listAllSumaryParticipantQuery;
    private final UpdateParticipantHandler updateParticipantHandler;
    private final DeleteParticipantHandler deleteParticipantHandler;

    /*
     * =========================================================
     * ENDPOINT: CREATE PARTICIPANT
     * =========================================================
     */
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @PostMapping("/register")
    public ResponseEntity<Void> createParticipant (@Valid @RequestBody ParticipantRequest dto){
        CreateParticipantCommand command = new CreateParticipantCommand(dto);
        createParticipantHandler.createParticipant(command);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @GetMapping("/listSumary")
    public ResponseEntity<Page<ParticipantSumaryResponse>> ListAllSumaryParticipant(){
        Page<ParticipantSumaryResponse> response = listAllSumaryParticipantQuery.listAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @GetMapping("/listOne/{id}")
    public ResponseEntity<ParticipantResponse> listOneParticipant(@PathVariable Long id){
        ParticipantResponse response = listOneParticipantQuery.listOneParticipant(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<ParticipantResponse> updateParticipant(@PathVariable Long id,
                                                                 @Valid @RequestBody ParticipantUpdate dto){
        UpdateParticipantCommand command = new UpdateParticipantCommand(id, dto);
        updateParticipantHandler.updateParticipant(id, command);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id){
        deleteParticipantHandler.deleteParticipant(id);
        return ResponseEntity.ok().build();
    }

}