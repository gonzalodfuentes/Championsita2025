package com.championsita.partida;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.Principal;
import com.championsita.jugabilidad.modelo.*;
import com.championsita.jugabilidad.sistemas.SistemaColisiones;
import com.championsita.jugabilidad.sistemas.SistemaFisico;
import com.championsita.jugabilidad.sistemas.SistemaPartido;
import com.championsita.jugabilidad.visuales.DibujadorJugador;
import com.championsita.jugabilidad.visuales.DibujadorPelota;
import com.championsita.jugabilidad.visuales.HudPartido;
import com.championsita.menus.menupausa.Pausa;
import com.championsita.menus.menupausa.PausaBoton;
import com.championsita.partida.herramientas.Config;
import com.championsita.partida.herramientas.MundoPartida;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.modosdejuego.implementaciones.ModoEspecial;
import com.championsita.partida.nucleo.ContextoModoDeJuego;
import com.championsita.partida.herramientas.*;
import com.championsita.partida.nucleo.ContextoPartida;

import java.util.ArrayList;

/**
 * Controla la ejecución de una partida, independiente del modo.
 * Se encarga de preparar el contexto y delegar la lógica al modo elegido.
 */
public class ControladorDePartida extends InputAdapter implements Screen {

    private final Config config;
    private ModoDeJuego modoJuego;

    private SpriteBatch batch = new SpriteBatch();
    private Texture texturaCancha;
    private FitViewport viewport;
    private ShapeRenderer renderizadorDeFormas;

    private ArrayList<Personaje> jugadores = new ArrayList<>();
    private Pelota pelota;
    Cancha cancha;

    private ArrayList<DibujadorJugador> dibujadoresJugadores = new ArrayList<>();
    private DibujadorPelota dibPelota;
    private HudPartido dibujadorHudPartido;

    private SistemaFisico fisica;
    private SistemaColisiones colisiones;
    private SistemaPartido partido;

    private Pausa pausa;

    public ControladorDePartida(Config config, Principal juego) {
        this.config = config;
        ContextoPartida contextoPartida= InicializadorPartida.inicializar(config, this);
        this.modoJuego = contextoPartida.modo;
        this.batch = contextoPartida.batch;
        this.renderizadorDeFormas = contextoPartida.rendererFormas;
        this.viewport = contextoPartida.viewport;
        this.texturaCancha = contextoPartida.texturaCancha;
        this.fisica = contextoPartida.fisica;
        this.colisiones = contextoPartida.colisiones;
        this.partido = contextoPartida.partido;

        MundoPartida mundo = PartidaFactory.crearDesdeConfig(config);
        this.cancha = mundo.cancha;
        this.pelota = mundo.pelota;
        this.jugadores = mundo.jugadores;
        this.dibPelota = mundo.dibPelota;
        this.dibujadoresJugadores = mundo.dibujadoresJugadores;

        this.dibujadorHudPartido = new HudPartido(viewport);

        this.pausa = new Pausa(juego);
    }

    @Override
    public void show() {
        ContextoModoDeJuego ctx = new ContextoModoDeJuego(viewport, batch, cancha ,fisica, colisiones, partido, jugadores, this);

        if(modoJuego.getClass() == ModoEspecial.class) {
            ctx = new ContextoModoDeJuego(viewport, batch, cancha ,fisica, colisiones, partido, jugadores, this, config.habilidadesEspeciales);
        }

        ctx.pelota = pelota;

        modoJuego.iniciar(ctx);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this); // ControladorDePartida escucha ESC y mouse
        multiplexer.addProcessor(modoJuego.getProcesadorEntrada()); // el modo sigue recibiendo input
        Gdx.input.setInputProcessor(multiplexer);

        this.pausa.show();
    }

    @Override
    public void render(float delta) {
        RenderizadorPartida render = new RenderizadorPartida();

        ScreenUtils.clear(Color.BLACK);
        modoJuego.actualizar(delta);

        // 1. FONDO (cancha)
        render.renderFondo(batch, viewport, cancha);

        // 2. ENTIDADES (jugadores + pelota)
        render.renderEntidades(batch, viewport, dibujadoresJugadores, dibPelota, modoJuego);

        // 3. HUD DEL MODO (proyección del mundo)
        render.renderHudModo(batch, modoJuego, viewport);

        // 4. HUD DEL PARTIDO (pantalla)
        render.renderHudPartido(batch, dibujadorHudPartido, partido,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());

        // 5. ARCOS (ShapeRenderer sobre mundo)
        render.renderArcos(renderizadorDeFormas, viewport, cancha);

        this.pausa.renderizar(batch);
    }

    @Override
    public boolean keyDown(int key) {
        return this.pausa.keyDown(key);
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        return this.pausa.mouseMoved(x, y);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        y = Gdx.graphics.getHeight() - y;
        return this.pausa.touchUp(x, y, pointer, button);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (texturaCancha != null) texturaCancha.dispose();
        if (modoJuego != null) modoJuego.liberar();
    }

    public ArrayList<Personaje> getJugadoresDelEquipo(Equipo equipo) {
        ArrayList<Personaje> lista = new ArrayList<>();

        for (Personaje pj : jugadores) {
            if (pj.getEquipo() == equipo) {
                lista.add(pj);
            }
        }

        return lista;
    }
}