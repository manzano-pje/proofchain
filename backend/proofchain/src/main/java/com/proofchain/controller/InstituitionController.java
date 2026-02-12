package com.proofchain.controller;

import com.proofchain.Dtos.InstituitionRequestDto;
import com.proofchain.Dtos.InstituitionReturnDto;
import com.proofchain.Dtos.NewInstituitionRequestDto;
import com.proofchain.service.InstituitionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/instituition")
public class InstituitionController {

    public final InstituitionService instituitionService;

    @PostMapping
    public ResponseEntity<Void> createInstituition(  @RequestBody NewInstituitionRequestDto newInstituitionRequestDto){
        instituitionService.createInstituition(newInstituitionRequestDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/update/{cnpj}")
    public ResponseEntity<Void> updateInstituition(@PathVariable String cnpj, @RequestBody InstituitionRequestDto instituitionRequestDto){
        instituitionService.updateInstituition(cnpj, instituitionRequestDto);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<Void> deleteInstituition(@PathVariable String cnpj){
        instituitionService.deleteInstituition(cnpj);
        return ResponseEntity.ok().build();

    }

}
