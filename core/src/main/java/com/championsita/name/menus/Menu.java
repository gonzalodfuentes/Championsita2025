package com.championsita.name.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.name.Principal;

public class Menu extends InputAdapter implements Screen {

    protected Principal juego;
    protected int altoPantalla;
    protected int anchoPantalla;
    protected SpriteBatch batch;
    private Texture fondo;
    protected Sprite fondoSprite;
    protected Texture atras;
    protected Sprite atrasSprite;
    protected Color colorBoton;
    protected Texture flecha, flechaCursor;
    protected Sprite[] flechas, flechasInvertidas;

    public Menu(Principal juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        this.batch = this.juego.getBatch();

        this.anchoPantalla = 1024;
        this.altoPantalla = 768;

        this.atras = new Texture("atrasBoton.png");
        this.atrasSprite = new Sprite(this.atras);

        this.colorBoton = new Color(this.atrasSprite.getColor());

        this.fondo = new Texture("fondoChampionsita.png");
        this.fondoSprite = new Sprite(this.fondo);
        this.fondoSprite.setSize(this.anchoPantalla, this.altoPantalla);
    }

    protected void crearFlechas(int cantFlechas) {
        this.flecha = new Texture("menuDosJugadores/flechaNormal.png");
        this.flechaCursor = new Texture("menuDosJugadores/flechaInvertida.png");
        this.flechas = new Sprite[cantFlechas];
        this.flechasInvertidas = new Sprite[cantFlechas];

        for(int i = 0; i < cantFlechas; i++) {
            this.flechas[i] = new Sprite(this.flecha);
            this.flechasInvertidas[i] = new Sprite(this.flechaCursor);
            this.flechasInvertidas[i].setSize(this.flechas[i].getWidth(), this.flechas[i].getHeight());
            if(i % 2 != 0) {
                this.flechas[i].setRotation(180);
                this.flechasInvertidas[i].setRotation(180);
            }
        }
    }

    protected void crearAtras(float ancho, float alto, float x, float y) {
        this.atrasSprite.setSize(ancho, alto);
        this.atrasSprite.setPosition(x, y);
    }

    @Override
    public void render(float delta) {
        this.fondoSprite.draw(this.batch);
    }

    protected void cargarAtras() {
        this.atrasSprite.draw(this.batch);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        this.fondo.dispose();
    }

    protected boolean condicionAtras(int x, int y, Sprite sprite, boolean click) {
        boolean dentro = x >= sprite.getX() && x <= sprite.getX() + sprite.getWidth() &&
                y >= sprite.getY() && y <= sprite.getY() + sprite.getHeight() ? true : false;

        if(!click) {
            if(dentro) {
                this.atrasSprite.setColor(0, 1, 0, 1);
            }
            else {
                this.atrasSprite.setColor(this.colorBoton);
            }
        }

        return dentro;
    }

    protected boolean condicionFlechas(Sprite flecha, int x, int y) {
        float fx = flecha.getX();
        float fy = flecha.getY();
        float fAnc = flecha.getWidth();
        float fAlt = flecha.getHeight();

        boolean dentro = x >= fx && x <= fx + fAnc &&
                y >= fy && y <= fy + fAlt ? true : false;

        if(dentro) {
            flecha.setTexture(this.flechaCursor);
        } else {
            flecha.setTexture(this.flecha);
        }

        return dentro;
    }

}






