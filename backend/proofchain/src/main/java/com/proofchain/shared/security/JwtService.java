package com.proofchain.shared.security;


// Geração e validação de token

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Service
@AllArgsConstructor
public class JwtService {
//
//
//
//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//    // Tempo de expiração do token (1 hora)
//    private final long expirationTime = 1000 * 60 * 60;
//
//    /**
//    * Gera um token JWT para o usuário autenticado.
//    *
//    * @param username identificador do usuário
//    * @return token JWT assinado
//    */
//
//    public String generateToken(String username){
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(key)
//                .compact();
//
//    }
//
//    /**
//    * Extrai o username contido no token.
//    *
//    * @param token JWT recebido do cliente
//    * @return username armazenado no token
//    */
//
//    public String extractUsername(String token){
//        return getClaims(token).getSubject();
//    }
//
//    /**
//     * Valida se o token ainda é válido (não expirado e assinado corretamente).
//     *
//     * @param token JWT recebido
//     * @return true se válido, false caso contrário
//     */
//
//    public boolean isTokenValid(String token){
//        try{
//            return  getClaims(token).getExpiration().after(new Date());
//        } catch (Exception e){
//            return false;
//        }
//    }
//
//    /**
//     * Extrai os dados internos (claims) do token.
//     */
//
//    private Claims getClaims(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJwt(token)
//                .getBody();
//    }
}

/*
🔧 Versão mais otimizada (sênior):
- Chave RSA
- Namespaces nos claims
- Refresh token
Motivo: segurança e escalabilidade
*/
