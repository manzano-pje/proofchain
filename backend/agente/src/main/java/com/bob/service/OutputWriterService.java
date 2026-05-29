package com.bob.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class OutputWriterService {

    public String readExistingReport(String projectRootPath, String reportName) {
        Path reportPath = Paths.get(projectRootPath, reportName);
        if (Files.exists(reportPath)) {
            try { return Files.readString(reportPath); } catch (IOException e) { return ""; }
        }
        return "";
    }

    public void saveGlobalReport(String projectRootPath, String reportName, String updatedContent) {
        Path reportPath = Paths.get(projectRootPath, reportName);
        try {
            Files.createDirectories(reportPath.getParent() != null ? reportPath.getParent() : reportPath);
            Files.writeString(reportPath, updatedContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar relatório: " + reportName, e);
        }
    }

    public void saveJavadocIntoFile(String projectRootPath, String filename, String aiOutput) {
        Path javaFilePath = findFileRecursively(Path.of(projectRootPath), filename);
        if (javaFilePath == null) return;

        try {
            String originalCode = Files.readString(javaFilePath);
            String classJavadoc = extractTagContent(aiOutput, "[JAVADOC_CLASSE]");

            if (classJavadoc != null && !originalCode.contains("Agente IA Bob")) {
                int insertIndex = findClassDeclarationIndex(originalCode);
                if (insertIndex != -1) {
                    String modifiedCode = originalCode.substring(0, insertIndex)
                            + classJavadoc + "\n"
                            + originalCode.substring(insertIndex);
                    Files.writeString(javaFilePath, modifiedCode);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int findClassDeclarationIndex(String code) {
        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("@Component") || line.startsWith("@Service") || line.startsWith("@RestController")
                    || line.startsWith("@Repository") || line.startsWith("public class") || line.startsWith("public interface")) {
                return code.indexOf(lines[i]);
            }
        }
        return -1;
    }

    private String extractTagContent(String text, String tag) {
        if (!text.contains(tag)) return null;
        try {
            int start = text.indexOf(tag) + tag.length();
            int end = text.indexOf("[", start);
            if (end == -1) end = text.length();
            return text.substring(start, end).trim();
        } catch (Exception e) { return null; }
    }

    private Path findFileRecursively(Path root, String filename) {
        try (var stream = Files.walk(root)) {
            return stream.filter(Files::isRegularFile).filter(p -> p.getFileName().toString().equals(filename)).findFirst().orElse(null);
        } catch (IOException e) { return null; }
    }
}
