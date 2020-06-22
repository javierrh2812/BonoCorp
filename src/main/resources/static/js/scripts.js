

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


