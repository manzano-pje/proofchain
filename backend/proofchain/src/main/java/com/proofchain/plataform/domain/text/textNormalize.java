package com.proofchain.plataform.domain.text;

import java.util.Set;

public final class textNormalize {

    // Lista de palavras que devem ficar em minúsculas, como "de", "da", etc.
    private static final Set<String> IGNORADAS = Set.of(
            "da","de","do","das","dos","e","em","com","um","uma","uns","umas","o","a"
    );

    public static String normalize(String text) {
        if (text == null || text.isBlank()) {
            return text; // Retorna texto vazio ou nulo diretamente
        }

        // Divide o texto em palavras
        String[] words = text.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();

            // Verifica se a palavra é uma palavra de ligação e não é a primeira palavra
            if (i == 0 || !IGNORADAS.contains(word)) {
                word = capitalize(word);
            }

            // Adiciona a palavra ao resultado, com espaço entre as palavras
            result.append(i > 0 ? " " : "").append(word);
        }

        return result.toString();
    }
    private static String capitalize(String word) {
        return word.substring(0,1).toUpperCase() + word.substring(1);
    }

}

