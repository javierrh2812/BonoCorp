package com.bonocorp.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bonocorp.app.model.Usuario;
import com.bonocorp.app.service.UsuarioService;



@Controller
@SessionAttributes("usuario")
public class InicioController {

	
	@Autowired
	private UsuarioService usuarioServ;
	
	
	/*LOGIN*/
	@GetMapping(value = {"/inicio/login", "", "/"})
	public String inicio(Model model){
		Usuario usuario = new Usuario();	
		model.addAttribute("usuario", usuario);
		model.addAttribute("titulo", ": Iniciar sesión");
		return "/inicio/login";
	}
	
	
	//EL MÉTODO LOGIN MÁS INSEGURO DEL MUNDO XD
	@PostMapping("/inicio/validar")
	public String validar(@ModelAttribute("usuario") Usuario usuario, Model model, 
			SessionStatus status) {
		try {
			
			//BUSCA SI EL USUARIO CON EL NOMBRE DE USUARIO ENVIADO EXISTE
			Usuario find = usuarioServ.findByUsername(usuario.getNombreUsuario()).orElse(null);
			
			if (find!=null) {
				//SI EXISTE, COMPARAR LA CONTRASEÑA MANDADA CON LA DEL USUARIO
				if(find.getContraseña().equalsIgnoreCase(usuario.getContraseña())) {
					status.setComplete();
					return "redirect:/bono/misbonos/"+find.getId();
					}

			}
						
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return "redirect:/inicio/index";
	}
	
	
	
	/*REGISTRO*/
	@GetMapping("/inicio/signin")
	public String signin(Model model){
		Usuario usuario = new Usuario();
		model.addAttribute("usuario", usuario);
		model.addAttribute("titulo", ": Registro");
		return "/inicio/signin";
	}
	@PostMapping("/inicio/save")
	public String save(@ModelAttribute("usuario") Usuario usuario, Model model, 
			SessionStatus status) {
		try {
			usuarioServ.create(usuario);
			status.setComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return "redirect:/inicio/login";
	}
	
}
