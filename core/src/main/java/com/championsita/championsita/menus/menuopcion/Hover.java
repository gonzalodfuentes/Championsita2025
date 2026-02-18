package com.championsita.menus.menuopcion;

import com.badlogic.gdx.graphics.Color;

public enum Hover {

    VERDE(new Color(0f, 1f, 0f, 1f)),
    AZUL(new Color(0f, 0f, 1f, 1f)),
    ROJO(new Color(1f, 0f, 0f, 1f)),
    AMARILLO(new Color(1f, 1f, 0f, 1f)),
    CELESTE(new Color(0.4f, 0.8f, 1f, 1f)),
    NARANJA(new Color(1f, 0.5f, 0f, 1f)),
    OSCURECIDO(new Color(0.7f, 0.7f, 0.7f, 1f)),
    BLANCO(new Color(1f, 1f, 1f, 0.7f)),
    LIMA(new Color(0.75f, 1f, 0f, 1f)),
    VIOLETA(new Color(0.5f, 0f, 0.5f, 1f)),
    ROSA(new Color(1f, 0.4f, 0.7f, 1f)),
    MARRON(new Color(0.6f, 0.3f, 0.1f, 1f)),
    NADA(new Color(1, 1, 1, 1));

    private Color color;

    private Hover(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }
}
