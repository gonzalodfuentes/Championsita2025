package com.championsita.personajes;

import com.championsita.entidades.Personaje;
import com.championsita.entidades.AtributosPersonaje;
import com.championsita.entidades.ConfiguracionPersonaje;

public class Normal extends Personaje {
    public Normal(String nombre, ConfiguracionPersonaje config, float escala) {
        super(nombre,
              config,
              new AtributosPersonaje(
                  1.0f,  // velocidad base
                  1.8f,  // velocidad sprint
                  100f,  // stamina máxima
                  5f,    // consumo sprint/seg
                  10f,   // recarga stamina/seg
                  0.9f,  // límite bloqueo stamina
                  2f     // segundos de bloqueo
              ),
              escala);
    }
}
