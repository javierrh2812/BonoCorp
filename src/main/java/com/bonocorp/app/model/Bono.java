package com.bonocorp.app.model;

import javax.persistence.FetchType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
@Entity
@Table(name = "bonos")
public @Data class Bono {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "valor_nominal", nullable = false)
	private Double valorNominal;
	
	@Column(name = "valor_comercial", nullable = false)
	private Double valorComercial;
	@Column(name = "años_de_pago")
	private Integer añosDePago;
	@Column(name = "dias_por_periodo")
	private Integer diasPorPeriodo;

	@Column(name = "fecha_de_emision")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date fechaDeEmision;

	@Column(name = "dias_por_año")
	private Integer diasPorAño = 360;
	@Column(name = "impuesto_renta")
	private Double impuestoRenta = 30.0;

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

	// TASAS
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
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "bono_id")
	private List<Cuota> cuotas;

	public void CalcularDatos() {

		this.periodosPorAño = diasPorAño / diasPorPeriodo;
		this.totalPeriodos = periodosPorAño * añosDePago;

		System.out.println(1 + tea / 100);
		System.out.println(1.00 * diasPorPeriodo / diasPorAño);
		this.tep = Math.pow(1 + tea / 100, 1.00 * diasPorPeriodo / diasPorAño) - 1;
		System.out.println("tep:" + tep);
		this.tep *= 100;
		System.out.println("tep %: " + tep);
		this.tasaPeriodo = Math.pow(1 + tasaDescuento / 100, diasPorPeriodo / diasPorAño) - 1;
		this.tasaPeriodo *= 100;

	}

	public void CalcularMetodoFrances() {

		// SE CALCULA PRIMERO LA RENTA

	}

	public void CalcularMetodoAleman() {

		// SE CALCULA PRIMERO LA AMORTIZACION

	}

	public void CalcularMetodoAmericano() {

		// SE AMORTIZA EN LA UTLTIMA CUOTA

	}

	public void CalcularFlujo() {
		
		cuotas = new ArrayList<Cuota>();
		
		double escudo = valorNominal * tep * impuestoRenta / 10000;
		for (int i = 0; i <= totalPeriodos; i++) {
			Cuota aux = new Cuota();

			aux.setNumeroCuota(i);
			if (i == 0) {
				aux.setInflacionPeriodo(0.0);
				double suma1 = porcentajeCavali + porcentajeColocacion + porcentajeEstructuracion + porcentajeFlotacion;
				suma1 /= 100;
				aux.setFlujoEmisor(this.valorComercial * (1 - suma1));
				aux.setFlujoEmisorConEscudo(aux.getFlujoEmisor());
				double suma2 = porcentajeFlotacion + porcentajeCavali;
				suma2 /= 100;
				aux.setFlujoBonista(-1 * this.valorComercial * (1 + suma2));

			} else {
				aux.setValorBono(this.valorNominal);
				aux.setBonoIndexado(this.valorNominal * (1 + this.tasaInflacionPeriodo / 100));
				aux.setCupon((tep / 100) * -1 * aux.getBonoIndexado());
				if (i != this.totalPeriodos)
					aux.setFlujoEmisor(aux.getCupon());
				else
					aux.setFlujoEmisor(aux.getCupon() - aux.getBonoIndexado() * (1 + porcentajePrima / 100));
				aux.setFlujoEmisorConEscudo(aux.getFlujoEmisor() + escudo);// menos IR
				aux.setFlujoBonista(aux.getFlujoEmisor() * -1);
			}
			aux.setFlujoEmisor((aux.getFlujoEmisor()*100)/100);
			aux.setFlujoBonista((aux.getFlujoBonista()*100)/100);


			System.out.println(aux.toString());
			cuotas.add(aux);

		}

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
