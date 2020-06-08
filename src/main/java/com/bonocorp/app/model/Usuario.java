package com.bonocorp.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="usuarios")
public @Data class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	
	@Column(name="nombre_usuario")
	private String nombreUsuario;
	
	@Column
	private String contraseña;
	
	@Column
	private String nombres;

}
