package com.championsita.menus.compartido;

public enum OpcionDeTiempo {
    CORTO(1),
    MEDIO(2),
    LARGO(3);
    private final int codigo;
    OpcionDeTiempo(int c) { this.codigo = c; }
    public int getCode() { return codigo; }
}
