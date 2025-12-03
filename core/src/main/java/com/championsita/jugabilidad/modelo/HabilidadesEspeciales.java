package com.championsita.jugabilidad.modelo;

public enum HabilidadesEspeciales {
    NEUTRO("Neutro"),
    GRANDOTE("Grandote"),
    PEQUEÑIN("Pequenin"),
    EMPUJON("Empujon"),
    ZURDO("Zurdo"),
    DIESTRO("Diestro"),
    ATLETA("Atleta"),
    EXTREMISTA("Extremista");

    private final String nombre;

    HabilidadesEspeciales(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
