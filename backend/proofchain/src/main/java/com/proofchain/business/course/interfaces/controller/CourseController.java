package com.proofchain.business.course.interfaces.controller;

import com.proofchain.business.course.application.command.CreateCourseCommand;
import com.proofchain.business.course.application.command.UpdateCourseCommand;
import com.proofchain.business.course.application.handler.CreateCourseHandler;
import com.proofchain.business.course.application.handler.ListAllCourseHandler;
import com.proofchain.business.course.application.handler.ListOneCourseHandler;
import com.proofchain.business.course.application.handler.UpdateCourseHandler;
import com.proofchain.business.course.domain.model.Course;
import com.proofchain.business.course.interfaces.dto.request.CourseRequestDto;
import com.proofchain.business.course.interfaces.dto.response.CourseResponse;
import com.proofchain.business.course.interfaces.dto.response.FullCourseResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CourseController
 *
 * Função no sistema:
 * Responsável por expor endpoints HTTP relacionados ao domínio de cursos.
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
 * Ponto de entrada do módulo de cursos dentro da arquitetura ProofChain.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    /*
     * =========================================================
     * DEPENDÊNCIAS (APPLICATION LAYER)
     * =========================================================
     */
    private final UpdateCourseHandler updateCourseHandler;
    private final CreateCourseHandler createCourseHandler;
    private final ListAllCourseHandler listAllCourses;
    private final ListOneCourseHandler listOneCourse;

    /*
     * =========================================================
     * ENDPOINT: CREATE COURSE
     * =========================================================
     */

    @PreAuthorize("hasRole('SUPER_ADMIN','ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<Void> createCourse(@Valid @RequestBody CourseRequestDto dto) {

        CreateCourseCommand command = new CreateCourseCommand(dto);
        createCourseHandler.handle(command);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
     * =========================================================
     * ENDPOINT: LIST ALL COURSES
     * =========================================================
     */

    @PreAuthorize("hasRole('ADMIN', 'USER')")
    @GetMapping
    public List<FullCourseResponse> listAllCourses() {
        return listAllCourses.listAllCourses();
    }

    /*
     * =========================================================
     * ENDPOINT: LIST ONE COURSE
     * =========================================================
     */

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public CourseResponse listOneCourse(@PathVariable Long id) {
        return listOneCourse.listOneCourse(id);
    }

    /*
     * =========================================================
     * ENDPOINT: UPDATE COURSE
     * =========================================================
     */

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