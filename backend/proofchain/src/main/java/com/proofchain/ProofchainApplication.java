package com.proofchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO) // introduzido por copilot
@SpringBootApplication
public class ProofchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProofchainApplication.class, args);
	}

}
