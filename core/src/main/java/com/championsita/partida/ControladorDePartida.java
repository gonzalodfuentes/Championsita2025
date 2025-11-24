package com.championsita.partida;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.jugabilidad.constantes.Constantes;
import com.championsita.jugabilidad.modelo.*;
import com.championsita.jugabilidad.personajes.Normal;
import com.championsita.jugabilidad.sistemas.SistemaColisiones;
import com.championsita.jugabilidad.sistemas.SistemaFisico;
import com.championsita.jugabilidad.sistemas.SistemaPartido;
import com.championsita.jugabilidad.visuales.DibujadorJugador;
import com.championsita.jugabilidad.visuales.DibujadorPelota;
import com.championsita.menus.compartido.OpcionDeGoles;
import com.championsita.menus.compartido.OpcionDeTiempo;
import com.championsita.menus.menucarga.Campo;
import com.championsita.menus.menueleccion.Especial;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.modosdejuego.implementaciones.ModoEspecial;
import com.championsita.partida.modosdejuego.implementaciones.Practica;
import com.championsita.partida.modosdejuego.implementaciones.UnoContraUno;
import com.championsita.partida.nucleo.Contexto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        public final ArrayList<Equipo> equiposJugadores; // ← NUEVO
        public ArrayList<HabilidadesEspeciales> habilidadesEspeciales;

        private Config(Builder b) {
            this.skinsJugadores = b.skinsJugadores;
            this.campo = b.campo;
            this.goles = b.goles;
            this.tiempo = b.tiempo;
            this.modo = b.modo;
            this.equiposJugadores = b.equiposJugadores; // ← NUEVO
            if(modo.equals("especial")){
                this.habilidadesEspeciales = b.habilidadesEspeciales;
            }
        }

        public static class Builder {
            public ArrayList<String> skinsJugadores = new ArrayList<>();
            public ArrayList<Equipo> equiposJugadores = new ArrayList<>(); // ← NUEVO
            private Campo campo;
            private OpcionDeGoles goles = OpcionDeGoles.UNO;
            private OpcionDeTiempo tiempo = OpcionDeTiempo.CORTO;
            private String modo = "practica";
            public ArrayList<HabilidadesEspeciales> habilidadesEspeciales = new ArrayList<>();

            public Builder agregarSkin(String skin) {
                this.skinsJugadores.add(skin);
                return this;
            }

            // NUEVO: agregar equipo por jugador en el mismo orden que las skins
            public Builder agregarEquipo(Equipo equipo) {
                this.equiposJugadores.add(equipo);
                return this;
            }

            public Builder agregarHabilidades(ArrayList<HabilidadesEspeciales> habilidades) {
                this.habilidadesEspeciales.addAll(habilidades);
                return this;
            }

            public Builder campo(Campo v) { this.campo = v; return this; }
            public Builder goles(OpcionDeGoles v) { this.goles = v; return this; }
            public Builder tiempo(OpcionDeTiempo v) { this.tiempo = v; return this; }
            public Builder modo(String v) { this.modo = v; return this; }

            public Config build() {
                if (skinsJugadores.isEmpty() || campo == null) {
                    throw new IllegalStateException("Faltan datos obligatorios en Config (skins/campo)");
                }

                // opcional: validar que, si se pasaron equipos, coincida la cantidad
                if (!equiposJugadores.isEmpty() && equiposJugadores.size() != skinsJugadores.size()) {
                    throw new IllegalStateException("La cantidad de equipos no coincide con la de skins");
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
    private ShapeRenderer renderizadorDeFormas;

    private final ArrayList<Personaje> jugadores = new ArrayList<>();
    private Pelota pelota;
    Cancha cancha;

    private ArrayList<DibujadorJugador> dibujadoresJugadores = new ArrayList<>();
    private DibujadorPelota dibPelota;

    private SistemaFisico fisica;
    private SistemaColisiones colisiones;
    private SistemaPartido partido;

    public ControladorDePartida(Config config) {
        this.config = config;
    }

    @Override
    public void show() {
        inicializarBase();
        crearEntidades();

        // Crear contexto común
        Contexto ctx = new Contexto(viewport, batch, cancha ,fisica, colisiones, partido, jugadores, this);
        if(modoJuego.getClass() == ModoEspecial.class) {
            ctx = new Contexto(viewport, batch, cancha ,fisica, colisiones, partido, jugadores, this, config.habilidadesEspeciales);
        }
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
        cancha.dibujarCancha(batch, viewport);

        // Entidades principales
        for(DibujadorJugador dibujador : dibujadoresJugadores) {
            dibujador.dibujar(batch);
        }
        dibPelota.dibujar(batch);

        // Dibujo propio del modo (HUD o indicadores)
        modoJuego.renderizar(batch);

        batch.end();

        //Dibujamos los arcos
        renderizadorDeFormas.setProjectionMatrix(viewport.getCamera().combined);
        renderizadorDeFormas.begin(ShapeRenderer.ShapeType.Line);
        cancha.getArcoDerecho().dibujar(renderizadorDeFormas);
        cancha.getArcoIzquierdo().dibujar(renderizadorDeFormas);
        renderizadorDeFormas.end();
    }

    private void inicializarBase() {
        // Selección del modo
        String m = config.modo == null ? "practica" : config.modo.toLowerCase();
        switch (m) {
            case "1v1", "dosjug", "2jug", "1vs1" -> this.modoJuego = new UnoContraUno();
            case "especial" -> this.modoJuego = new ModoEspecial();
            default -> this.modoJuego = new Practica();
        }
        batch = new SpriteBatch();
        renderizadorDeFormas = new ShapeRenderer();
        // Carga de textura de cancha a partir del enum Campo
        String nombreCampo = config.campo.getNombre();
        texturaCancha = new Texture("campos/campo" + nombreCampo + ".png");
        viewport = new FitViewport(Constantes.MUNDO_ANCHO, Constantes.MUNDO_ALTO);
        fisica = new SistemaFisico();
        colisiones = new SistemaColisiones();
        partido = new SistemaPartido(this);
    }

    private void crearEntidades() {
        cancha = new Cancha(0.5f, 0.8f, texturaCancha);

        jugadores.clear();
        dibujadoresJugadores.clear();

        int cantidadDeJugadores = this.modoJuego.getCantidadDeJugadores();
        ArrayList<ConfiguracionPersonaje> configuraciones = new ArrayList<>();

        for (int i = 0; i < cantidadDeJugadores; i++) {
            configuraciones.add(ConfiguracionPersonaje.porDefecto(config.skinsJugadores.get(i)));
            Normal jugador = new Normal("Jugador" + (i + 1),
                    configuraciones.get(i),
                    Constantes.ESCALA_PERSONAJE);

            // Si tenemos equipo definido para este índice, lo asignamos
            if (config.modo.equalsIgnoreCase("especial")) {
                // Asignación automática si no se pasó equipo
                Equipo eq = (i == 0) ? Equipo.ROJO : Equipo.AZUL;
                jugador.setEquipo(eq);
            }
            else if (config.equiposJugadores != null
                    && !config.equiposJugadores.isEmpty()
                    && i < config.equiposJugadores.size()) {

                jugador.setEquipo(config.equiposJugadores.get(i));
            }
            else {
                // Default para modos normales
                jugador.setEquipo((i == 0) ? Equipo.ROJO : Equipo.AZUL);
            }


            jugadores.add(jugador);
        }

        pelota = new Pelota(Constantes.MUNDO_ANCHO / 2f,
                Constantes.MUNDO_ALTO / 2f,
                Constantes.ESCALA_PELOTA);

        for (Personaje jugador : jugadores) {
            dibujadoresJugadores.add(new DibujadorJugador(jugador));
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
