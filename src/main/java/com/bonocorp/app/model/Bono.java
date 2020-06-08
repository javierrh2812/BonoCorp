package com.bonocorp.app.model;

import javax.persistence.FetchType;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name = "bonos")
public @Data class Bono {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// DATOS DEL BONO (ok)
	@Column(name = "valor_nominal") 
	private Double valorNominal;
	@Column(name = "valor_comercial")
	private Double valorComercial;
	@Column(name = "años_de_pago")
	private Integer añosDePago;
	@Column(name = "dias_por_periodo")
	private Integer diasPorPeriodo;
	
	@Column(name = "fecha_de_emision")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaDeEmision;

	
	// DATOS POR DEFECTO
	@Column(name = "dias_por_año")
	private Integer diasPorAño = 360;
	@Column(name = "impuesto_renta")
	private Double impuestoRenta = 30.0;

	// PORCENTAJES  (OK)
	@Column(name = "porcentaje_prima")
	private Double porcentajePrima;
	@Column(name = "porcentaje_estructuracion")
	private Double porcentajeEstructuracion;
	@Column(name = "porcentaje_flotacion")
	private Double porcentajeFlotacion;
	@Column(name = "porcentaje_colocacion")
	private Double porcentajeColocacion;
	@Column(name = "porcentaje_cavali")
	private Double porcentajeCavali;

	// TASAS (ok)
	@Column(name = "tea")
	private Double tea;
	@Column(name = "tasa_de_descuento")
	private Double tasaDescuento = 0.0;
	@Column(name = "tasa_inflacion_anual")
	private Double tasaInflacionAnual = 0.0;

	// DATOS CALCULADOS
	@Column(name = "periodos_por_año")
	private Integer periodosPorAño;
	@Column(name = "total_periodos")
	private Integer totalPeriodos;
	@Column(name = "tasa_del_periodo")
	private Double tasaPeriodo;
	@Column(name = "tep")
	private Double tep;
	@Column(name = "tasa_inflacion_periodo")
	private Double tasaInflacionPeriodo = 0.0;

	// MÉTODO
	@Column(name = "metodo")
	private String metodo;

	// USUARIO DEL BONO
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	// COUTAS, CUPONES
	@OneToMany(mappedBy = "bono")
	private List<Cuota> cuotas;

	public void CalcularDatos() {

		this.periodosPorAño = diasPorAño / diasPorPeriodo;
		this.totalPeriodos = periodosPorAño * añosDePago;
		this.tep = Math.pow(1 + tea, diasPorPeriodo / diasPorAño) - 1;
		this.tasaPeriodo = Math.pow(1 + tasaDescuento, diasPorPeriodo / diasPorAño) - 1;

	}

	public void CalcularFlujo() {

		switch (metodo) {

		case "Alemán":
			break;
		case "Fracés":
			break;
		case "Americano":
			break;

		}

	}
}
