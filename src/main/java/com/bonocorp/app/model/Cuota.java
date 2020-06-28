package com.bonocorp.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "cuotas")
public @Data class Cuota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "numero_cuota")
	private Integer numeroCuota;

	//@Column(name = "inflacion_periodo")
	//private Double inflacionPeriodo;

	@Column(name = "valor_bono")
	private Double valorBono;

	//@Column(name = "bono_indexado")
	//private Double bonoIndexado;

	@Column(name = "cupon")
	private Double cupon;

	//@Column(name = "escudo")
	//private Double escudo;

	@Column(name = "flujo_emisor")
	private Double flujoEmisor;

	@Column(name = "flujo_emisor_con_escudo")
	private Double flujoEmisorConEscudo;

	@Column(name = "flujo_bonista")
	private Double flujoBonista;


}
