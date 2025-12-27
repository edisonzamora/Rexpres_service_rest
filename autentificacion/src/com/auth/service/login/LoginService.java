package com.auth.service.login;

import com.auth.dto.LoginUsuarioDto;

public interface LoginService {

	public LoginUsuarioDto guardarUsuarioLogin(LoginUsuarioDto dto);
	
	public LoginUsuarioDto buscarLoginPorCorreo(LoginUsuarioDto dto);
	
	
}
