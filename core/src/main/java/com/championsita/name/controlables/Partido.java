package com.championsita.name.controlables;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.championsita.name.controlables.ManejadorInputJugador;
import com.championsita.name.controlables.Pelota;
import com.championsita.name.controlables.Personaje;

public class Partido extends InputAdapter implements Screen {

    // === Atributos ===
    private SpriteBatch batch;
    private Texture canchaDeFutbol;
    private Texture texturaDelPersonaje;
    private Personaje personaje;
    private Personaje personajeDos;
    private Pelota pelota;
    private FitViewport viewport;
    private ManejadorInputJugador manejadorInput;
    private ManejadorInputJugador manejadorInputDos;
    private boolean modoDosJugadores;

    public Partido(boolean modoDosJugadores) {
        this.modoDosJugadores = modoDosJugadores;
    }

    // === Métodos del ciclo de vida ===

    @Override
    public void show() {
        batch = new SpriteBatch();

        canchaDeFutbol = new Texture("campos/CampoDeJuego.png");
        texturaDelPersonaje = new Texture("jugador/amarillo/Jugador.png");

        personaje = new Personaje(0.003f);
        manejadorInput = new ManejadorInputJugador(personaje, false);
        pelota = new Pelota(3, 3, 0.002f);

        viewport = new FitViewport(8, 5);

        if (modoDosJugadores) {
            personajeDos = new Personaje(0.003f);
            manejadorInputDos = new ManejadorInputJugador(personajeDos, true);

            // Permite que los dos reciban eventos
            Gdx.input.setInputProcessor(new InputMultiplexer(manejadorInput, manejadorInputDos));
        } else {
            Gdx.input.setInputProcessor(manejadorInput);
        }
    }

    @Override
    public void render(float delta) {
        delta = Gdx.graphics.getDeltaTime();

        personaje.update(delta);
        manejadorInput.actualizar(delta);
        personaje.limitarMovimiento(viewport.getWorldWidth(), viewport.getWorldHeight());
        if(modoDosJugadores) {
            manejadorInputDos.actualizar(delta);
            personajeDos.update(delta);
            personajeDos.limitarMovimiento(viewport.getWorldWidth(), viewport.getWorldHeight());
        }
        pelota.actualizar(delta);

        detectarColisionConPelota();

        dibujar();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        batch.dispose();
        canchaDeFutbol.dispose();
        texturaDelPersonaje.dispose();
    }

    // === Métodos auxiliares ===

    private void dibujar() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(canchaDeFutbol, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        personaje.render(batch);
        if(modoDosJugadores) {
            personajeDos.render(batch);
        }
        pelota.render(batch);
        batch.end();
    }

    private void detectarColisionConPelota() {
        if(personaje.getHitbox().overlaps(pelota.getHitbox())) {
            float dx = 0, dy = 0;

            switch (personaje.getDireccion()) {
                case DERECHA:         dx =  0.001f; break;
                case IZQUIERDA:       dx = -0.001f; break;
                case ARRIBA:          dy =  0.001f; break;
                case ABAJO:           dy = -0.001f; break;
                case ARRIBA_DERECHA:  dx =  0.001f; dy =  0.001f; break;
                case ARRIBA_IZQUIERDA:dx = -0.001f; dy =  0.001f; break;
                case ABAJO_DERECHA:   dx =  0.001f; dy = -0.001f; break;
                case ABAJO_IZQUIERDA: dx = -0.001f; dy = -0.001f; break;
            }

            if (personaje.getHitbox().overlaps(pelota.getHitbox())) {
                if (personaje.estaEspacioPresionado()) {
                    pelota.disparar(dx, dy);
                } else {
                    pelota.empujar(dx, dy);
                }
            }
        } else {
            pelota.detener();
        }

        if(modoDosJugadores && personajeDos.getHitbox().overlaps(pelota.getHitbox())) {
            float dx = 0, dy = 0;

            switch (personajeDos.getDireccion()) {
                case DERECHA:         dx =  0.001f; break;
                case IZQUIERDA:       dx = -0.001f; break;
                case ARRIBA:          dy =  0.001f; break;
                case ABAJO:           dy = -0.001f; break;
                case ARRIBA_DERECHA:  dx =  0.001f; dy =  0.001f; break;
                case ARRIBA_IZQUIERDA:dx = -0.001f; dy =  0.001f; break;
                case ABAJO_DERECHA:   dx =  0.001f; dy = -0.001f; break;
                case ABAJO_IZQUIERDA: dx = -0.001f; dy = -0.001f; break;
            }

            if (personajeDos.getHitbox().overlaps(pelota.getHitbox())) {
                if (personajeDos.estaEspacioPresionado()) {
                    pelota.disparar(dx, dy);
                } else {
                    pelota.empujar(dx, dy);
                }
            }
        } else {
            pelota.detener();
        }
    }
}