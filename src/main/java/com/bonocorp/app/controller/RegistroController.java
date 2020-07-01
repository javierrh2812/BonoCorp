package com.bonocorp.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bonocorp.app.model.Usuario;
import com.bonocorp.app.service.UsuarioService;


@Controller
@RequestMapping("bonocorp/registro")
@SessionAttributes("usuario")
public class RegistroController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private String contraseña;
	
	@GetMapping
	public String start(Model model) {
		Usuario usuario = new Usuario();
		usuario.addAutorizacion("ROLE_EMPRESA");
		usuario.addAutorizacion("ACCESS_ADDBONO");
		usuario.addAutorizacion("ACCESS_EDITBONO");
		usuario.addAutorizacion("ACCESS_DELBONO");
		usuario.setEnable(true);
		model.addAttribute("usuario", usuario);
		return "/usuario/signin";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute("usuario") Usuario usuario, Model model, SessionStatus status) {
		try {
			this.contraseña = usuario.getContraseña();
			usuario.setContraseña( passwordEncoder.encode(this.contraseña));
			usuarioService.create(usuario);
			status.setComplete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return "redirect:/bonocorp/login";
	}
}
