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


function calcular_tea(){
	
	var tna=(document.getElementById("tna").value)/100;
	var periodos=360/document.getElementById("dias_cap").value;
	var tea=(((1+tna/periodos)**periodos)-1)*100;
	
	//MÉTODO PARA REDONDEAR A 7 DECIMALES
	tea = Math.round(tea*10000000);
	tea /= 10000000;
	
	document.getElementById("tea").value=tea;

	
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




