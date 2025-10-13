package com.championsita.menus.menucarga;

public enum Campo {

	VERDE("Verde"),
	VERDE_OSCURO("VerdeOscuro"),
	NARANJA("Naranja"),
	AZUL("Azul");
	
	private String nombre;

	private Campo(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}
}
