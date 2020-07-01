package com.bonocorp.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bonocorp.app.model.Bono;
import com.bonocorp.app.model.Usuario;
import com.bonocorp.app.service.BonoService;
import com.bonocorp.app.service.UsuarioService;


@Controller
@RequestMapping("/bonocorp/bono")
@SessionAttributes({"bono", "cuota", "usuario"})
public class BonoController {

	@Autowired
	private BonoService bonoServ; 
	
	@Autowired
	private UsuarioService usuarioServ;
	
	private static Usuario autenticado=null; 

	/*
	@ModelAttribute
	public void getUsuario(Model model) {
		model.addAttribute("autenticado", autenticado);
	}
	*/
	
	//OBTENER SOLO LOS BONOS DEL USUARIO
	@GetMapping("/misbonos/{id}")
	public String misbonos(@PathVariable("id")Integer id, Model model){

		try {
			autenticado=usuarioServ.findById(id).orElse(null);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	
		try {
		model.addAttribute("autenticado", autenticado);
		model.addAttribute("bonos", autenticado.getBonos());
		model.addAttribute("titulo", ": Mis bonos");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "/bono/start";
	}
	
	//OBTENER SOLO LOS BONOS DEL USUARIO
		@GetMapping("/misbonos")
		public String misbonos(Model model){
			try {
				List<Bono> bonos = bonoServ.readAll();
				model.addAttribute("bonos", bonos);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
			return "/bono/start";
		}

	@GetMapping("/nuevo")
	public String nuevo(Model model){
		Bono bono= new Bono();
		model.addAttribute("bono", bono);
		model.addAttribute("titulo",": Nuevo bono");
		return "/bono/nuevo";
	}
	
	@PostMapping("/save")
	public String save (@ModelAttribute("bono")Bono bono, Model model, SessionStatus status) {
		try {
			bono.CalcularFlujo();
			bonoServ.create(bono);
			status.setComplete();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/bonocorp/bono/misbonos";
	}
	
	
	
	@GetMapping("/info/{id}")
	public String edit(@PathVariable("id") Integer id,  Model model) {
		try {
			Optional<Bono> optional = bonoServ.findById(id);
			if(optional.isPresent()) {
				model.addAttribute("bono", optional.get());
				model.addAttribute("cuotas", optional.get().getCuotas());
				model.addAttribute("titulo", ": Información del bono");
				//List<Cuota> cuotas = optional.get().getCuotas();
				//model.addAttribute("cuotas", cuotas);
			}
			
			else {
				return "redirect:/bono/start";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/bono/info";
	}
	@GetMapping("/del/{id}")
	public String del(@PathVariable("id") Integer id,  Model model) {
		try {
			Optional<Bono> optional = bonoServ.findById(id);
			if( optional.isPresent() ) {
				bonoServ.deleteById(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/bonocorp/bono/misbonos";
	}
	
}
