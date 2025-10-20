package com.championsita.jugabilidad.modelo;

import com.badlogic.gdx.math.Rectangle;

public class Arco {

    private Rectangle arcoSize;

    public Arco(float x, float y, float ancho, float alto) {
        arcoSize = new Rectangle(x, y, ancho, alto);
    }

    public Rectangle getHitbox() {
        return arcoSize;
    }

    public float getX() {
        return arcoSize.getX();
    }
    public float getY() {
        return arcoSize.getY();
    }
    public float getWidth() {
        return arcoSize.getWidth();
    }
    public float getHeight() {
        return arcoSize.getHeight();
    }





}