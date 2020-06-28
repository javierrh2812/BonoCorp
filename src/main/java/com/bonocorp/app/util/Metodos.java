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

    public static Double hallarVan(List<Cuota> cuotas, Double tasa) {
        Double van = 0.0;
        for (Cuota cuota : cuotas) {
            if (cuota.getNumeroCuota() == 0)
                van += cuota.getFlujoEmisor();
            else {
                van += cuota.getFlujoEmisor() / (Math.pow(tasa + 1, cuota.getNumeroCuota()));
            }
        }
        return van;
    }

    public static Double hallarTir(List<Cuota> cuotas) {

        // metodo tomado de:
        // https://www.cesarcastillolopez.com/2016/12/calculo-manual-de-la-tir.html
        Double tir = 0.0, inicial = 0.0, sumflujo = 0.0, numexp = 0.0, denexp = 0.0;

        // numexp = f1/1 + f2/2 + ... fn/n
        // denexp = f1*1 + f2*2 + ... fn*n

        // aproximaciones por defecto y por exceso
        Double adef = 0.0;

        // adef=(sumflujo/inicial)^(sumflujo/denexp) -1
        System.out.println("IMPRIMIENDO CUOTAS ENVIDADAS");
        System.out.println(cuotas.toString());
        // metodo de aproximacion por defecto
        for (Cuota cuota : cuotas) {
            if (cuota.getNumeroCuota() == 0)
                inicial = cuota.getFlujoBonista();
            else {
                sumflujo += cuota.getFlujoBonista();
                denexp += cuota.getFlujoBonista() / cuota.getNumeroCuota() * 1.0;
            }
        }
        System.out.println("inicial: "+inicial);
        System.out.println("sumflujo: "+sumflujo);
        System.out.println("denexp: "+denexp);

        adef = Math.pow(sumflujo*1.0 / inicial*1.0, sumflujo*1.0 / denexp) - 1;

        adef = Math.ceil(adef) * 1.0; // redondeo hacia arriba

        System.out.println("aproximacion defecto: "+ adef);

        // el rango del valor de la tir es [adef:aexc]
/*
        Double van =0.0;
        for (int i = 1; i <= 3; i++) {
            while (true) {
                van = hallarVan(cuotas, adef);
                System.out.println("van bucle"+ i+ " = "+van);
                if (van < 0) {
                    adef -= 0.1/Math.pow(10, i);
                    System.out.println("van negativo: "+van);
                    break;
                }
                adef += 0.1/Math.pow(10, i);
                System.out.println("tir defecto: "+ adef);
            }
        }
*/

        return adef;
    }

}
