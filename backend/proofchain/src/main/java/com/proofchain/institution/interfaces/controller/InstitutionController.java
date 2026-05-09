package com.proofchain.institution.interfaces.controller;

import com.proofchain.institution.InstitutionService;
import com.proofchain.institution.application.handler.*;
import com.proofchain.institution.interfaces.dtos.request.InstitutionRequest;
import com.proofchain.institution.interfaces.dtos.request.NewInstitutionRequestDto;
import com.proofchain.institution.interfaces.dtos.response.InstitutionReturn;
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

    public final InstitutionService institutionService;
    private final CreateInstitutionHandler createInstitution;
    private final DeleteInstitutionHandler deleteInstitution;
    private ListAllInstitutionHandler listAllInstitution;
    private ListOneInstitutionHandler listOneInstitution;
    private UpdateInstitutionHandler updateInstitution;

    @PostMapping
    public ResponseEntity<String> createInstitution(@RequestBody NewInstitutionRequestDto newInstitutionRequestDto){
        createInstitution.createinstitution(newInstitutionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Instituição cadastrada com sucesso.");
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{cnpj}")
    public ResponseEntity<String> updateInstitution(@Valid @PathVariable String cnpj,
                                                    @Valid @RequestBody InstitutionRequest institutionRequest){
        institutionService.updateinstitution(cnpj, institutionRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Instituição atualizada com sucesso.");
    }

//    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public List<InstitutionReturn> getAllInstitution(){

        return listAllInstitution.getAllinstitution();
    }

//    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/get/{cnpj}")
    public InstitutionReturn getOneInstitution(@PathVariable String cnpj){
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
