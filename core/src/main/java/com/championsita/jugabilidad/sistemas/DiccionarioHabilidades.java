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

        ModificadorHabilidad empujon = new ModificadorHabilidad();
        empujon.consumoSprint *= 1.25f;
        diccionario.put(HabilidadesEspeciales.EMPUJON, empujon);

        ModificadorHabilidad zurdo = new ModificadorHabilidad();
        zurdo.recargaStamina *= 0.75f;
        diccionario.put(HabilidadesEspeciales.ZURDO, zurdo);

        ModificadorHabilidad diestro = new ModificadorHabilidad();
        zurdo.recargaStamina *= 0.75f;
        diccionario.put(HabilidadesEspeciales.DIESTRO, diestro);

        ModificadorHabilidad atleta = new ModificadorHabilidad();
        atleta.staminaMax *= 1.25f;
        atleta.recargaStamina *= 1.25f;
        atleta.escalaSprite *= 1.15f;
        diccionario.put(HabilidadesEspeciales.ATLETA, atleta);

        ModificadorHabilidad extremista = new ModificadorHabilidad();
        extremista.buffPostGol = 1.5f;
        extremista.penalizacionPostGol = 1.5f;
        diccionario.put(HabilidadesEspeciales.EXTREMISTA, extremista);

    }

    public static ModificadorHabilidad obtener(HabilidadesEspeciales h) {
        return diccionario.get(h);
    }
}
