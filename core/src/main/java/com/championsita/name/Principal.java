package com.championsita.name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private Personaje personaje;
    private Pelota pelota;

    private FitViewport viewport;

    private ManejadorInput manejadorInput;

    // === Métodos del ciclo de vida ===

    @Override
    public void create() {
        batch = new SpriteBatch();

        canchaDeFutbol = new Texture("CampoDeJuego.png");
        texturaDelPersonaje = new Texture("Jugador.png");

        personaje = new Personaje(0.003f);
        manejadorInput = new ManejadorInput(personaje);
        pelota = new Pelota(3, 3, 0.002f);

        viewport = new FitViewport(8, 5);

        Gdx.input.setInputProcessor(manejadorInput);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        personaje.update(delta);
        manejadorInput.actualizar(delta);
        personaje.limitarMovimiento(viewport.getWorldWidth(), viewport.getWorldHeight());
        pelota.actualizar(delta);

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
        pelota.render(batch);
        batch.end();
    }

    private void detectarColisionConPelota() {
        if (personaje.getHitbox().overlaps(pelota.getHitbox())) {
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
    }
}
