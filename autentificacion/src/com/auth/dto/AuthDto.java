package com.auth.dto;

public class AuthDto {

private String correo;

private String password;
	
private String token;

public String getToken() {
	return token;
}

public void setToken(String token) {
	this.token = token;
}

public String getCorreo() {
	return correo;
}

public void setCorreo(String correo) {
	this.correo = correo;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

}
