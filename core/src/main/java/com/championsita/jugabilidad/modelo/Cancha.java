package com.championsita.jugabilidad.modelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.jugabilidad.constantes.Constantes;

public class Cancha {   //La idea es que sea abstracta para las distintas canchas y modos

    private Texture texturaCancha;

    private Arco arcoIzquierdo;
    private Arco arcoDerecho;

    private float anchoArco;
    private float altoArco;
    private float yCentro;

    public Cancha(float anchoArco, float altoArco, Texture pngCancha) {
        this.texturaCancha = pngCancha;

        this.anchoArco = anchoArco;
        this.altoArco = altoArco;

        // Qué tanto sobresalen hacia afuera
        float desplazamientoHorizontal = 0.3f;  // ajustá este valor a gusto

        // Posiciones horizontales
        float posicionArcoIzquierdoX = -desplazamientoHorizontal;
        float posicionArcoDerechoX   = Constantes.MUNDO_ANCHO - anchoArco + desplazamientoHorizontal;

        // Posición vertical centrada
        float posicionArcoY = (Constantes.MUNDO_ALTO / 2f) - (altoArco / 2f);

        // Crear arcos
        arcoIzquierdo = new Arco(posicionArcoIzquierdoX, posicionArcoY, anchoArco, altoArco);
        arcoDerecho   = new Arco(posicionArcoDerechoX, posicionArcoY, anchoArco, altoArco);
    }

    public void dibujarCancha(SpriteBatch batch, FitViewport vistaAjustada) {

        batch.draw(texturaCancha,
                0,
                0,
                vistaAjustada.getWorldWidth(),
                vistaAjustada.getWorldHeight());



    }

    public void dispose() {
        if (texturaCancha != null) texturaCancha.dispose();
    }

    public Arco getArcoIzquierdo() {
        return arcoIzquierdo;
    }

    public Arco getArcoDerecho() {
        return arcoDerecho;
    }
}
