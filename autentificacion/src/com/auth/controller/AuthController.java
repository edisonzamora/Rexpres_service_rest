package com.auth.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth.dto.AuthDto;
import com.auth.dto.LoginUsuarioDto;
import com.auth.dto.PasswordDto;
import com.auth.service.auth.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autentificaciones", description = "Opciones para Autenificarse en los servicon del api")
public class AuthController {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	@Qualifier("passwordEncoder")
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public AuthService authService;

	@Operation(summary = "Codificar Contraseña")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
			                @ApiResponse(responseCode = "406", description = "Usuario no existe") })
	@PostMapping("${auth.passwordencryter}")
	@ResponseBody
	private ResponseEntity<String> codificapass(@RequestBody PasswordDto pass) {
		logger.info(">>codificando password....<<");
		String passEncoder = bCryptPasswordEncoder.encode(pass.getPassword());
		logger.info(">>password codificado: " + passEncoder + "<<");
		return ResponseEntity.ok(passEncoder);
	}

	@Operation(summary = "Dar de alta un usuaro para que pueda autentificarce en los sercios de API")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Se da de alta el usuario, devuelde el usuario"),
	                        @ApiResponse(responseCode = "401", description = "Si no esta autorizado para usar este servico") })
	@PostMapping("${auth.alta}")
	@ResponseBody
	private ResponseEntity<?> altaUsuario(@RequestBody LoginUsuarioDto dto) {

		AuthDto newLoginU = authService.authAltaUsuario(dto);

		Optional<AuthDto> user = Optional.ofNullable(newLoginU);

		if (user.isPresent())
			return ResponseEntity.ok(user.get());

		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}

	@Operation(summary = "Genera el token para autentificarce en la API")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Se a generado el TOKEN correctamente (se enviara el token por header)"),
	                        @ApiResponse(responseCode = "401", description = "Si no esta autorizado para usar este servico") })
	@PostMapping("${auth.generarToken}")
	private ResponseEntity<?> generarToken(@RequestBody AuthDto dto) {
		logger.info(">>generarToken<<");
		AuthDto newLoginU = authService.authGenerarToken(dto);

		Optional<AuthDto> user = Optional.ofNullable(newLoginU);

		if (user.isPresent())
			return ResponseEntity.status(HttpStatus.OK).header("TOKEN", user.get().getToken()).build();

		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}

}
