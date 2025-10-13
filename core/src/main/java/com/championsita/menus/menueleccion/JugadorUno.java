package com.championsita.menus.menueleccion;

public enum JugadorUno {

    AMARILLO("Amarillo"),
    CELESTE("Celeste");

    private String nombre;

    private JugadorUno(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }
}
