package com.championsita.menus.menuopcion;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.menus.compartido.Assets;

public enum Nivel {

    MAXIMO(0.11f, Assets.tex("opcion/volumen/vol120.png")),
    CIEN(0.09f, Assets.tex("opcion/volumen/vol100.png")),
    ALTO(0.07f, Assets.tex("opcion/volumen/vol80.png")),
    MODERADO(0.05f, Assets.tex("opcion/volumen/vol60.png")),
    BAJO(0.03f, Assets.tex("opcion/volumen/vol40.png")),
    MINIMO(0.01f, Assets.tex("opcion/volumen/vol20.png")),
    MUTE(0, Assets.tex("opcion/volumen/vol0.png"));

    private float volumen;
    private Texture textura;

    private Nivel(float volumen, Texture textura) {
        this.volumen = volumen;
        this.textura = textura;
    }

    public float getVolumen() {
        return this.volumen;
    }

    public Texture getTextura() {
        return this.textura;
    }
}