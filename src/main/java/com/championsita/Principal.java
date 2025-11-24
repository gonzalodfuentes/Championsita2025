package com.championsita;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.constantes.Constantes;
import com.championsita.entrada.EntradaJugador;
import com.championsita.modelo.*;
import com.championsita.personajes.Normal;
import com.championsita.sistemas.SistemaPartido;
import com.championsita.visuales.DibujadorJugador;
import com.championsita.visuales.DibujadorPelota;
import com.championsita.sistemas.SistemaFisico;
import com.championsita.sistemas.SistemaColisiones;

public class Principal extends ApplicationAdapter {

    // Render / cámara / hud
    private SpriteBatch pintor;
    private Texture texturaCancha;
    private FitViewport vistaAjustada;
    private HudPartido hudPartido;

    // Herramientas
    private CoordenadasDebug cords;

    // Actores
    private Personaje jugador1;
    private Personaje jugador2;
    private Pelota    pelota;
    private Cancha cancha;

    private ShapeRenderer debugRenderer;


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
    private SistemaPartido sistemaPartido;

    // Configs
    private final ConfiguracionPersonaje configJugador1 = ConfiguracionPersonaje.porDefecto();
    private final ConfiguracionPersonaje configJugador2 = ConfiguracionPersonaje.porDefecto();

    @Override
    public void create() {
        inicializarRenderYCamara();
        crearActores();
        configurarEntradas();
        crearSistemas();

        debugRenderer = new ShapeRenderer();

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

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
    public void dispose() {
        if (pintor != null) pintor.dispose();
        if (cancha != null) cancha.dispose();
        // si tus modelos tienen dispose(), llamalos acá
    }

    // ======================== Privados ========================



       private void inicializarRenderYCamara() {
        pintor = new SpriteBatch();
        texturaCancha = new Texture("CampoDeJuego.png");
        vistaAjustada = new FitViewport(Constantes.MUNDO_ANCHO, Constantes.MUNDO_ALTO);
        hudCamara = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudCamara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }


    private void crearActores() {
        jugador1 = new Normal("Jugador 1", configJugador1, Constantes.ESCALA_PERSONAJE);
        jugador2 = new Normal("Jugador 2", configJugador2, Constantes.ESCALA_PERSONAJE);

        jugador1.setPosicion(2.0f, 2.5f);
        jugador2.setPosicion(6.0f, 2.5f);

        dibujadorJugador1 = new DibujadorJugador(jugador1);
        dibujadorJugador2 = new DibujadorJugador(jugador2);

        float altoCancha = 0.9f;
        float anchoCancha = 0.3f;

        cancha = new Cancha(anchoCancha,altoCancha);



        pelota = new Pelota(Constantes.MUNDO_ANCHO / 2f, Constantes.MUNDO_ALTO / 2f, Constantes.ESCALA_PELOTA);
        dibujadorPelota = new DibujadorPelota(pelota);

        hudPartido = new HudPartido();

        cancha = new Cancha(Constantes.anchoCancha,Constantes.altoCancha);
        cancha.setTexturaCancha(texturaCancha);

    }


    private void configurarEntradas() {
        entradaJugador1 = new EntradaJugador(jugador1, Keys.W, Keys.S, Keys.A, Keys.D, Keys.SPACE);
        entradaJugador2 = new EntradaJugador(jugador2, Keys.I, Keys.K, Keys.J, Keys.L, Keys.O);
        multiplexorEntradas = new InputMultiplexer(entradaJugador1, entradaJugador2);
        Gdx.input.setInputProcessor(multiplexorEntradas);
    }

    private void crearSistemas() {
        sistemaFisico    = new SistemaFisico();
        sistemaColisiones = new SistemaColisiones();
        sistemaPartido = new SistemaPartido();
    }

    private void dibujarEscena() {
        ScreenUtils.clear(Color.BLACK);



        vistaAjustada.apply();
        pintor.setProjectionMatrix(vistaAjustada.getCamera().combined);

        pintor.begin();

        cancha.dibujarCancha(pintor,vistaAjustada);

            dibujadorJugador1.dibujar(pintor);
            dibujadorJugador2.dibujar(pintor);
            jugador1.getHud().dibujarBarraStamina(pintor, jugador1.getX(), jugador1.getY());
            dibujadorPelota.dibujar(pintor);

            sistemaPartido.verificarSiHayGol(pelota,cancha);

        sistemaPartido.verificarSiHayGol(pelota,cancha);



        // --- DEBUG HITBOXES ---
        debugRenderer.setProjectionMatrix(vistaAjustada.getCamera().combined); // usar la misma cámara
        debugRenderer.begin(ShapeRenderer.ShapeType.Line); // solo contornos

        debugRenderer.setColor(Color.RED);
        debugRenderer.rect(cancha.getArcoIzquierdo().getX(),cancha.getArcoIzquierdo().getY(),cancha.getArcoIzquierdo().getWidth(),cancha.getArcoIzquierdo().getHeight());

        debugRenderer.setColor(Color.RED);
        debugRenderer.rect(cancha.getArcoIzquierdo().getX(),cancha.getArcoIzquierdo().getY(),cancha.getArcoIzquierdo().getWidth(),cancha.getArcoIzquierdo().getHeight());

        debugRenderer.setColor(Color.RED);
        debugRenderer.rect(cancha.getArcoDerecho().getX(),cancha.getArcoDerecho().getY(),cancha.getArcoDerecho().getWidth(),cancha.getArcoDerecho().getHeight());



        // Pelota
        debugRenderer.setColor(Color.BLUE);
        debugRenderer.rect(pelota.getHitbox().x, pelota.getHitbox().y,
                pelota.getHitbox().width, pelota.getHitbox().height);
        debugRenderer.end();

        pintor.end();

        pintor.setProjectionMatrix(hudCamara.combined);


        pintor.begin();  //Hud (contador)

            hudPartido.dibujarHud(pintor, sistemaPartido);
            cords.render(pintor);


        pintor.end();

    

    }
}
