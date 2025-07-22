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
    private SpriteBatch batch;
    private Texture canchaDeFutbol;
    private Texture texturaDelPersonaje;
    private Sprite spriteDelPersonaje;
    private Personaje personaje;
    private FitViewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();
        canchaDeFutbol = new Texture("CampoDeJuego.png");
        texturaDelPersonaje = new Texture("Jugador.png");
        personaje = new Personaje(0.003f);
        viewport = new FitViewport(8, 5);
    }

    @Override
    public void render() {
	    float delta = Gdx.graphics.getDeltaTime();
	    personaje.update(delta);
	    personaje.limitarMovimiento(viewport.getWorldWidth(), viewport.getWorldHeight());
        dibujar();
    }

    private void dibujar() {
    	ScreenUtils.clear(Color.BLACK);
		viewport.apply();
		batch.setProjectionMatrix(viewport.getCamera().combined);

		batch.begin();
	    float worldWidth = viewport.getWorldWidth();
	    float worldHeight = viewport.getWorldHeight();

	    batch.draw(canchaDeFutbol, 0, 0, worldWidth, worldHeight);
	    personaje.render(batch);
	    batch.end();
	}

	//private void logica()

	//private void ingreso()

	@Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void dispose() {
        batch.dispose();
        canchaDeFutbol.dispose();
        texturaDelPersonaje.dispose();
    }
}
