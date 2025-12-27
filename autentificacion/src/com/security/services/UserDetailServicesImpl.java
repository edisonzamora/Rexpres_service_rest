package com.security.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.auth.dto.LoginUsuarioDto;
import com.auth.service.login.LoginService;

@Service
public class UserDetailServicesImpl implements UserDetailsService {

	@Autowired
	private LoginService loginService;

	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
		LoginUsuarioDto logi = new LoginUsuarioDto();
		logi.setCorreo(correo);
		LoginUsuarioDto loginCorrecto = loginService.buscarLoginPorCorreo(logi);
		List<GrantedAuthority> roles = new ArrayList<>();

		if (loginCorrecto.getRole().equalsIgnoreCase("ADM")) {

			roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		if (loginCorrecto.getRole().equalsIgnoreCase("USU")) {

			roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		UserDetails user = new User(loginCorrecto.getCorreo(), loginCorrecto.getPassword(), roles);

		return user;
	}

}
