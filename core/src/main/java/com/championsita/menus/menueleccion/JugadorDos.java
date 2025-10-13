package com.championsita.menus.menueleccion;

public enum JugadorDos {

    ROJO("Rojo"),
    VERDE("Verde");

    private String nombre;

    private JugadorDos(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }
}
