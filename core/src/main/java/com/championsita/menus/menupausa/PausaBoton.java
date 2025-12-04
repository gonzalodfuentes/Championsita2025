package com.championsita.menus.menupausa;

import com.badlogic.gdx.graphics.Texture;
import com.championsita.menus.compartido.Assets;

public enum PausaBoton {

    CONTINUAR(Assets.tex("pausa/continuar.png")),
    SALIR(Assets.tex("pausa/salir.png"));

    private Texture textura;

    private PausaBoton(Texture textura) {
        this.textura = textura;
    }

    public Texture getTextura() {
        return this.textura;
    }
}
