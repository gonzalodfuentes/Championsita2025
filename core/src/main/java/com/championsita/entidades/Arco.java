package com.championsita.entidades;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class Arco {

    private Rectangle arcoSize;
    private float grosorPalo = 0.15f; // grosor de los palos

    public Arco(float x, float y, float ancho, float alto) {
        arcoSize = new Rectangle(x, y, ancho, alto);
    }

    public void dibujar(ShapeRenderer shape) {

        shape.rect(arcoSize.x, arcoSize.y, arcoSize.width, arcoSize.height);


    }

    public Rectangle getHitbox() {
        return arcoSize;
    }

    public float getX() { return arcoSize.getX(); }
    public float getY() { return arcoSize.getY(); }
    public float getWidth() { return arcoSize.getWidth(); }
    public float getHeight() { return arcoSize.getHeight(); }
}
