package com.bob.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class FileReaderService {

    // Certifique-se de que o nome aqui seja exatamente listJavaFilesRecursively
    public List<Path> listJavaFilesRecursively(String directory) throws IOException {
        Path dir = Paths.get(directory);
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            throw new IllegalArgumentException("O caminho fornecido não é um diretório válido: " + directory);
        }

        // O Files.walk garante a varredura de todas as subpastas!
        try (Stream<Path> paths = Files.walk(dir)) {
            return paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .toList();
        }
    }

    public String readFile(Path file) throws IOException {
        return Files.readString(file);
    }
}