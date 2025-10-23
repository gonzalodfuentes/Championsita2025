package com.championsita.menus.menumodosdejuego;

public enum ModoDeJuego {
    PRACTICA("Práctica"),
    DOS_JUGADORES("2 Jugadores"),
    SEIS_JUGADORES("6 Jugadores");

    private final String titulo;
    ModoDeJuego(String t) { this.titulo = t; }
    public String getTitulo() { return titulo; }
}
