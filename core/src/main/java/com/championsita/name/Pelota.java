package com.championsita.name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Pelota {

    // === Constantes ===
    private static final float FRICCION = 0.95f;
    private static final float FUERZA_DISPARO = 2.5f;
    private static final float FUERZA_EMPUJE = 1f; // más chico que disparo

    // === Atributos ===
    private Texture sheet;
    private Animation<TextureRegion> animacion;

    private float x, y;
    private float width, height;
    private float stateTime;

    private float velocidadX = 0;
    private float velocidadY = 0;
    private boolean animar = false;

    private Rectangle hitbox;

    // === Constructor ===
    public Pelota(float xInicial, float yInicial, float escala) {
        sheet = new Texture("pelotaAnimada.png");

        int columnas = 6;
        int filas = 1;

        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / columnas, sheet.getHeight() / filas);
        TextureRegion[] frames = new TextureRegion[columnas * filas];

        int index = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        animacion = new Animation<>(0.08f, frames);
        stateTime = 0f;

        width = frames[0].getRegionWidth() * escala;
        height = frames[0].getRegionHeight() * escala;

        this.x = xInicial;
        this.y = yInicial;

        hitbox = new Rectangle(x, y, width, height);
    }

    // === Métodos públicos ===
    public void actualizar(float delta, boolean alguienEmpuja) {
        if (Math.abs(velocidadX) > 0.01f || Math.abs(velocidadY) > 0.01f || alguienEmpuja) {
            animar = true;
            stateTime += delta;
        } else {
            animar = false;
        }

        x += velocidadX * delta;
        y += velocidadY * delta;

        velocidadX *= FRICCION;
        velocidadY *= FRICCION;

        if (Math.abs(velocidadX) < 0.01f) velocidadX = 0;
        if (Math.abs(velocidadY) < 0.01f) velocidadY = 0;

        hitbox.setPosition(x, y);
    }

    public void actualizar(float delta) {
        actualizar(delta, false); // llama al nuevo con un valor por defecto
    }



    public void disparar(float dx, float dy) {
        velocidadX = dx * getFuerzaDisparo();
        velocidadY = dy * getFuerzaDisparo();
        animar = true;
        stateTime = 0f; // reiniciar animación
    }

    public void empujar(float dx, float dy) {
        velocidadX = dx * getFuerzaEmpuje();
        velocidadY = dy * getFuerzaEmpuje();
        animar = true;
        stateTime = 0f; // reiniciar animación
    }

    public void detener() {
        animar = false;
    }

    public void render(SpriteBatch batch) {
        TextureRegion frameActual = animacion.getKeyFrame(stateTime, true);
        batch.draw(frameActual, x, y, width, height);
    }

    public void dispose() {
        sheet.dispose();
    }

    // === Getters y Setters ===
    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void setPosition(float nuevaX, float nuevaY) {
        this.x = nuevaX;
        this.y = nuevaY;
        hitbox.setPosition(nuevaX, nuevaY);
    }

	public static float getFuerzaEmpuje() {
		return FUERZA_EMPUJE;
	}

	public static float getFuerzaDisparo() {
		return FUERZA_DISPARO;
	}
}
