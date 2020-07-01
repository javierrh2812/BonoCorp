package com.bonocorp.app.model;

import javax.persistence.FetchType;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.bonocorp.app.util.Metodos;

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

	// 1- sol, 2-dolar, 3-euro
	@Column(name = "tipo_moneda")
	private Integer tipoMoneda;

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
	private Double tasaDescuento;
	@Column(name = "inflacion_anual")
	private Double tasaInflacion=0.00;
	

	// DATOS CALCULADOS
	@Column(name = "periodos_por_año")
	private Integer periodosPorAño;
	@Column(name = "total_periodos")
	private Integer totalPeriodos;
	@Column(name = "tasa_del_periodo")
	private Double tasaPeriodo;
	@Column
	private Double tep;
	@Column(name="tasa_inflacion_periodo")
	private Double tInfPer;

	@Column(name = "metodo", nullable = false)
	private String metodo;


	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	// COUTAS, CUPONES
	// cascadetype.all para persistir la lista
	// orphan removal para eliminar los elementos anteriores
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "bono_id")
	private List<Cuota> cuotas = new ArrayList<Cuota>();

	// RESULTADOS FINALES
	
	@Column
	private Double va;
	
	@Column
	private Double van;

	@Column(name = "tcea_emisor")
	private Double tceaEmisor;

	@Column(name = "tcea_emisor_con_escudo")
	private Double tceaEmisorConEscudo;

	@Column(name = "trea_bonista")
	private Double treaBonista;


	public void CalcularDatos() {

	}

	// ESTE METODO FUNCIONA SIN PLAZOS DE GRACIA
	public void CalcularFlujo() {

		cuotas.clear();

		// Si el metodo es frances
		Double cuotacte = 0.0, amortizacion = 0.0;
		Double interes = 0.0;
		// if (metodo == "Frances"){

		Double num = 0.0, den = 0.0, t = 0.0;
		t = tep / 100;
		num = Math.pow(1 + t, totalPeriodos) * t;
		den = Math.pow(1 + t, totalPeriodos) - 1;
		cuotacte = -1.0 * valorNominal * num / den;

		// if (metodo == "Aleman")
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
				
				aux.setFechaPago(fechaDeEmision);

			}

			// DEMAS CUOTAS
			else {

				Calendar calendar = Calendar.getInstance();
				
				calendar.setTime(cuotas.get(i-1).getFechaPago());
				calendar.add(Calendar.DAY_OF_YEAR, diasPorPeriodo);
				
				aux.setFechaPago(calendar.getTime());
				
				switch (metodo) {
				
				
				case "Americano":
					aux.setValorBono(valorNominal);
					aux.setBonoIndexado(Metodos.redondearDecimales(
							valorNominal*((tInfPer/100)+1)
							, 2));
					interes = Metodos.redondearDecimales(((tep / 100) * -1 * aux.getBonoIndexado()), 2);
					aux.setCuota(interes);
					if (i == totalPeriodos)
						aux.setCuota(interes - valorNominal);
					break;
					
					
					
					
				case "Aleman":
					
					if (i == 1) aux.setValorBono(valorNominal);

				    else aux.setValorBono(cuotas.get(i - 1).getValorBono() - amortizacion);
					
					
					aux.setBonoIndexado(Metodos.redondearDecimales(
							aux.getValorBono()*((tInfPer/100)+1)
							, 2));
					
					interes = Metodos.redondearDecimales(((tep / 100) * -1 * aux.getBonoIndexado()), 2);
					
					aux.setCuota(interes - amortizacion);
					
					break;

					
					
					
				case "Frances":

					if (i == 1) aux.setValorBono(valorNominal);

				    else aux.setValorBono(cuotas.get(i - 1).getValorBono() + cuotacte - interes);
					
					aux.setBonoIndexado(Metodos.redondearDecimales(
							aux.getValorBono()*((tInfPer/100)+1)
							, 2));
					
					interes = Metodos.redondearDecimales(((tep / 100) * -1 * aux.getBonoIndexado()), 2);
					
					aux.setCuota(cuotacte);
					
					break;
				}
				
				aux.setCuota(Metodos.redondearDecimales(aux.getCuota(), 2));

				if (metodo != "Americano")
					aux.setEscudo(Metodos.redondearDecimales(interes * 0.3, 2));
				
				
				
				if (i != totalPeriodos)
					aux.setFlujoEmisor(aux.getCuota());
			
				else {
					Double prima = aux.getValorBono() * (porcentajePrima / 100);
					aux.setFlujoEmisor(aux.getCuota() - prima);
				}
				
				
				
				//REDONDEO DE VALORES Y SE ASIGNAN MONTOS A LOS FLUJOS 

				aux.setValorBono(Metodos.redondearDecimales(aux.getValorBono(), 2));

				aux.setFlujoEmisor(Metodos.redondearDecimales(aux.getFlujoEmisor(), 2));

				aux.setFlujoEmisorConEscudo(Metodos.redondearDecimales((aux.getFlujoEmisor() - aux.getEscudo()), 2));

				aux.setFlujoBonista(Metodos.redondearDecimales((aux.getFlujoEmisor() * -1), 2));

			}

			cuotas.add(aux);
		}
		
		van = Metodos.hallarVAN(cuotas, tasaPeriodo, 1);
		va = van - cuotas.get(0).getFlujoBonista();
		
		Double tirBonista=  Metodos.hallarTirBonista(cuotas, tasaPeriodo);
		Double tirEmisor = Metodos.hallarTirEmisor(cuotas, tasaPeriodo);

		Double tirEmisorEsc = Metodos.hallarTirEmisorEscudo(cuotas, tasaPeriodo);

		
		treaBonista =  (Math.pow(tirBonista/100 +1 , periodosPorAño)-1)*100;
		tceaEmisor = (Math.pow(tirEmisor/100 +1 , periodosPorAño)-1)*100;
		tceaEmisorConEscudo = (Math.pow(tirEmisorEsc/100 +1 , periodosPorAño)-1)*100;
		
		va = Metodos.redondearDecimales(va, 2);
		tceaEmisor = Metodos.redondearDecimales(tceaEmisor, 7);
		tceaEmisorConEscudo = Metodos.redondearDecimales(tceaEmisorConEscudo,7);	
		van = Metodos.redondearDecimales(van, 2);
		treaBonista = Metodos.redondearDecimales(treaBonista,6);

		

		// CalculoFinal();
	}

}
