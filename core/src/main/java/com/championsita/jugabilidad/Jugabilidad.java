package com.championsita.jugabilidad;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.Principal;
import com.championsita.jugabilidad.constantes.Constantes;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.jugabilidad.modelo.ConfiguracionPersonaje;
import com.championsita.jugabilidad.modelo.Pelota;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.jugabilidad.personajes.Normal;
import com.championsita.jugabilidad.visuales.DibujadorJugador;
import com.championsita.jugabilidad.visuales.DibujadorPelota;
import com.championsita.jugabilidad.sistemas.SistemaFisico;
import com.championsita.jugabilidad.sistemas.SistemaColisiones;
import com.championsita.menus.menucarga.Carga;

public class Jugabilidad extends InputAdapter implements Screen {

    private String campoRuta, pielJugUno, pielJugDos;
    private final ConfiguracionPersonaje configJugador1;
    private final ConfiguracionPersonaje configJugador2;

    public Jugabilidad(String campoRuta, String pielJugUno, String pielJugDos) {
        this.campoRuta = campoRuta;
        this.pielJugUno = pielJugUno;
        this.pielJugDos = pielJugDos;

        this.configJugador1 = ConfiguracionPersonaje.porDefecto(pielJugUno);
        this.configJugador2 = ConfiguracionPersonaje.porDefecto(pielJugDos);
    }


    // Render / cámara
    private SpriteBatch pintor;
    private Texture texturaCancha;
    private FitViewport vistaAjustada;

    // Actores
    private Personaje jugador1;
    private Personaje jugador2;
    private Pelota    pelota;

    // Vistas
    private DibujadorJugador dibujadorJugador1;
    private DibujadorJugador dibujadorJugador2;
    private DibujadorPelota  dibujadorPelota;

    // Entrada
    private EntradaJugador entradaJugador1;
    private EntradaJugador entradaJugador2;
    private InputMultiplexer multiplexorEntradas;

    // Sistemas
    private SistemaFisico    sistemaFisico;
    private SistemaColisiones sistemaColisiones;

    @Override
    public void show() {
        inicializarRenderYCamara();
        crearActores();
        configurarEntradas();
        crearSistemas();
    }

    @Override
    public void render(float delta) {

        // 1) Entrada
        entradaJugador1.actualizar(delta);
        entradaJugador2.actualizar(delta);

        // 2) Físicas: personajes (animaciones/estado)
        sistemaFisico.actualizarPersonaje(jugador1, delta);
        sistemaFisico.actualizarPersonaje(jugador2, delta);

        // 3) Físicas: límites del mundo
        float W = vistaAjustada.getWorldWidth();
        float H = vistaAjustada.getWorldHeight();
        sistemaFisico.limitarPersonajeAlMundo(jugador1, W, H);
        sistemaFisico.limitarPersonajeAlMundo(jugador2, W, H);

        // 4) Colisiones: jugador ↔ jugador
        sistemaColisiones.separarJugadoresSiChocan(jugador1, jugador2);

        // 5) Colisiones: jugador ↔ pelota (ambos)
        sistemaColisiones.procesarContactoPelotaConJugador(pelota, jugador1);
        sistemaColisiones.procesarContactoPelotaConJugador(pelota, jugador2);

        // 6) Físicas: pelota (una sola vez)
        sistemaFisico.actualizarPelota(pelota, delta);

        // 7) Dibujo
        dibujarEscena();
    }

    @Override
    public void resize(int ancho, int alto) {
        vistaAjustada.update(ancho, alto, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (pintor != null) pintor.dispose();
        if (texturaCancha != null) texturaCancha.dispose();
        // si tus modelos tienen dispose(), llamalos acá
    }

    // ======================== Privados ========================

    private void inicializarRenderYCamara() {
        pintor = new SpriteBatch();
        texturaCancha = new Texture("campos/campo" + this.campoRuta + ".png");
        vistaAjustada = new FitViewport(Constantes.MUNDO_ANCHO, Constantes.MUNDO_ALTO);
    }

    private void crearActores() {
        jugador1 = new Normal("Jugador 1", configJugador1, Constantes.ESCALA_PERSONAJE);
        jugador2 = new Normal("Jugador 2", configJugador2, Constantes.ESCALA_PERSONAJE);

        jugador1.setPosicion(2.0f, 2.5f);
        jugador2.setPosicion(6.0f, 2.5f);

        dibujadorJugador1 = new DibujadorJugador(jugador1);
        dibujadorJugador2 = new DibujadorJugador(jugador2);

        pelota = new Pelota(Constantes.MUNDO_ANCHO / 2f, Constantes.MUNDO_ALTO / 2f, Constantes.ESCALA_PELOTA);
        dibujadorPelota = new DibujadorPelota(pelota);
    }

    private void configurarEntradas() {
        entradaJugador1 = new EntradaJugador(jugador1, Keys.W, Keys.S, Keys.A, Keys.D, Keys.CONTROL_LEFT);
        entradaJugador2 = new EntradaJugador(jugador2, Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.CONTROL_RIGHT);
        multiplexorEntradas = new InputMultiplexer(entradaJugador1, entradaJugador2);
        Gdx.input.setInputProcessor(multiplexorEntradas);
    }

    private void crearSistemas() {
        sistemaFisico    = new SistemaFisico();
        sistemaColisiones = new SistemaColisiones();
    }

    private void dibujarEscena() {
        ScreenUtils.clear(Color.BLACK);

        vistaAjustada.apply();
        pintor.setProjectionMatrix(vistaAjustada.getCamera().combined);
        pintor.begin();

        pintor.draw(texturaCancha, 0, 0, vistaAjustada.getWorldWidth(), vistaAjustada.getWorldHeight());

        dibujadorJugador1.dibujar(pintor);
        dibujadorJugador2.dibujar(pintor);
        dibujadorPelota.dibujar(pintor);

        pintor.end();
    }
}
