package com.proofchain.business.participant.interfaces.controller;

import com.proofchain.business.participant.aplication.command.CreateParticipantCommand;
import com.proofchain.business.participant.aplication.handler.CreateParticipantHandler;
import com.proofchain.business.participant.interfaces.dto.request.ParticipantRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /*
     * =========================================================
     * ENDPOINT: CREATE PARTICIPANT
     * =========================================================
     */
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    @PostMapping("/register")
    public ResponseEntity<Void> createParticipant (@Valid @RequestBody ParticipantRequest dto){
        CreateParticipantCommand command = new CreateParticipantCommand(dto);
        createParticipantHandler.createParticipant(command);
        return ResponseEntity.ok().build();
    }
}
