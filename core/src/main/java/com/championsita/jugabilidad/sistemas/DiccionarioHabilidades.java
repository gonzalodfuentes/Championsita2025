package com.championsita.jugabilidad.sistemas;

import com.championsita.jugabilidad.modelo.HabilidadesEspeciales;

import java.util.HashMap;

public class DiccionarioHabilidades {
    private static final HashMap<HabilidadesEspeciales, ModificadorHabilidad> diccionario = new HashMap<>();

    static {
        ModificadorHabilidad neutro = new ModificadorHabilidad();
        diccionario.put(HabilidadesEspeciales.NEUTRO, neutro);

        ModificadorHabilidad grandote = new ModificadorHabilidad();
        grandote.escalaSprite = 1.3f;
        grandote.velocidadBase = 0.75f;
        grandote.velocidadSprint = 0.75f;
        diccionario.put(HabilidadesEspeciales.GRANDOTE, grandote);

        ModificadorHabilidad pequenin = new ModificadorHabilidad();
        pequenin.escalaSprite = 0.7f;
        pequenin.velocidadBase = 1.3f;
        pequenin.velocidadSprint = 1.3f;
        pequenin.consumoSprint = 1.0f; // normal
        diccionario.put(HabilidadesEspeciales.PEQUEÑIN, pequenin);

        // etc. para todas las habilidades
    }

    public static ModificadorHabilidad obtener(HabilidadesEspeciales h) {
        return diccionario.get(h);
    }
}
