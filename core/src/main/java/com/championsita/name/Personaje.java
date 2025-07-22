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
    private Animation<TextureRegion> animArribaDerecha;
    private Animation<TextureRegion> animArribaIzquierda;
    private Animation<TextureRegion> animAbajoDerecha;
    private Animation<TextureRegion> animAbajoIzquierda;
    private Animation<TextureRegion> animArriba;
    private Animation<TextureRegion> animAbajo;

    private float x, y;
    private float width, height;
    private float stateTime;
    private boolean estaMoviendo;

    private Direccion direccionActual;

    public Personaje(float escala) {
        // Cargar texturas
        Texture sheetDerecha = new Texture("jugadorCorriendoDerecha.png");
        Texture sheetIzquierda = new Texture("jugadorCorriendoIzquierda.png");
        Texture texArribaDerecha = new Texture("jugadorMirandoArribaDerecha.png");
        Texture texArribaIzquierda = new Texture("jugadorMirandoArribaIzquierda.png");
        Texture texAbajoDerecha = new Texture("jugadorMirandoAbajoDerecha.png");
        Texture texAbajoIzquierda = new Texture("jugadorMirandoAbajoIzquierda.png");
        Texture texArriba = new Texture("jugadorMirandoArriba.png");
        Texture sheetAbajo = new Texture("jugadorCorriendoAbajo.png");
        Texture sheetArriba = new Texture("jugadorCorriendoArriba.png");

        textureQuieto = new Texture("Jugador.png");
        frameQuieto = new TextureRegion(textureQuieto);

        // Crear animaciones
        animacionDerecha = crearAnimacion(sheetDerecha, 7, 1);
        animacionIzquierda = crearAnimacion(sheetIzquierda, 7, 1);
        animArribaDerecha = crearAnimacion(texArribaDerecha, 1, 1);
        animArribaIzquierda = crearAnimacion(texArribaIzquierda, 1, 1);
        animAbajoDerecha = crearAnimacion(texAbajoDerecha, 1, 1);
        animAbajoIzquierda = crearAnimacion(texAbajoIzquierda, 1, 1);
        animArriba = crearAnimacion(sheetArriba, 6, 1);
        animAbajo = crearAnimacion(sheetAbajo, 6, 1);

        // Calcular tamaño basado en el primer frame
        TextureRegion frame = animacionDerecha.getKeyFrame(0);
        width = frame.getRegionWidth() * escala;
        height = frame.getRegionHeight() * escala;

        // Posición inicial
        x = 1;
        y = 1;
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

    private Direccion calcularDireccion(boolean izquierda, boolean derecha, boolean arriba, boolean abajo) {
        if (izquierda && arriba) return Direccion.ARRIBA_IZQUIERDA;
        if (derecha && arriba) return Direccion.ARRIBA_DERECHA;
        if (izquierda && abajo) return Direccion.ABAJO_IZQUIERDA;
        if (derecha && abajo) return Direccion.ABAJO_DERECHA;
        if (izquierda) return Direccion.IZQUIERDA;
        if (derecha) return Direccion.DERECHA;
        if (arriba) return Direccion.ARRIBA;
        if (abajo) return Direccion.ABAJO;
        return direccionActual; // No cambia si no hay entrada válida
    }

    public void update(float delta) {
        float speed = 1f;
        float move = speed * delta;
        estaMoviendo = false;

        boolean izquierda = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean derecha = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean arriba = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean abajo = Gdx.input.isKeyPressed(Input.Keys.S);

        estaMoviendo = izquierda || derecha || arriba || abajo;

        if (estaMoviendo) {
            direccionActual = calcularDireccion(izquierda, derecha, arriba, abajo);

            float dx = 0, dy = 0;
            if (izquierda) dx -= 1;
            if (derecha) dx += 1;
            if (arriba) dy += 1;
            if (abajo) dy -= 1;

            // Normalización para movimiento diagonal más fluido
            float len = (float)Math.sqrt(dx * dx + dy * dy);
            if (len != 0) {
                dx /= len;
                dy /= len;
            }

            x += dx * move;
            y += dy * move;
            stateTime += delta;
        }
    }

    public void limitarMovimiento(float worldWidth, float worldHeight) {
        x = MathUtils.clamp(x, 0, worldWidth - width);
        y = MathUtils.clamp(y, 0, worldHeight - height);
    }

    public void render(SpriteBatch batch) {
        TextureRegion frameActual;

        if (estaMoviendo) {
            switch (direccionActual) {
                case DERECHA:
                    frameActual = animacionDerecha.getKeyFrame(stateTime, true);
                    break;
                case IZQUIERDA:
                    frameActual = animacionIzquierda.getKeyFrame(stateTime, true);
                    break;
                case ARRIBA_DERECHA:
                    frameActual = animArribaDerecha.getKeyFrame(stateTime, true);
                    break;
                case ARRIBA_IZQUIERDA:
                    frameActual = animArribaIzquierda.getKeyFrame(stateTime, true);
                    break;
                case ABAJO_DERECHA:
                    frameActual = animAbajoDerecha.getKeyFrame(stateTime, true);
                    break;
                case ABAJO_IZQUIERDA:
                    frameActual = animAbajoIzquierda.getKeyFrame(stateTime, true);
                    break;
                case ARRIBA:
                	frameActual = animArriba.getKeyFrame(stateTime, true);
                	break;
                case ABAJO:
                	frameActual = animAbajo.getKeyFrame(stateTime, true);
                	break;
                default:
                    frameActual = frameQuieto;
            }
        } else {
            frameActual = frameQuieto;
        }

        batch.draw(frameActual, x, y, width, height);
    }

    public void dispose() {
        textureQuieto.dispose();
    }
}
