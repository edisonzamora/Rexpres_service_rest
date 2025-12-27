package com.auth.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.dto.LoginUsuarioDto;
import com.auth.entity.LoginUsuario;
import com.auth.repositories.LoginUsuarioAuthRepository;

@Service
public class LoginServiceImp implements LoginService{

	@Autowired
	private LoginUsuarioAuthRepository loginUsuarioAuthRepository;

	@Override
	public LoginUsuarioDto guardarUsuarioLogin(LoginUsuarioDto dto) {
		LoginUsuario loginU=new LoginUsuario();
		loginU.setCorreo(dto.getCorreo());
		loginU.setNombre(dto.getNombre());
		loginU.setPassword(dto.getPassword());
		loginU.setRole(dto.getRole());
		LoginUsuario newLoginU=	loginUsuarioAuthRepository.save(loginU);
		return mappLoginUsuarioToLoginUserDto(newLoginU);
	}
	
	private LoginUsuarioDto mappLoginUsuarioToLoginUserDto( LoginUsuarioDto dto , LoginUsuario entity ) {
		if(dto ==null) {
			dto = new LoginUsuarioDto();
		}
		dto.setId(entity.getId());
		dto.setCorreo(entity.getCorreo());
		dto.setNombre(entity.getNombre());
		dto.setPassword(entity.getPassword());
		dto.setRole(entity.getRole());
		return dto;
		
		
	}
	private LoginUsuarioDto mappLoginUsuarioToLoginUserDto(LoginUsuario entity ) {
		LoginUsuarioDto dto = new LoginUsuarioDto();
		mappLoginUsuarioToLoginUserDto(dto, entity );
		return dto;
	}

	@Override
	public LoginUsuarioDto buscarLoginPorCorreo(LoginUsuarioDto dto) {
		// TODO Auto-generated method stub
		LoginUsuario loginUsuario=loginUsuarioAuthRepository.findByCorreo(dto.getCorreo()).get();
		return mappLoginUsuarioToLoginUserDto(loginUsuario);
	}
}
