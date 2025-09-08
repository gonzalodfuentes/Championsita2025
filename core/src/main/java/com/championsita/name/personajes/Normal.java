package com.championsita.name.personajes;

import com.championsita.name.Personaje;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Normal extends Personaje {

    public Normal() {
        // Aquí defines nombre, escala, velocidad al crear el personaje
        super("Normal", 0.003f, 1.5f, 3f, 20f);

        // Puedes personalizar texturas y animaciones distintas,
        // pero en tu clase Personaje actual cargas siempre las mismas.
        // Si quieres personalizarlas, haz métodos abstractos para eso o sobreescribe.

        // Por ejemplo, si quieres cambiar texturaQuieto y animaciones,
        // agrega métodos protegidos para modificarlas o carga aquí texturas adicionales.
    }
}

