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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.bonocorp.app.util.Metodos;

import lombok.Data;

@Entity
@Table(name = "bonos")
public @Data class Bono {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Min(value = 1, message = "Debe colocar un valor positivo")
	@NotNull(message = "Debe ingresar el valor nominal")
	@Column(name = "valor_nominal", nullable = false)
	private Double valorNominal;

	@Min(value = 1, message = "Debe colocar un valor positivo")
	@NotNull(message = "Debe ingresar el valor comercial")
	@Column(name = "valor_comercial", nullable = false)
	private Double valorComercial;

	// 1- sol, 2-dolar, 3-euro
	@Column(name = "tipo_moneda")
	private Integer tipoMoneda;

	@NotNull(message = "Debe ingresar los años de pago")
	@Min(value = 1, message = "Ingrese un valor positivo")
	@Column(name = "años_de_pago")
	private Integer añosDePago;

	@NotNull(message = "Debe elegir el periodo de pago")
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
	@Min(value = 0, message = "Ingrese un valor positivo")
	@Column(name = "tea")
	private Double tea;

	@Column(name = "tasa_de_descuento")
	private Double tasaDescuento;

	// DATOS CALCULADOS
	@Column(name = "periodos_por_año")
	private Integer periodosPorAño;
	@Column(name = "total_periodos")
	private Integer totalPeriodos;
	@Column(name = "tasa_del_periodo")
	private Double tasaPeriodo;
	@Column(name = "tep")
	private Double tep;
	@Column(name = "escudo")
	private Double escudo;

	// MÉTODO

	@Column(name = "metodo", nullable = false)
	private String metodo;

	// USUARIO DEL BONO
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	// COUTAS, CUPONES
	// cascadetype.all para persistir la lista
	// orphan removal para eliminar los elementos anteriores
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "bono_id")
	private List<Cuota> cuotas = new ArrayList<Cuota>();

	// RESULTADOS FINALES

	@Column(name = "tcea_emisor")
	private Double tceaEmisor;

	@Column(name = "tcea_emisor_con_escudo")
	private Double tceaEmisorConEscudo;

	@Column(name = "trea_bonista")
	private Double treaBonista;

	@Column(name = "precio_actual")
	private Double precioActual;

	@Column(name = "valor_neto_actual")
	private Double valorNetoActual;

	public void CalcularDatos() {

	}

	public void CalcularFlujo() {

		// SE AMORTIZA EN LA UTLTIMA CUOTA

		cuotas.clear();
		// se inicializa la cuota

		// Si el metodo es frances
		Double cuotacte = 0.0, amortizacion = 0.0;
		Double interes = 0.0;
		//if (metodo == "Frances"){

			Double num = 0.0, den = 0.0, t=0.0;
			t = tep/100;
			num = Math.pow(1+t, totalPeriodos)* t;
			den = Math.pow(1+t, totalPeriodos) -1;
			cuotacte = -1.0*valorNominal * num/den;


			System.out.println("i:"+t+", vnom: "+valorNominal+ ", num: "+num+" ,den: "+den+", cuotacte: "+cuotacte);
		//}
		//if (metodo == "Aleman")
			amortizacion = Metodos.redondearDecimales(valorNominal / totalPeriodos, 2);

		// desde el periodo 0, hasta el total calculado
		for (int i = 0; i <= totalPeriodos; i++) {

			Cuota aux = new Cuota();

			aux.setNumeroCuota(i);

			// CUOTA 0
			if (i == 0) {

				double suma1 = (porcentajeCavali + porcentajeColocacion + porcentajeEstructuracion
						+ porcentajeFlotacion) / 100;

				aux.setFlujoEmisor(Metodos.redondearDecimales(valorComercial * (1 - suma1), 2));

				double suma2 = (porcentajeFlotacion + porcentajeCavali) / 100;

				aux.setFlujoEmisorConEscudo(aux.getFlujoEmisor());

				aux.setFlujoBonista(Metodos.redondearDecimales(-1 * valorComercial * (1 + suma2), 2));

			}

			// DEMAS CUOTAS
			else {

				switch (metodo) {
					case "Americano":
						aux.setValorBono(valorNominal);
						interes = Metodos.redondearDecimales(((tep / 100) * -1 * aux.getValorBono()), 2);
						aux.setCupon(interes);
						if (i == totalPeriodos)
							aux.setCupon(interes - valorNominal);
						break;
					case "Aleman":

						if (i == 1) {
							aux.setValorBono(valorNominal);
							interes = (tep / 100) * -1 * aux.getValorBono();
							aux.setCupon(interes - amortizacion);
						} else {
							aux.setValorBono(cuotas.get(i - 1).getValorBono() - amortizacion);
							interes = (tep / 100) * -1 * aux.getValorBono();

							aux.setCupon(interes - amortizacion);
						}
						break;

					case "Frances":

						if (i == 1) {
							aux.setValorBono(valorNominal);
							interes = (tep / 100) * -1 * aux.getValorBono();
							
						} else {
							//interes de la cuota anterior
							aux.setValorBono(cuotas.get(i - 1).getValorBono() + cuotacte - interes);
							//interes de cuota actual
							interes = (tep / 100) * -1 * aux.getValorBono();
						
						}
						aux.setCupon(cuotacte);
						break;
				}
				aux.setCupon(Metodos.redondearDecimales(aux.getCupon(), 2));
				if (metodo != "Americano")
					escudo = Metodos.redondearDecimales(interes * 0.3, 2);
				if (i != totalPeriodos)
					aux.setFlujoEmisor(aux.getCupon());

				// en ultima cuota
				else {
					Double prima = aux.getValorBono() * (porcentajePrima / 100);
					aux.setFlujoEmisor(aux.getCupon() - prima);
				}
				
				aux.setValorBono(Metodos.redondearDecimales(
					aux.getValorBono()
					,2));

				aux.setFlujoEmisor(Metodos.redondearDecimales(
					aux.getFlujoEmisor()
					, 2));

				aux.setFlujoEmisorConEscudo(Metodos.redondearDecimales(
					(aux.getFlujoEmisor() - escudo)
					,2));

				aux.setFlujoBonista(Metodos.redondearDecimales(
					(aux.getFlujoEmisor() * -1)
					,2));



			}

			cuotas.add(aux);
		}

		// CalculoFinal();
	}

	public void CalculoFinal() {
		System.out.println("tir: " + Metodos.hallarTir(cuotas));
	}
}
