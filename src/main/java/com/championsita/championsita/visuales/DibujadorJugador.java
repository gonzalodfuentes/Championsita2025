package com.championsita.visuales;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.championsita.modelo.Personaje;

public class DibujadorJugador {

    private final Personaje personaje;

    public DibujadorJugador(Personaje personaje) {
        this.personaje = personaje;
    }

    public void dibujar(SpriteBatch batch) {
        // 1) pedir frame actual al modelo
        TextureRegion frame = personaje.obtenerFrameActual();

        // 2) dibujar sprite
        batch.draw(frame, personaje.getX(), personaje.getY(), personaje.getAncho(), personaje.getAlto());

        // 3) dibujar HUD del personaje (si existe)
        if (personaje.getHud() != null) {
            personaje.getHud().dibujarBarraStamina(batch, personaje.getX(), personaje.getY());
        }
    }
}
