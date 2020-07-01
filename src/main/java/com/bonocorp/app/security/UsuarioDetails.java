package com.bonocorp.app.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bonocorp.app.model.Usuario;

public class UsuarioDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	//Inyeccion de Dependencias
	private Usuario usuario;
	public UsuarioDetails(Usuario usuario) {
		super();
		this.usuario = usuario;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		this.usuario.getAutorizaciones().forEach(autorizacion -> {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(autorizacion.getAutorizacion());
			grantedAuthorities.add(grantedAuthority);
		});
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return this.usuario.getContraseña();
	}

	@Override
	public String getUsername() {
		return this.usuario.getNombreUsuario();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.usuario.isEnable();
	}

}
