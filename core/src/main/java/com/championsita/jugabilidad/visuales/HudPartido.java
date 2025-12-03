package com.championsita.jugabilidad.visuales;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.championsita.jugabilidad.constantes.Constantes;
import com.championsita.jugabilidad.herramientas.Texto;
import com.championsita.jugabilidad.sistemas.SistemaPartido;

public class HudPartido {

    private int posYContador = 750;
    private Viewport viewPort;
    private Texto texto;

    public HudPartido(Viewport viewport) {
        this.viewPort = viewport;
        this.texto = new Texto(
                Constantes.fuente1,
                32,
                Color.WHITE,
                0.5f,
                Color.BLACK
        );
    }

    public void dibujarHud(SpriteBatch batch, SistemaPartido sistemaPartido) {

        int puntajeEquipo1 = sistemaPartido.getNotadorEquipo1();
        int puntajeEquipo2 = sistemaPartido.getNotadorEquipo2();

        int x = Gdx.graphics.getWidth()/2 - (int)(texto.getAncho()/2);
        int y = Gdx.graphics.getHeight() - 40;

        texto.setPosition(x, y);

        texto.setTexto(puntajeEquipo1 + "-" + puntajeEquipo2);

        texto.dibujar(batch);





    }










}
