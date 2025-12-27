package com.auth.service.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.auth.dto.AuthDto;
import com.auth.dto.LoginUsuarioDto;
import com.auth.service.login.LoginService;
import com.security.jwt.JwtTokenOperator;

@Service
public class AuthServiceImpl implements AuthService{
	
	protected final  Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenOperator  JwtTokenOperator;
	
	@Override
	public AuthDto authAltaUsuario(LoginUsuarioDto dtot) {
		logger.info(">>authAltaUsuario<<");
		   AuthDto response= new AuthDto();
		   LoginUsuarioDto loginusuario=loginService.guardarUsuarioLogin( dtot );
		   response.setCorreo(loginusuario.getCorreo());
		   response.setPassword(loginusuario.getPassword());
		
		   return response ;
	}

	@Override
	public AuthDto authGenerarToken(AuthDto dto) {
		logger.info(">>authGenerarToken<<");
		
		 Authentication authentication = authenticationManager.authenticate(
		            new UsernamePasswordAuthenticationToken(
		            		dto.getCorreo(),
		            		dto.getPassword()
		            )
		        );
		 String token = JwtTokenOperator.generateToken(authentication);
		 dto.setToken(token);
		 return dto;
	}
	
}
