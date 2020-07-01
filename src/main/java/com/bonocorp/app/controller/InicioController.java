package com.bonocorp.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/bonocorp")
public class InicioController {

	@GetMapping
	public String starter() {
		return "index";
	}
	
	/*LOGIN*/
	@GetMapping("login")
	public String login() {
		return "login";
	}
	
}
