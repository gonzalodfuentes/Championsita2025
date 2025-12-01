package com.championsita.partida;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.jugabilidad.constantes.Constantes;
import com.championsita.jugabilidad.modelo.ConfiguracionPersonaje;
import com.championsita.jugabilidad.modelo.Pelota;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.jugabilidad.personajes.Normal;
import com.championsita.jugabilidad.sistemas.SistemaColisiones;
import com.championsita.jugabilidad.sistemas.SistemaFisico;
import com.championsita.jugabilidad.visuales.DibujadorJugador;
import com.championsita.jugabilidad.visuales.DibujadorPelota;
import com.championsita.menus.compartido.OpcionDeGoles;
import com.championsita.menus.compartido.OpcionDeTiempo;
import com.championsita.menus.menucarga.Campo;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.modosdejuego.implementaciones.Practica;
import com.championsita.partida.modosdejuego.implementaciones.UnoContraUno;
import com.championsita.partida.nucleo.Contexto;

import java.util.ArrayList;

/**
 * Controla la ejecución de una partida, independiente del modo.
 * Se encarga de preparar el contexto y delegar la lógica al modo elegido.
 */
public class ControladorDePartida implements Screen {

    /**
     * Configuración tipada de partida. Builder internamente para simplificar creación
     * desde menús sin depender de múltiples clases sueltas.
     */
    public static class Config {
        public final ArrayList<String> skinsJugadores;
        public final Campo campo;
        public final OpcionDeGoles goles;
        public final OpcionDeTiempo tiempo;
        public final String modo; // "practica", "1v1", etc.

        private Config(Builder b) {
            this.skinsJugadores = b.skinsJugadores;
            this.campo = b.campo;
            this.goles = b.goles;
            this.tiempo = b.tiempo;
            this.modo = b.modo;
        }

        public static class Builder {
            public ArrayList<String> skinsJugadores = new ArrayList<>();
            private Campo campo;
            private OpcionDeGoles goles = OpcionDeGoles.UNO;
            private OpcionDeTiempo tiempo = OpcionDeTiempo.CORTO;
            private String modo = "practica";

            public Builder agregarSkin(String skin) {this.skinsJugadores.add(skin); return this; }
            public Builder campo(Campo v) { this.campo = v; return this; }
            public Builder goles(OpcionDeGoles v) { this.goles = v; return this; }
            public Builder tiempo(OpcionDeTiempo v) { this.tiempo = v; return this; }
            public Builder modo(String v) { this.modo = v; return this; }

            public Config build() {
                if (skinsJugadores.isEmpty() || campo == null) {
                    throw new IllegalStateException("Faltan datos obligatorios en Config (skins/campo)");
                }
                return new Config(this);
            }
        }
    }

    private final Config config;
    private ModoDeJuego modoJuego;

    private SpriteBatch batch = new SpriteBatch();
    private Texture texturaCancha;
    private FitViewport viewport;

    private final ArrayList<Personaje> jugadores = new ArrayList<>();
    private Pelota pelota;

    private ArrayList<DibujadorJugador> dibujadoresJugadores = new ArrayList<>();
    private DibujadorPelota dibPelota;

    private SistemaFisico fisica;
    private SistemaColisiones colisiones;

    public ControladorDePartida(Config config) {
        this.config = config;
    }

    @Override
    public void show() {
        inicializarBase();
        crearEntidades();

        // Crear contexto común
        Contexto ctx = new Contexto(viewport, batch, texturaCancha, fisica, colisiones, jugadores);
        ctx.pelota = pelota;

        modoJuego.iniciar(ctx);
        Gdx.input.setInputProcessor(modoJuego.getProcesadorEntrada());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        modoJuego.actualizar(delta);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        // Fondo (cancha)
        batch.draw(texturaCancha, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Entidades principales
        for(DibujadorJugador dibujador : dibujadoresJugadores) {
            dibujador.dibujar(batch);
        }
        dibPelota.dibujar(batch);

        // Dibujo propio del modo (HUD o indicadores)
        modoJuego.renderizar(batch);

        batch.end();
    }

    private void inicializarBase() {
        // Selección del modo
        String m = config.modo == null ? "practica" : config.modo.toLowerCase();
        switch (m) {
            case "1v1", "dosjug", "2jug", "1vs1" -> this.modoJuego = new UnoContraUno();
            default -> this.modoJuego = new Practica();
        }
        batch = new SpriteBatch();
        // Carga de textura de cancha a partir del enum Campo
        String nombreCampo = config.campo.getNombre();
        texturaCancha = new Texture("campos/campo" + nombreCampo + ".png");
        viewport = new FitViewport(Constantes.MUNDO_ANCHO, Constantes.MUNDO_ALTO);
        fisica = new SistemaFisico();
        colisiones = new SistemaColisiones();
    }

    private void crearEntidades() {
        jugadores.clear();
        int cantidadDeJugadores = this.modoJuego.getCantidadDeJugadores();
        ArrayList<ConfiguracionPersonaje> ConfiguracionPersonajes = new ArrayList<>();

        for(int i = 0; i < cantidadDeJugadores; i++ ){
            ConfiguracionPersonajes.add(ConfiguracionPersonaje.porDefecto(config.skinsJugadores.get(i)));
            jugadores.add(new Normal("Jugador" + (i+1), ConfiguracionPersonajes.get(i), Constantes.ESCALA_PERSONAJE));
        }

        pelota = new Pelota(Constantes.MUNDO_ANCHO / 2f, Constantes.MUNDO_ALTO / 2f, Constantes.ESCALA_PELOTA);

        for (int i = 0; i < ConfiguracionPersonajes.size(); i++) {
            dibujadoresJugadores.add(new DibujadorJugador(jugadores.get(i)));
        }
        dibPelota = new DibujadorPelota(pelota);
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
}
