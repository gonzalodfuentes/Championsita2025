package com.championsita.menus.menuprincipal;

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
import com.championsita.menus.compartido.Assets;

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

        // Assets compartidos
        this.atras = Assets.tex("atrasBoton.png");
        this.siguiente = Assets.tex("menuDosJugadores/okBoton.png");
        this.fondo = Assets.tex("fondoChampionsita.png");
        this.musica = Assets.music("futebol.mp3");
        this.sonido = Assets.sfx("cursor.mp3");

        this.atrasSprite = new Sprite(this.atras);
        this.atrasSprite.setSize(140, 70);
        this.atrasSprite.setPosition(15, 670);

        this.colorBoton = new Color(this.atrasSprite.getColor());
        this.colorAccion = new Color(0, 1, 0, 1);

        this.siguienteSprite = new Sprite(this.siguiente);
        this.siguienteSprite.setSize(140, 70);
        this.siguienteSprite.setPosition(Gdx.graphics.getWidth() - this.siguienteSprite.getWidth() - 15, 670);

        this.fondoSprite = new Sprite(this.fondo);
        this.fondoSprite.setSize(this.anchoPantalla, this.altoPantalla);

        // Música
        this.musica.setVolume(0.03f);
        this.musica.setLooping(true);
        if (!this.musica.isPlaying()) this.musica.play();

        int cantBotonesGlobales = 2;
        inicializarSonido(cantBotonesGlobales);
    }

    protected void inicializarSonido(int cantBotones) {
        this.cursorSonido = new boolean[cantBotones];
        for (int i = 0; i < this.cursorSonido.length; i++) this.cursorSonido[i] = false;
    }

    protected void reproducirSonido(int i, boolean dentro) {
        if (dentro) {
            if (!this.cursorSonido[i]) {
                this.sonido.play(0.5f);
                this.cursorSonido[i] = true;
            }
        } else {
            this.cursorSonido[i] = false;
        }
    }

    protected void crearFlechas(int cantFlechas) {
        this.flecha = Assets.tex("menuDosJugadores/flechaNormal.png");
        this.flechaCursor = Assets.tex("menuDosJugadores/flechaInvertida.png");
        this.flechas = new Sprite[cantFlechas];
        this.flechasInvertidas = new Sprite[cantFlechas];

        for (int i = 0; i < cantFlechas; i++) {
            this.flechas[i] = new Sprite(this.flecha);
            this.flechasInvertidas[i] = new Sprite(this.flechaCursor);
            this.flechasInvertidas[i].setSize(this.flechas[i].getWidth(), this.flechas[i].getHeight());
            if (i % 2 != 0) {
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
        // Nada que disposear acá: lo maneja Assets.
        // Si en algún momento querés liberar todo (salida del juego), llamar a Assets.disposeAll() desde Principal.
    }

    protected void cambiarMenu(boolean dentro, Menu atrasAdelante) {
        if (dentro) {
            this.juego.actualizarPantalla(atrasAdelante);
        }
    }
}
