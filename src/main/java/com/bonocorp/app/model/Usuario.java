package com.bonocorp.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "usuarios")
public @Data class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "nombre_usuario")
	private String nombreUsuario;

	@Column
	private String contraseña;

	@Column
	private String nombres;
	
	private boolean enable;
	
	@OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Autorizacion> autorizaciones;

	// COUTAS, CUPONES
	// cascadetype.all para persistir la lista
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Bono> bonos;
	
	public Usuario() {
		this.enable = true;
		this.autorizaciones = new ArrayList<>();
		this.bonos = new ArrayList<>();
	}
	
	public Usuario(String nombreUsuario, String contraseña, String nombres) {
		this.nombreUsuario = nombreUsuario;
		this.contraseña = contraseña;
		this.nombres = nombres;
		this.enable = true;
		this.autorizaciones = new ArrayList<>();
		this.bonos = new ArrayList<>();
	}
	
	public void addAutorizacion(String auto) {
		Autorizacion autorizacion = new Autorizacion();
		autorizacion.setAutorizacion(auto);
		autorizacion.setUsuario(this);
		
		this.autorizaciones.add(autorizacion);
	}
	

}
