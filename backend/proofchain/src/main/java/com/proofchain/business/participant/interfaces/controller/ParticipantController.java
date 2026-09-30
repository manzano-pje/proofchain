package com.proofchain.business.participant.interfaces.controller;

import com.proofchain.business.participant.aplication.command.CreateParticipantCommand;
import com.proofchain.business.participant.aplication.handler.CreateParticipantHandler;
import com.proofchain.business.participant.aplication.query.ListAllSumaryParticipantQuery;
import com.proofchain.business.participant.interfaces.dto.request.ParticipantRequest;
import com.proofchain.business.participant.interfaces.dto.response.ParticipantSumaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final ListAllSumaryParticipantQuery listAllSumaryParticipantQuery;

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
    @GetMapping("/list")
    public Page<ParticipantSumaryResponse> listAll(){
        return listAllSumaryParticipantQuery.listAll();

    }
}
