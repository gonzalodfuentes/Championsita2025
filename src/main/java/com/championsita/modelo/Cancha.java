package com.championsita.modelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.constantes.Constantes;

public class Cancha {   //La idea es que sea abstracta para las distintas canchas y modos

    private Texture texturaCancha;

    private Arco arcoIzquierdo;
    private Arco arcoDerecho;

    private float anchoArco;
    private float altoArco;
    private float yCentro;

    public Cancha(float anchoArco, float altoArco) {
        texturaCancha = new Texture("CampoDeJuego.png");

        this.anchoArco = anchoArco;
        this.altoArco = altoArco;

        float ladoIzquierda = 0f;
        float ladoDerecho =  Constantes.MUNDO_ANCHO - anchoArco;


        float xCentro = (Constantes.MUNDO_ANCHO / 2f) - (anchoArco/ 2f);
        float yCentro = (Constantes.MUNDO_ALTO / 2f) - (altoArco/ 2f);


        arcoIzquierdo = new Arco(ladoIzquierda,yCentro,anchoArco,altoArco);
        arcoDerecho = new Arco(ladoDerecho,yCentro,anchoArco,altoArco);

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
