package com.championsita.modelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.constantes.Constantes;

public class Cancha {   //La idea es que sea abstracta para las distintas canchas y modos

    private Texture texturaCancha;
    private Arco arco;
    private float anchoCancha;
    private float altoCancha;
    private float yCentro;
    private float ladoIzquierda = 0f;
    private float ladoDerecho;

    public Cancha(float anchoCancha, float altoCancha) {
        texturaCancha = new Texture("CampoDeJuego.png");

        this.anchoCancha = anchoCancha;
        this.altoCancha = altoCancha;

        float xCentro = (Constantes.MUNDO_ANCHO / 2f) - (anchoCancha/ 2f);
        float yCentro = (Constantes.MUNDO_ALTO / 2f) - (altoCancha/ 2f);

        arco = new Arco(ladoIzquierda,yCentro,anchoCancha,altoCancha);

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

    public Arco getArco() {
        return arco;
    }
}
