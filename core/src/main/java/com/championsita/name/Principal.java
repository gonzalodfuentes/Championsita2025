package com.championsita.name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Principal extends ApplicationAdapter {

    // === Atributos ===
    private SpriteBatch batch;

    private Texture canchaDeFutbol;
    private Texture texturaDelPersonaje;

    private Personaje jugador1;
    private Personaje jugador2;
    private Pelota pelota;
    private ManejadorInput controlador1;
    private ManejadorInput controlador2;
    InputMultiplexer multiplexer;

    private FitViewport viewport;

    PersonajeConfig configJugador1 = new PersonajeConfig(
    	    "Jugador.png", // quieto
    	    "jugadorCorriendoDerecha.png",
    	    "jugadorCorriendoIzquierda.png",
    	    "jugadorCorriendoArriba.png",
    	    "jugadorCorriendoAbajo.png",
    	    "jugadorCorriendoArribaDerecha.png",
    	    "jugadorCorriendoArribaIzquierda.png",
    	    "jugadorCorriendoAbajoDerecha.png",
    	    "jugadorCorriendoAbajoIzquierda.png"
    	);

    PersonajeConfig configJugador2 = new PersonajeConfig(
    	    "Jugador.png", // quieto
    	    "jugadorCorriendoDerecha.png",
    	    "jugadorCorriendoIzquierda.png",
    	    "jugadorCorriendoArriba.png",
    	    "jugadorCorriendoAbajo.png",
    	    "jugadorCorriendoArribaDerecha.png",
    	    "jugadorCorriendoArribaIzquierda.png",
    	    "jugadorCorriendoAbajoDerecha.png",
    	    "jugadorCorriendoAbajoIzquierda.png"
    	);

    // === Métodos del ciclo de vida ===

    @Override
    public void create() {
        batch = new SpriteBatch();

        canchaDeFutbol = new Texture("CampoDeJuego.png");
        texturaDelPersonaje = new Texture("Jugador.png");

        jugador1 = new Personaje(configJugador1, 0.003f);
        jugador2 = new Personaje(configJugador2, 0.003f);
        controlador1 = new ManejadorInput(jugador1, Keys.W, Keys.S, Keys.A, Keys.D, Keys.SPACE);
        controlador2 = new ManejadorInput(jugador2, Keys.I, Keys.K, Keys.J, Keys.L, Keys.O);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(controlador1);
        multiplexer.addProcessor(controlador2);
        Gdx.input.setInputProcessor(multiplexer);

        pelota = new Pelota(3, 3, 0.002f);

        viewport = new FitViewport(8, 5);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        controlador1.actualizar(delta);
        controlador2.actualizar(delta);
        jugador1.update(delta);
        jugador1.limitarMovimiento(viewport.getWorldWidth(), viewport.getWorldHeight());
        jugador2.update(delta);
        jugador2.limitarMovimiento(viewport.getWorldWidth(), viewport.getWorldHeight());
        pelota.actualizar(delta, false);

        detectarColisionConPelota();
        if(jugador1.getHitbox().overlaps(jugador2.getHitbox())) {
            resolverColision(jugador1, jugador2);
        }

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
        texturaDelPersonaje.dispose();
    }

    // === Métodos auxiliares ===

    private void resolverColision(Personaje a, Personaje b) {
        // Calculamos el centro de cada hitbox
        float aCenterX = a.getX() + a.getHitbox().width / 2;
        float aCenterY = a.getY() + a.getHitbox().height / 2;
        float bCenterX = b.getX() + b.getHitbox().width / 2;
        float bCenterY = b.getY() + b.getHitbox().height / 2;

        // Vector de desplazamiento
        float dx = aCenterX - bCenterX;
        float dy = aCenterY - bCenterY;

        // Evitar división por cero
        if(dx == 0 && dy == 0) dy = 0.01f;

        // Normalizamos
        float len = (float)Math.sqrt(dx*dx + dy*dy);
        dx /= len;
        dy /= len;

        // Separar personajes un poco
        float overlap = 0.01f; // ajuste según tamaño de hitboxes
        a.setPosition(a.getX() + dx * overlap, a.getY() + dy * overlap);
        b.setPosition(b.getX() - dx * overlap, b.getY() - dy * overlap);
    }


    private void dibujar() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(canchaDeFutbol, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        jugador1.render(batch);
        jugador2.render(batch);
        pelota.render(batch);
        batch.end();
    }



    private void detectarColisionConPelota() {
        boolean alguienEmpuja = false;

        // Chequea jugador 1
        if (jugador1.getHitbox().overlaps(pelota.getHitbox())) {
            alguienEmpuja = true;
            aplicarFuerzaPelota(jugador1);
        }

        // Chequea jugador 2
        if (jugador2.getHitbox().overlaps(pelota.getHitbox())) {
            alguienEmpuja = true;
            aplicarFuerzaPelota(jugador2);
        }

        // Actualiza pelota con la info de si alguien la empuja
        pelota.actualizar(Gdx.graphics.getDeltaTime(), alguienEmpuja);
    }

    private void aplicarFuerzaPelota(Personaje personaje) {
        float dx = 0, dy = 0;

        // Determinar vector según dirección
        switch (personaje.getDireccion()) {
            case DERECHA:         dx = 1; break;
            case IZQUIERDA:       dx = -1; break;
            case ARRIBA:          dy = 1; break;
            case ABAJO:           dy = -1; break;
            case ARRIBA_DERECHA:  dx = 1; dy = 1; break;
            case ARRIBA_IZQUIERDA:dx = -1; dy = 1; break;
            case ABAJO_DERECHA:   dx = 1; dy = -1; break;
            case ABAJO_IZQUIERDA: dx = -1; dy = -1; break;
        }

        // Normalizar diagonal
        float len = (float) Math.sqrt(dx*dx + dy*dy);
        if (len != 0) { dx /= len; dy /= len; }

        // Determinar fuerza
        float fuerza = personaje.estaEspacioPresionado() ? Pelota.getFuerzaDisparo() : Pelota.getFuerzaEmpuje();

        dx *= fuerza;
        dy *= fuerza;

        // Aplicar fuerza
        if (personaje.estaEspacioPresionado()) {
            pelota.disparar(dx, dy);
        } else {
            pelota.empujar(dx, dy);
        }
    }
}
