package com.proofchain.instructor.interfaces.controller;

import com.proofchain.instructor.application.command.RequestInstructorCommand;
import com.proofchain.instructor.application.handler.*;
import com.proofchain.instructor.interfaces.dto.RequestInstructorDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * InstructorController
 *
 * Função no sistema:
 * Responsável por expor endpoints HTTP relacionados ao domínio de instrutores.
 * Atua como camada de entrada da aplicação, recebendo requisições externas e delegando
 * toda lógica de negócio para a camada de aplicação (handlers e commands).
 *
 * Estrutura atual:
 * Controller REST baseado em Spring Boot.
 * Expõe operações CRUD de cursos com suporte a segurança baseada em roles.
 * Integra-se com camada de aplicação através de Commands e Handlers.
 *
 * Fluxo:
 * 1. Recebe requisição HTTP do cliente
 * 2. Valida entrada via Bean Validation (@Valid)
 * 3. Converte DTO em Command (quando necessário)
 * 4. Encaminha para Handler correspondente
 * 5. Retorna resposta HTTP ao cliente
 *
 * Integração no sistema:
 * Ponto de entrada do módulo de instrutores dentro da arquitetura ProofChain.
 */

 @RestController
@AllArgsConstructor

@RequestMapping("/api/v1/instructor")
public class InstructorController {

    /*
     * =========================================================
     * DEPENDÊNCIAS (APPLICATION LAYER)
     * =========================================================
     */
    private final CreateInstructorHandler create;
    private final ListAllInstructorHandler listAll;
    private final ListOneInstructorHandler listOne;
    private final UpdateInstructorHandler update;
    private final DeleteInstructorHandler delete;

    /*
     * =========================================================
     * ENDPOINT: CREATE INSTRUCTOR
     * =========================================================
     */
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<Void> createInstructor(@Valid @RequestBody RequestInstructorDto dto){
        RequestInstructorCommand command = new RequestInstructorCommand(dto);
        create.create(command);
        return ResponseEntity.ok().build();
    }

    /*
     * =========================================================
     * ENDPOINT: LISTALL INSTRUCTOR
     * =========================================================
     */

    /*
     * =========================================================
     * ENDPOINT: LISTONE INSTRUCTOR
     * =========================================================
     */

    /*
     * =========================================================
     * ENDPOINT: UPDATE INSTRUCTOR
     * =========================================================
     */

    /*
     * =========================================================
     * ENDPOINT: DELETE INSTRUCTOR
     * =========================================================
     */

}
