package com.bob.controller;

import com.bob.service.DocumentationAgent;
import com.bob.service.FileReaderService;
import com.bob.service.OutputWriterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/documentation")
public class DocumentationController {

    private static final Logger log = Logger.getLogger(DocumentationController.class.getName());
    private final FileReaderService fileReaderService;
    private final DocumentationAgent documentationAgent;
    private final OutputWriterService outputWriterService;

    public DocumentationController(FileReaderService fileReaderService,
                                   DocumentationAgent documentationAgent,
                                   OutputWriterService outputWriterService) {
        this.fileReaderService = fileReaderService;
        this.documentationAgent = documentationAgent;
        this.outputWriterService = outputWriterService;
    }

    @PostMapping("/java")
    public String documentDirectory(@RequestParam("path") String directory) throws IOException {
        log.info("Iniciando processamento completo do projeto: " + directory);

        List<Path> files = fileReaderService.listJavaFilesRecursively(directory);
        if (files.isEmpty()) {
            return "Nenhum arquivo .java encontrado.";
        }

        // 1. Processa cada arquivo individualmente para INJETAR o Javadoc no próprio .java
        for (Path file : files) {
            String code = fileReaderService.readFile(file);
            documentationAgent.documentFileAndInjectJavadoc(directory, file, code);
        }

        // 2. Lê ou Inicializa os Relatórios Centrais (SYSTEM_ARCHITECTURE.md e README.md)
        String currentArchDoc = outputWriterService.readExistingReport(directory, "SYSTEM_ARCHITECTURE.md");
        String currentReadmeDoc = outputWriterService.readExistingReport(directory, "README.md");

        // 3. Pede para a IA gerar a Tabela de Arquitetura Consolidada e salva na pasta /docs
        log.info("Gerando Tabela de Arquitetura Consolidada...");
        String updatedArchDoc = documentationAgent.generateArchitectureTable(currentArchDoc, files, fileReaderService);

        // CORREÇÃO AQUI: Salvando explicitamente dentro da pasta docs/
        outputWriterService.saveGlobalReport(directory + "/docs", "SYSTEM_ARCHITECTURE.md", updatedArchDoc);

        // 4. Pede para a IA gerar o README.md matador e salva na raiz para o GitHub
        log.info("Gerando ou Atualizando o README.md do GitHub...");
        String updatedReadmeDoc = documentationAgent.generateReadmePortfolio(currentReadmeDoc, updatedArchDoc);

        // Mantido na raiz pura do projeto
        outputWriterService.saveGlobalReport(directory, "README.md", updatedReadmeDoc);

        log.info("Automação finalizada com sucesso absoluto!");
        return "Sucesso! Javadocs injetados, SYSTEM_ARCHITECTURE.md salvo na pasta /docs e README.md gerado na raiz.";
    }
}
