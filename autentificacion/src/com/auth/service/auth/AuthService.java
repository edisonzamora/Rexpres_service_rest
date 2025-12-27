package com.auth.service.auth;

import com.auth.dto.AuthDto;
import com.auth.dto.LoginUsuarioDto;

public interface AuthService {
	
	public AuthDto authAltaUsuario(LoginUsuarioDto dto);
	
	public AuthDto authGenerarToken(AuthDto dto);
}
