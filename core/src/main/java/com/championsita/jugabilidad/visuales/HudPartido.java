package com.championsita.jugabilidad.visuales;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.jugabilidad.constantes.Constantes;
import com.championsita.jugabilidad.herramientas.Texto;
import com.championsita.jugabilidad.sistemas.SistemaPartido;

public class HudPartido {

    private int posYContador = 750;

    private Texto texto;

    public HudPartido() {

        this.texto = new Texto(
                Constantes.fuente1,
                50,
                Color.WHITE,
                2f,
                Color.BLACK
        );

        texto.setPosition(Gdx.graphics.getWidth()/2 - 47, posYContador);
    }

    public void dibujarHud(SpriteBatch batch, SistemaPartido sistemaPartido) {

        int puntajeEquipo1 = sistemaPartido.getNotadorEquipo1();
        int puntajeEquipo2 = sistemaPartido.getNotadorEquipo2();


        texto.setTexto(String.valueOf(puntajeEquipo1)+ "-" +String.valueOf(puntajeEquipo2));

        texto.dibujar(batch);





    }










}
