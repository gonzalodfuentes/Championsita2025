package com.championsita.menus.compartido;

public enum OpcionDeGoles {
    UNO(1),
    TRES(3),
    CINCO(5);
    private final int valor;
    OpcionDeGoles(int v) { this.valor = v; }
    public int getValor() { return valor; }
}
