package com.championsita.name.personajes;

import com.championsita.name.Personaje;
import com.championsita.name.PersonajeConfig;

public class Normal extends Personaje {

    // Ajustá los valores a gusto
    public Normal(String nombre, PersonajeConfig config, float escala,
                  float velocidadBase, float velocidadSprint, float staminaMax) {
        super(nombre, config, escala, velocidadBase, velocidadSprint, staminaMax);
    }

    // Atajo cómodo si querés usar un nombre por defecto
    public Normal(PersonajeConfig config, float escala,
                  float velocidadBase, float velocidadSprint, float staminaMax) {
        this("Normal", config, escala, velocidadBase, velocidadSprint, staminaMax);
    }
}
