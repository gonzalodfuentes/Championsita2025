package com.championsita.name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Personaje {
    private TextureRegion frameQuieto;
    private Texture textureQuieto;
    private Animation<TextureRegion> animacionDerecha;
    private Animation<TextureRegion> animacionIzquierda;

    private float x, y;
    private float width, height;
    private float stateTime;
    private boolean estaMoviendo;

    private enum Direccion { IZQUIERDA, DERECHA }
    private Direccion direccionActual = Direccion.DERECHA;

    public Personaje(float escala) {
        // Cargar texturas
        Texture sheetDerecha = new Texture("jugadorCorriendoDerecha.png");
        Texture sheetIzquierda = new Texture("jugadorCorriendoIzquierda.png");
        textureQuieto = new Texture("Jugador.png");
        frameQuieto = new TextureRegion(textureQuieto);

        // Crear animaciones
        animacionDerecha = crearAnimacion(sheetDerecha, 7, 1);
        animacionIzquierda = crearAnimacion(sheetIzquierda, 7, 1);

        // Calcular tamaño basado en el primer frame
        TextureRegion frame = animacionDerecha.getKeyFrame(0);
        width = frame.getRegionWidth() * escala;
        height = frame.getRegionHeight() * escala;

        // Posición inicial
        x = 2;
        y = 2;
        stateTime = 0f;
    }

    private Animation<TextureRegion> crearAnimacion(Texture sheet, int columnas, int filas) {
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / columnas, sheet.getHeight() / filas);
        TextureRegion[] frames = new TextureRegion[columnas * filas];

        int index = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return new Animation<>(0.1f, frames);
    }

    public void update(float delta) {
        float speed = 2.5f;
        float move = speed * delta;
        estaMoviendo = false;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= move;
            stateTime += delta;
            estaMoviendo = true;
            direccionActual = Direccion.IZQUIERDA;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += move;
            stateTime += delta;
            estaMoviendo = true;
            direccionActual = Direccion.DERECHA;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += move;
            estaMoviendo = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= move;
            estaMoviendo = true;
        }
    }

    public void limitarMovimiento(float worldWidth, float worldHeight) {
        x = MathUtils.clamp(x, 0, worldWidth - width);
        y = MathUtils.clamp(y, 0, worldHeight - height);
    }

    public void render(SpriteBatch batch) {
        TextureRegion frameActual;

        if (estaMoviendo) {
            if (direccionActual == Direccion.DERECHA) {
                frameActual = animacionDerecha.getKeyFrame(stateTime, true);
            } else {
                frameActual = animacionIzquierda.getKeyFrame(stateTime, true);
            }
        } else {
            frameActual = frameQuieto;
        }

        batch.draw(frameActual, x, y, width, height);
    }

    public void dispose() {
        textureQuieto.dispose();
        // También deberías guardar y liberar los `Texture` de las animaciones si los guardás como campo
        // Pero en este ejemplo los usamos solo localmente, así que no hay necesidad extra
    }
}
