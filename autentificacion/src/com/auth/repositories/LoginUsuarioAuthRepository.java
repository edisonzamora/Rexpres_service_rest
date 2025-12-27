package com.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.entity.LoginUsuario;

@Repository
public interface LoginUsuarioAuthRepository extends JpaRepository<LoginUsuario, Integer> {

Optional<LoginUsuario> findByCorreo(String correo);

}
