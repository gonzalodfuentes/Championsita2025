package com.championsita.visuales;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.championsita.entidades.Pelota;

public class DibujadorPelota {

    private final Pelota pelota;

    public DibujadorPelota(Pelota pelota) {
        this.pelota = pelota;
    }

    public void dibujar(SpriteBatch batch) {
        TextureRegion frame = pelota.obtenerFrameActual();
        batch.draw(frame, pelota.getX(), pelota.getY(), pelota.getWidth(), pelota.getHeight());
    }
}
