package com.bonocorp.app.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bonocorp.app.model.Usuario;
import com.bonocorp.app.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Optional<Usuario> optional = usuarioRepository.findByNombreUsuario(username);
			if (optional.isPresent()) {
				UsuarioDetails usuarioDetails = new UsuarioDetails( optional.get() );
				return usuarioDetails;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		throw new UsernameNotFoundException("El usuario ingresado no existe");
	}
	
	
	
}
