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
    private FitViewport viewport;
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        canchaDeFutbol = new Texture("CampoDeJuego.png");
        texturaDelPersonaje = new Texture("Jugador.png");
        spriteBatch = new SpriteBatch();
        spriteDelPersonaje = new Sprite(texturaDelPersonaje); // Initialize the sprite based on the texture
        spriteDelPersonaje.setSize(0.3f, 0.3f); // Define the size of the sprite
        viewport = new FitViewport(8, 5);
    }

    @Override
    public void render() {
        ingreso();
        logica();
        dibujar();
    }

    private void dibujar() {
    	ScreenUtils.clear(Color.BLACK);
		viewport.apply();
		spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
	    spriteBatch.begin();

	    float worldWidth = viewport.getWorldWidth();
	    float worldHeight = viewport.getWorldHeight();

	    spriteBatch.draw(canchaDeFutbol, 0, 0, worldWidth, worldHeight);
	    spriteDelPersonaje.draw(spriteBatch);

	    spriteBatch.end();
	}

	private void logica() {
	    float worldWidth = viewport.getWorldWidth();
	    float worldHeight = viewport.getWorldHeight();
	    float PlayerWidth = spriteDelPersonaje.getWidth();
	    float PlayerHeight = spriteDelPersonaje.getHeight();

	    spriteDelPersonaje.setX(MathUtils.clamp(spriteDelPersonaje.getX(), 0, worldWidth - PlayerWidth));
	    spriteDelPersonaje.setY(MathUtils.clamp(spriteDelPersonaje.getY(), 0, worldHeight - PlayerHeight));

	}

	private void ingreso() {
		float speed = 1;
		float delta = Gdx.graphics.getDeltaTime();
		float actualSpeed = speed * delta;
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
	        spriteDelPersonaje.translateX(-actualSpeed);
	    }
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
	        spriteDelPersonaje.translateX(actualSpeed);
	    }
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
	        spriteDelPersonaje.translateY(-actualSpeed);
	    }
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
	        spriteDelPersonaje.translateY(actualSpeed);
	    }

	}

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
