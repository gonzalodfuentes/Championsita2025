package com.championsita.name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.name.personajes.Normal;

public class Principal extends ApplicationAdapter {

    // === Atributos ===
    private SpriteBatch batch;

    private Texture canchaDeFutbol;

    private Personaje jugador1;
    private Personaje jugador2;
    private Pelota   pelota;

    private ManejadorInput controlador1;
    private ManejadorInput controlador2;
    private InputMultiplexer multiplexer;

    private FitViewport viewport;

    // Si usás PersonajeConfig, dejalos listos para tus sprites
    private final PersonajeConfig configJugador1 = new PersonajeConfig(
            "Jugador.png",
            "jugadorCorriendoDerecha.png",
            "jugadorCorriendoIzquierda.png",
            "jugadorCorriendoArriba.png",
            "jugadorCorriendoAbajo.png",
            "jugadorCorriendoArribaDerecha.png",
            "jugadorCorriendoArribaIzquierda.png",
            "jugadorCorriendoAbajoDerecha.png",
            "jugadorCorriendoAbajoIzquierda.png"
    );

    private final PersonajeConfig configJugador2 = new PersonajeConfig(
            "Jugador.png",
            "jugadorCorriendoDerecha.png",
            "jugadorCorriendoIzquierda.png",
            "jugadorCorriendoArriba.png",
            "jugadorCorriendoAbajo.png",
            "jugadorCorriendoArribaDerecha.png",
            "jugadorCorriendoArribaIzquierda.png",
            "jugadorCorriendoAbajoDerecha.png",
            "jugadorCorriendoAbajoIzquierda.png"
    );

    @Override
    public void create() {
        batch = new SpriteBatch();
        canchaDeFutbol = new Texture("CampoDeJuego.png");

        // --- Instancias de jugadores ---
        // Si tu clase Normal recibe (nombre, config, escala, vBase, vSprint, staminaMax) usá eso.
        // Si tu Normal() es sin args, cambialo acá por el ctor real que tengas.
        jugador1 = new Normal(/* "J1", */ configJugador1, 0.003f, 1.0f, 1.8f, 100f);
        jugador2 = new Normal(/* "J2", */ configJugador2, 0.003f, 1.0f, 1.8f, 100f);

        // Posiciones iniciales distintas para que no spawneen pegados
        jugador1.setPosition(2.0f, 2.5f);
        jugador2.setPosition(6.0f, 2.5f);

        // --- Controles independientes por jugador ---
        controlador1 = new ManejadorInput(jugador1, Keys.W, Keys.S, Keys.A, Keys.D, Keys.SPACE);
        controlador2 = new ManejadorInput(jugador2, Keys.I, Keys.K, Keys.J, Keys.L, Keys.O);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(controlador1);
        multiplexer.addProcessor(controlador2);
        Gdx.input.setInputProcessor(multiplexer);

        pelota = new Pelota(4, 2.5f, 0.002f);

        viewport = new FitViewport(8, 5);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Actualizar inputs y jugadores
        controlador1.actualizar(delta);
        controlador2.actualizar(delta);

        jugador1.update(delta);
        jugador2.update(delta);

        jugador1.limitarMovimiento(viewport.getWorldWidth(), viewport.getWorldHeight());
        jugador2.limitarMovimiento(viewport.getWorldWidth(), viewport.getWorldHeight());

        // Colisiones jugador-jugador
        if (jugador1.getHitbox().overlaps(jugador2.getHitbox())) {
            resolverColision(jugador1, jugador2);
        }

        // Colisión con pelota -> calcula si alguien empuja/dispara
        detectarColisionConPelota();

        dibujar();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        canchaDeFutbol.dispose();
        // Personaje/Pelota deberían tener su propio dispose si corresponde
    }

    // === Dibujo ===
    private void dibujar() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(canchaDeFutbol, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        jugador1.render(batch);
        jugador2.render(batch);
        pelota.render(batch);

        // Barras de stamina individuales
        jugador1.hud.dibujarBarraStamina(batch, jugador1.getX(), jugador1.getY());
        jugador2.hud.dibujarBarraStamina(batch, jugador2.getX(), jugador2.getY());

        batch.end();
    }

    // === Colisiones ===

    private void detectarColisionConPelota() {
        boolean alguienEmpuja = false;

        if (jugador1.getHitbox().overlaps(pelota.getHitbox())) {
            alguienEmpuja = true;
            aplicarFuerzaPelota(jugador1);
        }
        if (jugador2.getHitbox().overlaps(pelota.getHitbox())) {
            alguienEmpuja = true;
            aplicarFuerzaPelota(jugador2);
        }

        // Si tu Pelota tiene actualizar(delta, empujando)
        pelota.actualizar(Gdx.graphics.getDeltaTime(), alguienEmpuja);
        // Si tu versión es pelota.actualizar(delta) sin flag, cambiala arriba y quitá el boolean.
    }

    private void aplicarFuerzaPelota(Personaje p) {
        float dx = 0, dy = 0;
        switch (p.getDireccion()) {
            case DERECHA:         dx =  1; break;
            case IZQUIERDA:       dx = -1; break;
            case ARRIBA:          dy =  1; break;
            case ABAJO:           dy = -1; break;
            case ARRIBA_DERECHA:  dx =  1; dy =  1; break;
            case ARRIBA_IZQUIERDA:dx = -1; dy =  1; break;
            case ABAJO_DERECHA:   dx =  1; dy = -1; break;
            case ABAJO_IZQUIERDA: dx = -1; dy = -1; break;
        }
        float len = (float)Math.sqrt(dx*dx + dy*dy);
        if (len != 0) { dx /= len; dy /= len; }

        float fuerza = p.estaEspacioPresionado() ? Pelota.getFuerzaDisparo() : Pelota.getFuerzaEmpuje();
        dx *= fuerza; dy *= fuerza;

        if (p.estaEspacioPresionado()) pelota.disparar(dx, dy);
        else                           pelota.empujar(dx, dy);
    }

    private void resolverColision(Personaje a, Personaje b) {
        // centros
        float aCx = a.getX() + a.getHitbox().width  / 2f;
        float aCy = a.getY() + a.getHitbox().height / 2f;
        float bCx = b.getX() + b.getHitbox().width  / 2f;
        float bCy = b.getY() + b.getHitbox().height / 2f;

        float dx = aCx - bCx;
        float dy = aCy - bCy;
        if (dx == 0 && dy == 0) dy = 0.01f;

        float len = (float)Math.sqrt(dx*dx + dy*dy);
        dx /= len; dy /= len;

        float overlap = 0.01f; // tweak
        a.setPosition(a.getX() + dx * overlap, a.getY() + dy * overlap);
        b.setPosition(b.getX() - dx * overlap, b.getY() - dy * overlap);
    }
}
