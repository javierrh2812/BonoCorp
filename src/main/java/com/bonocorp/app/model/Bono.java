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
	
	//1- sol, 2-dolar, 3-euro
	@Column(name="tipo_moneda")
	private Integer tipoMoneda;
	
	
	@NotNull(message="Debe ingresar los años de pago")
	@Min(value = 1, message = "Ingrese un valor positivo")
	@Column(name = "años_de_pago")
	private Integer añosDePago;
	

	@NotNull(message="Debe elegir el periodo de pago")
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
	@Column(name = "escudo")
	private Double escudo;

	// MÉTODO
	
	@Column(name = "metodo", nullable = false)
	private String metodo;

	// USUARIO DEL BONO
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	// COUTAS, CUPONES
	//cascadetype.all para persistir la lista 
	//orphan removal para eliminar los elementos anteriores 
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "bono_id")
	private List<Cuota> cuotas = new ArrayList<Cuota>();

	public void CalcularDatos() {
		
		this.valorNominal *=100;
		this.valorNominal /=100;

		periodosPorAño = diasPorAño / diasPorPeriodo;
		
		totalPeriodos = periodosPorAño * añosDePago;
		
		//tep = (1+tea)^(diaperiodo/360) - 1
		tep = Math.pow(1 + tea / 100, 1.00 * diasPorPeriodo / diasPorAño) - 1;
		//se divide y se multiplican los porcentajes por 100 porque
		//el usuario ingresa el valor porcentual
		tep *= 100;
		
		//tep = (1+teaDescuento)^(diaperiodo/360) - 1
		tasaPeriodo = Math.pow(1 + tasaDescuento / 100, diasPorPeriodo / diasPorAño) - 1;
		tasaPeriodo *= 100;

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
	
	public double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }

	public void CalcularFlujo() {
		
		cuotas.clear();
		//se inicializa la cuota
		
		//escudo = cupon*%irenta
		escudo = valorNominal * tep * impuestoRenta / 10000;
		
		
		//desde el periodo 0, hasta el total calculado
		for (int i = 0; i <= totalPeriodos; i++) {
			
			Cuota aux = new Cuota();

			aux.setNumeroCuota(i);
			if (i == 0) {
				aux.setInflacionPeriodo(0.0);
				double suma1 = porcentajeCavali + porcentajeColocacion +
						porcentajeEstructuracion + porcentajeFlotacion;
				suma1 /= 100;
				aux.setFlujoEmisor(valorComercial * (1 - suma1));
				aux.setFlujoEmisorConEscudo(aux.getFlujoEmisor());
				double suma2 = porcentajeFlotacion + porcentajeCavali;
				suma2 /= 100;
				aux.setFlujoBonista(-1 * valorComercial * (1 + suma2));

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
		
		tep = Metodos.redondearDecimales(tep, 7);
	}
}
