package com.bonocorp.app.util;

import java.util.List;

import com.bonocorp.app.model.Cuota;

public class Metodos {
	public static double redondearDecimales(double valorInicial, int numeroDecimales) {
		double parteEntera, resultado;
		resultado = valorInicial;
		parteEntera = Math.floor(resultado);
		resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
		resultado = Math.round(resultado);
		resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
		return resultado;
	}

	public static Double hallarVNA(List<Cuota> cuotas, Double tasa) {
		Double van = 0.0;
		for (Cuota cuota : cuotas) {
			if (cuota.getNumeroCuota() != 0)
				van += cuota.getFlujoBonista() / (Math.pow(tasa / 100 + 1, cuota.getNumeroCuota()));
		}
		return redondearDecimales(van, 2);
	}

	public static Double utilidad(List<Cuota> cuotas, Double tasa) {
		Double utilidad = hallarVNA(cuotas, tasa) + cuotas.get(0).getFlujoBonista();

		return utilidad;
	}

	public static Double hallarVAN(List<Cuota> cuotas, Double tasa, Integer tipo) {
		Double van = 0.0;
		for (Cuota cuota : cuotas) {

			switch (tipo) {
			case 1:
				van += cuota.getFlujoBonista() / (Math.pow(tasa / 100 + 1, cuota.getNumeroCuota()));
				break;
			case 2:
				van += cuota.getFlujoEmisor() / (Math.pow(tasa / 100 + 1, cuota.getNumeroCuota()));
				break;
			case 3:
				van += cuota.getFlujoEmisorConEscudo() / (Math.pow(tasa / 100 + 1, cuota.getNumeroCuota()));
				break;
			}
		}
		return van;

	}

	public static Double hallarTirBonista(List<Cuota> cuotas, Double cok) {

		Double tir = cok;
		Boolean positivo;
		Double utilidad = hallarVAN(cuotas, tir, 1);
		if (utilidad > 0) positivo = true;
		else positivo = false;
		for (int i = 1; i < 6; i++) {
			
			if (positivo) {
				while (utilidad > 0) {
					//System.out.println("utilidad posi: "+utilidad+", tir: "+tir);
					tir += 1 / (Math.pow(10, i));
					utilidad = hallarVAN(cuotas, tir, 1);
				}
			} else {
				while (utilidad < 0) {
					//System.out.println("utilidad nega: "+utilidad+", tir: "+tir);
					tir -= 1 / (Math.pow(10, i));
					utilidad = hallarVAN(cuotas, tir, 1);
				}
			}
			positivo = !positivo;

		}

		return tir;
	}

	public static Double hallarTirEmisor(List<Cuota> cuotas, Double cok) {

		Double tir = cok;
		Boolean positivo;
		Double utilidad = hallarVAN(cuotas, tir, 2);
		if (utilidad > 0) positivo = true;
		else positivo = false;
		for (int i = 1; i < 6; i++) {
			if (positivo) {
				while (utilidad > 0) {
					tir -= 1 / (Math.pow(10, i));
					utilidad = hallarVAN(cuotas, tir, 2);
				}
			} else {
				while (utilidad < 0) {
					tir += 1 / (Math.pow(10, i));
					utilidad = hallarVAN(cuotas, tir, 2);				}
			}
			positivo = !positivo;

		}

		return tir;
	}

	public static Double hallarTirEmisorEscudo(List<Cuota> cuotas, Double cok) {

		Double tir = cok;
		Boolean positivo;
		Double utilidad = hallarVAN(cuotas, tir, 3);
		if (utilidad > 0) positivo = true;
		else positivo = false;
		for (int i = 1; i < 6; i++) {

			if (positivo) {
				while (utilidad > 0) {
					tir -= 1 / (Math.pow(10, i));
					utilidad = hallarVAN(cuotas, tir, 3);
				}
			} else {
				while (utilidad < 0) {
					tir += 1 / (Math.pow(10, i));
					utilidad = hallarVAN(cuotas, tir, 3);
				}
			}
			positivo = !positivo;

		}

		return tir;
	}

}
