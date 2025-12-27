package com.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.auth.dto.LoginUsuarioDto;
import com.auth.service.login.LoginService;

@RestController
@RequestMapping("/login")
public class LoginUsuarioController {

	
	@Autowired
	private LoginService loginService;

	@PostMapping("/alta")
	@ResponseBody
	private ResponseEntity<?> altaUsuario(@RequestBody LoginUsuarioDto dto){
		
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}
	
}
