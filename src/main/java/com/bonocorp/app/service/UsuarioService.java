package com.bonocorp.app.service;

import java.util.Optional;

import com.bonocorp.app.model.Usuario;

public interface UsuarioService extends CrudService<Usuario, Integer> {
	Optional<Usuario> findByUsername(String nombreUsuario) throws Exception;

}
