package com.championsita.menus.menuprincipal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.championsita.menus.compartido.Assets;

public class RenderizadorDeMenu{

    Menu menu;

    public RenderizadorDeMenu(Menu m){
        this.menu = m;
    }

    public void renderFondo(float delta) {
        menu.fondoSprite.draw(menu.batch);
    }

    public void cargarAtrasSiguiente() {
        menu.atrasSprite.draw(menu.batch);
        menu.siguienteSprite.draw(menu.batch);
    }

    public void crearFlechas(int cantFlechas) {
        menu.flecha = Assets.tex("menuDosJugadores/flechaNormal.png");
        menu.flechaCursor = Assets.tex("menuDosJugadores/flechaInvertida.png");
        menu.flechas = new Sprite[cantFlechas];
        menu.flechasInvertidas = new Sprite[cantFlechas];

        for (int i = 0; i < cantFlechas; i++) {
            menu.flechas[i] = new Sprite(menu.flecha);
            menu.flechasInvertidas[i] = new Sprite(menu.flechaCursor);
            menu.flechasInvertidas[i].setSize(menu.flechas[i].getWidth(), menu.flechas[i].getHeight());
            if (i % 2 != 0) {
                menu.flechas[i].setRotation(180);
                menu.flechasInvertidas[i].setRotation(180);
            }
        }
    }
}
