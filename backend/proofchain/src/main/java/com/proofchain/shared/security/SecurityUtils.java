package com.proofchain.shared.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public SecurityUtils(){}

    /*
     * Retorna o usuário logado
     *
     * @return Long - ID da instituição
     */

    public static UserDetailsImpl getCurrentUser(){
        Authentication authentication = SecurityContextHolder
                                        .getContext()
                                        .getAuthentication();

        if(authentication == null){
            return null;
        }

        Object principal = authentication.getPrincipal();
        if(!(principal instanceof UserDetailsImpl user)){
            return null;
        }

        return user;
    }

    /**
     * Retorna o ID do usuário autenticado.
     */
    public static Long getUserId() {
        UserDetailsImpl user = getCurrentUser();
        if(user != null){
            return user.getId();
        }else {
            return null;
        }
    }

    /**
     *  Retorna o ID da instituição do usuário logado
     */
    public static Long getInstitutionId() {

        UserDetailsImpl user = getCurrentUser();

        if (user != null) {
            return user.getInstitutionId();
        }else{
            return null;
        }
    }

    /**
     * Retorna o e-mail do usuário autenticado.
     */
    public static String getUsername() {

        UserDetailsImpl user = getCurrentUser();

        if (user != null){
            return user.getUsername();
        }else {
            return null;
        }
    }

    /**
     * Retorna o papel (ROLE) do usuário autenticado.
     */
    public static String getRole() {

        UserDetailsImpl user = getCurrentUser();

        if(user != null) {
            return user.getRole();
        }else {
            return null;
        }
    }

    /**
     * Verifica se existe um usuário autenticado.
     */
    public static boolean isAuthenticated() {
        if(getCurrentUser() != null){
            return true;
        }else {
            return false;
        }
    }
}