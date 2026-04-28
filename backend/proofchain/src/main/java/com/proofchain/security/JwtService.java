package com.proofchain.security;


// Geração e validação de token

import com.proofchain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;


@Service
@AllArgsConstructor
public class JwtService {

    // Em produção isso vem de variável de ambiente
    private final String secret = "secret-key";

    // Gera o token JWT
    public String generateToken(User user){

        return Jwts.builder()
                // ID do usuário
                .setSubject(user.getId().toString())

                // Claim da instituição (tenat)
                .claim("tenant_id",
                        user.getInstituition().getId().toString())

                // Claim do papel do usuário
                .claim("role", user.getRole().name())

                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis()+3600000)
                )
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // Valida token e retorna os claims
    public Claims validateToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

/*
🔧 Versão mais otimizada (sênior):
- Chave RSA
- Namespaces nos claims
- Refresh token
Motivo: segurança e escalabilidade
*/
