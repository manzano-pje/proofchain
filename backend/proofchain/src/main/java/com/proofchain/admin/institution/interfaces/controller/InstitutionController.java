package com.proofchain.admin.institution.interfaces.controller;

import com.proofchain.admin.institution.application.handler.*;
import com.proofchain.admin.institution.application.query.ListAllInstitutionHandler;
import com.proofchain.admin.institution.application.query.ListOneInstitutionHandler;
import com.proofchain.institution.application.handler.*;
import com.proofchain.admin.institution.interfaces.dtos.request.UpdateInstitutionRequest;
import com.proofchain.admin.institution.interfaces.dtos.request.CreateInstitutionRequestDto;
import com.proofchain.admin.institution.interfaces.dtos.response.InstitutionResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/institution")
@CrossOrigin(origins = "*") // importante para o Vue acessar
public class InstitutionController {


    // TODO :Gerar documentação
    // SUPER_ADMIN
    // Criar métodos listar todas as instituições
    // Excluir instituição


    private final CreateInstitutionHandler createInstitution;
    private final DeleteInstitutionHandler deleteInstitution;
    private ListAllInstitutionHandler listAllInstitution;
    private ListOneInstitutionHandler listOneInstitution;
    private UpdateInstitutionHandler updateInstitution;

    @PostMapping
    public ResponseEntity<String> createInstitution(@RequestBody CreateInstitutionRequestDto createInstitutionRequestDto){
        createInstitution.createinstitution(createInstitutionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Instituição cadastrada com sucesso.");
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{cnpj}")
    public ResponseEntity<String> updateInstitution(@Valid @PathVariable String cnpj,
                                                    @Valid @RequestBody UpdateInstitutionRequest UpdateInstitutionRequest){
        updateInstitution.updateinstitution(cnpj, UpdateInstitutionRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Instituição atualizada com sucesso.");
    }

//    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public List<InstitutionResponse> getAllInstitution(){

        return listAllInstitution.getAllinstitution();
    }

//    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/get/{cnpj}")
    public InstitutionResponse getOneInstitution(@PathVariable String cnpj){
        return listOneInstitution.getOneinstitution(cnpj);
    }

//    @PreAuthorize("hasRole('SUPER_ADMIN', 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{cnpj}")
    public ResponseEntity<String> deleteInstitution(@PathVariable String cnpj){
        deleteInstitution.deleteinstitution(cnpj);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Instituição apagada com sucesso.");
    }
}
