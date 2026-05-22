package com.bob.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

@Service
public class DocumentationAgent {

    private final ChatModel chatModel;
    private final OutputWriterService outputWriterService;
    private static final Logger log = Logger.getLogger(DocumentationAgent.class.getName());

    public DocumentationAgent(ChatModel chatModel, OutputWriterService outputWriterService) {
        this.chatModel = chatModel;
        this.outputWriterService = outputWriterService;
    }

    // PROMPT 1: Gera estritamente as tags de Javadoc para injeção física no .java
    public void documentFileAndInjectJavadoc(String projectRootPath, Path file, String code) {
        String fileName = file.getFileName().toString();
        log.info("Injetando Javadoc de forma cirúrgica em: " + fileName);

        String prompt = """
            Atue como um Arquiteto de Software especialista em Java.
            Sua única tarefa é gerar a documentação Javadoc para a classe fornecida.
            
            🛑 REGRAS CRÍTICAS:
            - NÃO retorne o código-fonte original.
            - Retorne APENAS os blocos de Javadoc estruturados exatamente no formato do exemplo abaixo.
            - Não inclua nenhuma outra palavra, explicação ou introdução fora das tags.

            Formato esperado de retorno:
            [JAVADOC_CLASSE]
            /**
             * Descrição concisa da responsabilidade da classe.
             * @author Agente IA Bob
             * @since 2026
             */

            Código para processamento do arquivo [%s]:
            ```java
            %s
            ```
            """.formatted(fileName, code);

        String output = chatModel.call(new Prompt(prompt)).getResult().getOutput().getText();
        outputWriterService.saveJavadocIntoFile(projectRootPath, fileName, output);
    }

    // PROMPT 2: Gera e alimenta incrementalmente a tabela de Onboarding sem códigos
    public String generateArchitectureTable(String currentDoc, List<Path> files, FileReaderService reader) {
        StringBuilder todosCodigos = new StringBuilder();
        files.forEach(f -> {
            try { todosCodigos.append("\n// Classe: ").append(f.getFileName()).append("\n").append(reader.readFile(f)); } catch(Exception e){}
        });

        String prompt = """
            Atue como um Arquiteto de Software especialista em Documentação Técnica e Onboarding.
            Alimente ou construa um arquivo chamado SYSTEM_ARCHITECTURE.md no formato de tabela corporativa.
            
            🛑 REGRAS CRÍTICAS DE ESCOPO:
            - PROIBIDO incluir qualquer linha de código-fonte Java ou blocos de código markdown.
            - Apresente apenas informações conceituais estruturadas na tabela.
            
            DOCUMENTAÇÃO ATUAL (Mantenha as classes que já existem aqui):
            \"\"\"
            %s
            \"\"\"

            CLASSES DO PROJETO PARA ADICIONAR/ATUALIZAR NA TABELA:
            \"\"\"
            %s
            \"\"\"

            Gere a tabela contendo estritamente estas colunas para cada classe identificada:

            | Classe | Camada | Parâmetros / Métodos | Retorno | Regra / Responsabilidade de Negócio | Exceções Customizadas Lançadas | Dependências Injetadas |
            """.formatted(currentDoc, todosCodigos.toString());

        return chatModel.call(new Prompt(prompt)).getResult().getOutput().getText();
    }

    // PROMPT 3: Cria o README focado em impressionar no GitHub (Portfólio)
    public String generateReadmePortfolio(String currentReadme, String tabelaArquitetura) {
        String prompt = """
            Atue como um Especialista em Developer Relations e Portfólio do GitHub.
            Crie ou atualize o arquivo README.md principal deste projeto para torná-lo extremamente profissional.
            
            🛑 INSTRUÇÕES:
            - Use Badges visuais atraentes no topo para as tecnologias (Java 21, Spring Boot, Spring AI, Ollama, Clean Architecture).
            - Adicione seções de: Visão Geral do Sistema, Princípios Arquiteturais Aplicados, Guia de Execução Rápida (Como rodar o App e o Ollama).
            - NÃO inclua códigos longos do sistema.
            
            Aqui está a estrutura de classes atual para te apoiar no resumo do sistema:
            \"\"\"
            %s
            \"\"\"
            
            Retorne o texto completo do README.md estruturado.
            """.formatted(tabelaArquitetura);

        return chatModel.call(new Prompt(prompt)).getResult().getOutput().getText();
    }
}
