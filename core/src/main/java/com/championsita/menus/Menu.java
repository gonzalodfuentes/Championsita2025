package com.championsita.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.Principal;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

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
    protected Color colorAccion;
    protected Texture flecha, flechaCursor;
    protected Sprite[] flechas, flechasInvertidas;
    private Music musica;
    protected Sound sonido;
    protected boolean[] cursorSonido;
    protected Texture siguiente;
    protected Sprite siguienteSprite;

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
        this.atrasSprite.setSize(140, 70);
        this.atrasSprite.setPosition(15, 670);

        this.colorBoton = new Color(this.atrasSprite.getColor());
        this.colorAccion = new Color(0, 1, 0, 1);

        this.siguiente = new Texture("menuDosJugadores/okBoton.png");
        this.siguienteSprite = new Sprite(this.siguiente);
        this.siguienteSprite.setSize(140, 70);
        this.siguienteSprite.setPosition(Gdx.graphics.getWidth() - this.siguienteSprite.getWidth() - 15, 670);

        this.fondo = new Texture("fondoChampionsita.png");
        this.fondoSprite = new Sprite(this.fondo);
        this.fondoSprite.setSize(this.anchoPantalla, this.altoPantalla);

        this.musica = Gdx.audio.newMusic(Gdx.files.internal("futebol.mp3"));
        this.musica.setVolume(0.03f);
        this.musica.setLooping(true);
        this.musica.play();

        this.sonido = Gdx.audio.newSound(Gdx.files.internal("cursor.mp3"));
        int cantBotonesGlobales = 2;
        inicializarSonido(cantBotonesGlobales);
    }

    protected void inicializarSonido(int cantBotones) {
        this.cursorSonido = new boolean[cantBotones];
        for(int i = 0; i < this.cursorSonido.length; i++) {
            this.cursorSonido[i] = false;
        }
    }

    protected void reproducirSonido(int i, boolean dentro) {
        if(dentro) {
            if(!this.cursorSonido[i]) {
                this.sonido.play(0.5f);
                this.cursorSonido[i] = true;
            }
        }
        else {
            this.cursorSonido[i] = false;
        }
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

    @Override
    public void render(float delta) {
        this.fondoSprite.draw(this.batch);
    }

    protected void cargarAtrasSiguiente() {
        this.atrasSprite.draw(this.batch);
        this.siguienteSprite.draw(this.batch);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        this.fondo.dispose();
        this.musica.dispose();
    }

    protected boolean condicionDentro(int x, int y, Sprite sprite) {
        boolean dentro = x >= sprite.getX() && x <= sprite.getX() + sprite.getWidth() &&
                y >= sprite.getY() && y <= sprite.getY() + sprite.getHeight() ? true : false;

        return dentro;
    }

    protected boolean condicionColor(boolean dentro, Sprite sprite) {
        if(dentro) {
            sprite.setColor(this.colorAccion);
        }
        else {
            sprite.setColor(this.colorBoton);
        }

        return dentro;
    }

    protected void cambiarMenu(boolean dentro, Menu atrasAdelante) {
        if(dentro) {
            this.juego.actualizarPantalla(atrasAdelante);
        }
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






