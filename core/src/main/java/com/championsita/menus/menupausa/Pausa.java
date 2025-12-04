package com.championsita.menus.menupausa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.championsita.Principal;
import com.championsita.menus.local.Local;
import com.championsita.menus.menuopcion.Opcion;
import com.championsita.menus.menuprincipal.GestorInputMenu;
import com.championsita.menus.menuprincipal.Inicial;
import com.championsita.menus.menuprincipal.Menu;
import com.championsita.menus.menuprincipal.RenderizadorDeMenu;
import com.championsita.partida.ControladorDePartida;

public class Pausa extends Menu {

    private ShapeRenderer fondo;
    private boolean pausado;
    private PausaBoton pausa;
    private Sprite[] botones;
    private GestorInputMenu gestorMenu;

    public Pausa(Principal juego) {
        super(juego);
    }

    @Override
    public void show() {
        super.show();
        this.fondo = new ShapeRenderer();
        this.botones = new Sprite[this.pausa.values().length];
        int ancho = this.pausa.values()[0].getTextura().getWidth() - 100;
        int alto = this.pausa.values()[0].getTextura().getHeight() - 20;
        int apilar = 100;
        for(int i = 0; i < this.pausa.values().length; i++) {
            this.botones[i] = new Sprite(this.pausa.values()[i].getTextura());
            this.botones[i].setSize(ancho, alto);
            this.botones[i].setPosition(
                    (Gdx.graphics.getWidth() / 2f - ancho / 2f),
                    (Gdx.graphics.getHeight() / 2f - alto / 2f) + apilar
            );
            apilar -= 200;
        }
        super.inicializarSonido(this.botones.length);
        this.gestorMenu = new GestorInputMenu(this);
    }

    public void renderizar(SpriteBatch batch) {
        if(this.pausado) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            // Usar la misma proyección que el batch
            fondo.setProjectionMatrix(batch.getProjectionMatrix());

            fondo.begin(ShapeRenderer.ShapeType.Filled);
            fondo.setColor(0, 0, 0, 0.5f); // negro con 50% transparencia
            fondo.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            fondo.end();

            batch.begin();
            for(int i = 0; i < this.botones.length; i++) {
                this.botones[i].draw(batch);
            }
            batch.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public boolean keyDown(int key) {
        boolean tecla = false;

        if(key != Input.Keys.ESCAPE && this.pausado) {
            key = 0;
            tecla = true;
        }

        if(key == Input.Keys.ESCAPE) {
            if(this.pausado) {
                this.pausado = false;
            }
            else {
                this.pausado = true;
            }
            tecla = true;
        }

        return tecla;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        boolean hitAlgo = false;

        for(int i = 0; i < this.botones.length; i++) {
            boolean dentro = this.gestorMenu.condicionDentro(x, y, this.botones[i]);
            this.gestorMenu.condicionColor(dentro, this.botones[i]);
            super.reproducirSonido(i, dentro);
            hitAlgo |= dentro;
        }

        return hitAlgo;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        boolean clickAlgo = false;

        for(int i = 0; i < this.botones.length; i++) {
            boolean dentro = this.gestorMenu.condicionDentro(x, y, this.botones[i]);
            if(dentro) {
                cambiarMenu(i);
            }
            clickAlgo |= dentro;
        }

        return clickAlgo;
    }

    private void  cambiarMenu(int i) {
        switch(i) {
            case 0: this.pausado = false; break;
            case 1: super.cambiarMenu(true, new Inicial(super.juego));
        }
    }
}
