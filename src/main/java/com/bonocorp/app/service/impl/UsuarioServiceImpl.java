package com.bonocorp.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonocorp.app.model.Usuario;
import com.bonocorp.app.repository.UsuarioRepository;
import com.bonocorp.app.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	UsuarioRepository usuarioRepo;
	@Override
	public Usuario create(Usuario entity) throws Exception {
		return usuarioRepo.save(entity);
	}

	@Override
	public List<Usuario> readAll() throws Exception {
		return usuarioRepo.findAll();
	}

	@Override
	public Optional<Usuario> findById(Integer id) throws Exception {
		return usuarioRepo.findById(id);
	}

	@Override
	public Usuario update(Usuario entity) throws Exception {
		return usuarioRepo.save(entity);
	}

	@Override
	public void deleteById(Integer id) throws Exception {
		usuarioRepo.deleteById(id);
	}

	@Override
	public void deleteAll() throws Exception {
		usuarioRepo.deleteAll();
	}

	@Override
	public Optional<Usuario> findByUsername(String nombreUsuario) throws Exception {
		return usuarioRepo.findByNombreUsuario(nombreUsuario);
	}
	

}
