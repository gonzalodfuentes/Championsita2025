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

        this.colorBoton = new Color(1, 1, 1, 1);
        this.colorAccion = this.juego.getAccionColor();

        this.siguienteSprite = new Sprite(this.siguiente);
        this.siguienteSprite.setSize(140, 70);
        this.siguienteSprite.setPosition(this.anchoPantalla - this.siguienteSprite.getWidth() - 15, 670);

        this.fondoSprite = new Sprite(this.fondo);
        this.fondoSprite.setSize(this.anchoPantalla, this.altoPantalla);

        // Música

        this.musica.setLooping(true);
        this.musica.setVolume(this.juego.getVolumenMusica());
        if (!this.musica.isPlaying()) this.musica.play();

        int cantBotonesGlobales = 2;
        inicializarSonido(cantBotonesGlobales);
    }

    protected void inicializarSonido(int cantBotones) {
        this.cursorSonido = new boolean[cantBotones];
        for (int i = 0; i < this.cursorSonido.length; i++) this.cursorSonido[i] = false;
    }

    public void reproducirSonido(int i, boolean dentro) {
        if (dentro) {
            if (!this.cursorSonido[i]) {
                this.sonido.play(0.5f);
                this.cursorSonido[i] = true;
            }
        } else {
            this.cursorSonido[i] = false;
        }
    }

    @Override
    public void render(float delta) {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        // Nada que disposear acá: lo maneja Assets.
        // Si en algún momento querés liberar todo (salida del juego), llamar a Assets.disposeAll() desde Principal.
    }

    protected void cambiarMenu(boolean dentro, Menu nuevaPantalla) {
        if (!dentro) return;

        if (this.musica != null && this.musica.isPlaying()) {
            this.musica.stop();
        }

        this.juego.actualizarPantalla(nuevaPantalla);
    }

    public void demostracionTemporalMusica(float volumen) {
        this.musica.setVolume(volumen);
    }
}