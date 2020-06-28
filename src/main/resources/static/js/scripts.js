//USANDO FINANCEJS LIBRARY 
//Finance.js
//For more information, visit http://financejs.org
//Copyright 2014 - 2015 Essam Al Joubori, MIT license

// Instantiate a Finance class
var Finance = function() {};
// Internal Rate of Return (IRR)
Finance.prototype.IRR = function(cfs) {
	var depth = cfs.depth;
	var args = cfs.cashFlow;
	var numberOfTries = 1;
	// Cash flow values must contain at least one positive value and one negative value
	var positive, negative;
	Array.prototype.slice.call(args).forEach(function (value) {
	  if (value > 0) positive = true;
	  if (value < 0) negative = true;
	})
	if (!positive || !negative) throw new Error('IRR requires at least one positive value and one negative value');
	function npv(rate) {
	  numberOfTries++;
	  if (numberOfTries > depth) {
		throw new Error('IRR can\'t find a result');
	  }
	  var rrate = (1 + rate/100);
	  var npv = args[0];
	  for (var i = 1; i < args.length; i++) {
		npv += (args[i] / Math.pow(rrate, i));
	  }
	  return npv;
	}
	return Math.round(seekZero(npv) * 100) / 100;
  };



function cambia_bandera() {
	switch (document.getElementById("moneda").value) {
	case "1":
		document.getElementById("bandera").src = "/img/banderaperu.png";
		document.getElementById("simbolo").innerHTML = "PEN";
		break;
	case "2":
		document.getElementById("bandera").src = "/img/banderausa.png";
		document.getElementById("simbolo").innerHTML = "USD";
		break;
	case "3":
		document.getElementById("bandera").src = "/img/banderaue.png";
		document.getElementById("simbolo").innerHTML = "EUR";
		break;
	}
}

function redondear(numero, decimales){
	numero=Math.round(numero*(10*decimales));
	return numero/(10*decimales);
}

function calcular_tea(){
	
	var tna=(document.getElementById("tna").value)/100;
	var periodos=360/document.getElementById("dias_cap").value;
	var tea=(((1+tna/periodos)**periodos)-1)*100;
	
	//MÉTODO PARA REDONDEAR A 7 DECIMALES
	tea = Math.round(tea*10000000);
	tea /= 10000000;
	
	document.getElementById("tea").value=tea;

	
}
function calcular_periodos(){
	//periodos por año
 //ppa = 360/frecuencia de pago
	document.getElementById("ppa").value= 360/document.getElementById("fdp").value;

//totalperiodos
 //tper = ppa*años de pago 
	document.getElementById("tper").value= document.getElementById("ppa").value*
	document.getElementById("adp").value;
	calcular_tperiodos();

}

function calcular_tperiodos(){
	//tep = (1+tea)^(frecuenciadepago/360) - 1
	var fdp=document.getElementById("fdp").value;
	var tea=document.getElementById("tea").value;
	var tep = (((1+(tea/100))**(fdp/360))-1)*100;

	//tper= (1+tdescuento)^(frecuenciadepago/360) -1;
	var tdesc=document.getElementById("tdesc").value;
	var tper=(((1+(tdesc/100))**(fdp/360))-1)*100;
	

	//MÉTODO PARA REDONDEAR A 7 DECIMALES
	tep = Math.round(tep*10000000);
	tep /= 10000000;

	tper=Math.round(tper*10000000);
	tper /= 10000000;


   
	document.getElementById("tep").value=tep;
	document.getElementById("tperiod").value=tper;

	calcula_escudo();
}

function calcula_escudo(){

	var escudo= document.getElementById("vnom").value*document.getElementById("tep").value*0.003;

	escudo=Math.round(escudo*100);
	escudo/=100;

	document.getElementById("escudo").value=escudo;
}

function cambia_tasa(){
	switch (document.getElementById("tipoDeTasa").value){
	case "1":
		document.getElementById("tea").readOnly=false;

		document.getElementById("capitalizacion").style.display="none";
		document.getElementById("tasanominal").style.display="none";
		
		break;
	case "2":
		
		document.getElementById("tea").readOnly=true;

		document.getElementById("capitalizacion").style.display="block";
		
		document.getElementById("tasanominal").style.display="block";
		

		break;
	}
}



function resultados(){
	

}


