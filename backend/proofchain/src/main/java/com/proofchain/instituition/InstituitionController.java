package com.proofchain.instituition;

import com.proofchain.instituition.dtos.request.InstitutionRequestDto;
import com.proofchain.instituition.dtos.request.NewInstitutionRequestDto;
import com.proofchain.instituition.dtos.response.InstitutionReturn;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/instituition")
@CrossOrigin(origins = "*") // importante para o Vue acessar
public class InstituitionController {

    public final InstituitionService instituitionService;

    @PostMapping
    public ResponseEntity<String> createInstituition(@RequestBody NewInstitutionRequestDto newInstitutionRequestDto){
        instituitionService.createinstitution(newInstitutionRequestDto);
        ResponseEntity retorno = ResponseEntity.status(HttpStatus.CREATED)
                .body("Instituição cadastrada com sucesso.");
        return retorno;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/update/{cnpj}")
    public ResponseEntity<String> updateInstituition(@Valid @PathVariable String cnpj, @RequestBody InstitutionRequestDto institutionRequestDto){
        instituitionService.updateinstitution(cnpj, institutionRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Instituição atualizada com sucesso.");
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping
    public List<InstitutionReturn> getAllInstituition(){
        return instituitionService.getAllinstitution();
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/get/{cnpj}")
    public InstitutionReturn getOneInstituition(@PathVariable String cnpj){
        return instituitionService.getOneinstitution(cnpj);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{cnpj}")
    public ResponseEntity<String> deleteInstituition(@PathVariable String cnpj){
        instituitionService.deleteinstitution(cnpj);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Instituição apagada com sucesso.");
    }
}
