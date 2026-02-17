package com.proofchain.controller;

import com.proofchain.Dtos.request.InstituitionRequestDto;
import com.proofchain.Dtos.response.ApiResponse;
import com.proofchain.Dtos.response.InstituitionReturnDto;
import com.proofchain.Dtos.request.NewInstituitionRequestDto;
import com.proofchain.service.InstituitionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/instituition")
@CrossOrigin(origins = "*") // importante para o Vue acessar
public class InstituitionController {

    public final InstituitionService instituitionService;

    @PostMapping
    public ResponseEntity<ApiResponse> createInstituition(@RequestBody NewInstituitionRequestDto newInstituitionRequestDto){
        instituitionService.createInstituition(newInstituitionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(
                        true,
                       "Instituição cadastrada com sucesso.",
                        null));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/update/{cnpj}")
    public ResponseEntity<ApiResponse> updateInstituition(@PathVariable String cnpj, @RequestBody InstituitionRequestDto instituitionRequestDto){

        instituitionService.updateInstituition(cnpj, instituitionRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(
                        true,
                        "Instituição alterada com sucesso.",
                        null));
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping
    public List<InstituitionReturnDto> getAllInstituition(){
        return instituitionService.getAllInstituition();
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/get/{cnpj}")
    public InstituitionReturnDto getOneInstituition(@PathVariable String cnpj){
        return instituitionService.getOneInstituition(cnpj);
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    @DeleteMapping("/delete/{cnpj}")
    public ResponseEntity<ApiResponse> deleteInstituition(@PathVariable String cnpj){
        instituitionService.deleteInstituition(cnpj);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(
                        true,
                        "Instituição excluída com sucesso.",
                        null));

    }

}
