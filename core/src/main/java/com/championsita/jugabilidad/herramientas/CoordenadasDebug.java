package com.championsita.jugabilidad.herramientas;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.championsita.jugabilidad.constantes.Constantes;

public class CoordenadasDebug extends ApplicationAdapter {

    private Texto texto;

    public CoordenadasDebug() {

        this.texto = new Texto(Constantes.fuente2, 30, Color.YELLOW);
        texto.setPosition(10, Gdx.graphics.getHeight() - 10);

    }

    public void render(SpriteBatch batch) {

        // Obtener la posición del cursor
        int cursorX = Gdx.input.getX();
        int cursorY = Gdx.input.getY();

        // Crear la cadena de texto a mostrar
        String coords = "X: " + cursorX + ", Y: " + cursorY;

        texto.setTexto("X: " + cursorX + ", Y: " + cursorY);
        texto.dibujar(batch);

    }

    public void dispose(SpriteBatch batch) {
        batch.dispose();
    }
}