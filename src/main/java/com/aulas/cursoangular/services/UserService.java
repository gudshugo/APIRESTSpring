package com.aulas.cursoangular.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.aulas.cursoangular.security.UserSS;

public class UserService {
	
	//Retorna usuário logado no sistema
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch(Exception e) {
			return null;
		}
	}
	
}
