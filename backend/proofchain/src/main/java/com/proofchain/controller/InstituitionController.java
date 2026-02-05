package com.proofchain.controller;

import com.proofchain.Dtos.NewInstituitionRequestDto;
import com.proofchain.service.InstituitionService;
import com.proofchain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/instituition")
public class InstituitionController {

    public final InstituitionService instituitionService;

    @PostMapping
    public ResponseEntity<Void> createInstituition(@RequestBody NewInstituitionRequestDto newInstituitionRequestDto){
        instituitionService.createInstituition(newInstituitionRequestDto);
        return ResponseEntity.ok().build();
    }
}
