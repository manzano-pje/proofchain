package com.proofchain.couseClass.interfaces.controller;

import com.proofchain.couseClass.application.command.RequestCourseClassCommand;
import com.proofchain.couseClass.application.handler.*;
import com.proofchain.couseClass.interfaces.dto.request.RequestCourseClassDto;
import com.proofchain.couseClass.interfaces.dto.response.CourseClassReturn;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  CourseClass
 *
 * Função no sistema:
 * Responsável por expor endpoints HTTP relacionados ao domínio de instrutores.
 * Atua como camada de entrada da aplicação, recebendo requisições externas e delegando
 * toda lógica de negócio para a camada de aplicação (handlers e commands).
 *
 * Estrutura atual:
 * Controller REST baseado em Spring Boot.
 * Expõe operações CRUD de turmas com suporte a segurança baseada em roles.
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
 * Ponto de entrada do módulo de turmas dentro da arquitetura ProofChain.
 */

 @RestController
@AllArgsConstructor

@RequestMapping("/api/v1/couseClass")
public class CourseClassController {

    /*
     * =========================================================
     * DEPENDÊNCIAS (APPLICATION LAYER)
     * =========================================================
     */
    private final CreateCourseClassHandler create;
    private final ListAllCourseClassHandler listAll;
//    private final ListOneCourseClassHandler listOne;
//    private final UpdateCourseClassHandler update;
//    private final DeleteCourseClassHandler delete;

    /*
     * =========================================================
     * ENDPOINT: CREATE COURSECLASS
     * =========================================================
     */
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<Void> createInstructor(@Valid @RequestBody RequestCourseClassDto dto){
        RequestCourseClassCommand command = new RequestCourseClassCommand(dto);
        create.create(command);
        return ResponseEntity.ok().build();
    }

    /*
     * =========================================================
     * ENDPOINT: LISTALL COURSECLASS
     * =========================================================
     */
    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    @GetMapping("/list")
    public List<CourseClassReturn> listAllcourseClass(){
        return listAll.listAllcourseClass();
    }


    /*
     * =========================================================
     * ENDPOINT: LISTONE COURSECLASS
     * =========================================================
     */

    /*
     * =========================================================
     * ENDPOINT: UPDATE COURSECLASS
     * =========================================================
     */

    /*
     * =========================================================
     * ENDPOINT: DELETE COURSECLASS
     * =========================================================
     */

}
