package com.bonocorp.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bonocorp.app.model.Usuario;
import com.bonocorp.app.repository.AutorizacionRepository;
import com.bonocorp.app.repository.UsuarioRepository;


@Service
public class AddUserDB implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private AutorizacionRepository autorizacionRepository;
	
	
	@Override
	public void run(String... args) throws Exception {
		
		// SOLO DESBLOQUEAR CUANDO SE REQUIERA CREAR USUARIO DE FORMA MANUAL
		
		/*Usuario business = new Usuario();
		business.setNombreUsuario("empresa1");
		business.setContraseña(new BCryptPasswordEncoder().encode("empresa"));
		business.setNombres("La Gran sangre");
		business.setEnable(true);
		
		Usuario costumer = new Usuario();
		costumer.setNombreUsuario("cliente1");
		costumer.setContraseña(new BCryptPasswordEncoder().encode("cliente"));
		costumer.setNombres("Shawn Marphy");
		costumer.setEnable(true);
		
		business.addAutorizacion("ROLE_EMPRESA");
		business.addAutorizacion("ACCESS_ADDBONO");
		business.addAutorizacion("ACCESS_EDITBONO");
		business.addAutorizacion("ACCESS_DELBONO");
		
		costumer.addAutorizacion("ROLE_CLIENTE");
		costumer.addAutorizacion("ACCESS_VIEWBONO");
		
		usuarioRepository.save(business);
		usuarioRepository.save(costumer);*/
	}

}
