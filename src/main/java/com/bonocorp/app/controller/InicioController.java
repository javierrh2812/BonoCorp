package com.bonocorp.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("inicio")
@SessionAttributes("usuario")
public class InicioController {

	
	@Autowired
	private UsuarioService usuarioServ;
	
	
	/*LOGIN*/
	@GetMapping("/login")
	public String inicio(Model model){
		Usuario usuario = new Usuario();	
		usuario.setNombreUsuario("chupete");
		model.addAttribute("usuario", usuario);
		return "/inicio/login";
	}
	
	@PostMapping("/validar")
	public String validar(@ModelAttribute("usuario") Usuario usuario, Model model, 
			SessionStatus status) {
		try {
			System.out.println("DATOS MANDADOS EN LOGIN");
			System.out.println(usuario.getNombres());
			System.out.println(usuario.getContraseña());
			System.out.println(usuario.getNombreUsuario());
			System.out.println(usuario.getId());
			Usuario find = usuarioServ.findByUsername(usuario.getNombreUsuario()).orElse(null);
			if (find!=null) {
				System.out.println("Contraseña mandadada "+usuario.getContraseña());
				System.out.println("Contraseña de usuario "+find.getContraseña());

				if(find.getContraseña().equalsIgnoreCase(usuario.getContraseña())) {
					System.out.println("Credenciales correctas");
					status.setComplete();}
				
				else 
					System.out.println("Cotraseña erroneo o usuario inexistente");
			}
			else {
				System.out.println("usuario no encontrado");
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return "redirect:/inicio/index";
	}
	
	
	
	/*REGISTRO*/
	@GetMapping("/signin")
	public String signin(Model model){
		Usuario usuario = new Usuario();
		model.addAttribute("usuario", usuario);
		return "/inicio/signin";
	}
	@PostMapping("/save")
	public String save(@ModelAttribute("usuario") Usuario usuario, Model model, 
			SessionStatus status) {
		try {
			usuarioServ.create(usuario);
			System.out.println("DATOS DE USUARIO A REGISTRAR");
			System.out.println(usuario.getNombres());
			System.out.println(usuario.getContraseña());
			System.out.println(usuario.getNombreUsuario());
			System.out.println(usuario.getId());
			status.setComplete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return "redirect:/inicio/login";
	}
	
}
