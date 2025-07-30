package com.championsita.name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Personaje {

    // === Constantes ===
    private static final float VELOCIDAD = 1f;

    // === Texturas y Animaciones ===
    private Texture textureQuieto;
    private TextureRegion frameQuieto;

    private Animation<TextureRegion> animacionDerecha;
    private Animation<TextureRegion> animacionIzquierda;
    private Animation<TextureRegion> animacionArriba;
    private Animation<TextureRegion> animacionAbajo;
    private Animation<TextureRegion> animacionArribaDerecha;
    private Animation<TextureRegion> animacionArribaIzquierda;
    private Animation<TextureRegion> animacionAbajoDerecha;
    private Animation<TextureRegion> animacionAbajoIzquierda;

    // === Estado del personaje ===
    private float x, y;
    private float width, height;
    private float stateTime;
    private boolean estaMoviendo;
    private boolean espacioPresionado = false;
    private Direccion direccionActual = Direccion.ABAJO;

    private Rectangle hitbox;

    // === Constructor ===
    public Personaje(float escala) {
        // Cargar texturas
        textureQuieto = new Texture("Jugador.png");
        frameQuieto = new TextureRegion(textureQuieto);

        Texture sheetDerecha = new Texture("jugadorCorriendoDerecha.png");
        Texture sheetIzquierda = new Texture("jugadorCorriendoIzquierda.png");
        Texture sheetArriba = new Texture("jugadorCorriendoArriba.png");
        Texture sheetAbajo = new Texture("jugadorCorriendoAbajo.png");
        Texture sheetArribaDerecha = new Texture("jugadorCorriendoArribaDerecha.png");
        Texture sheetArribaIzquierda = new Texture("jugadorCorriendoArribaIzquierda.png");
        Texture sheetAbajoDerecha = new Texture("jugadorCorriendoAbajoDerecha.png");
        Texture sheetAbajoIzquierda = new Texture("jugadorCorriendoAbajoIzquierda.png");

        // Crear animaciones
        animacionDerecha = crearAnimacion(sheetDerecha, 7, 1);
        animacionIzquierda = crearAnimacion(sheetIzquierda, 7, 1);
        animacionArriba = crearAnimacion(sheetArriba, 6, 1);
        animacionAbajo = crearAnimacion(sheetAbajo, 6, 1);
        animacionArribaDerecha = crearAnimacion(sheetArribaDerecha, 6, 1);
        animacionArribaIzquierda = crearAnimacion(sheetArribaIzquierda, 6, 1);
        animacionAbajoDerecha = crearAnimacion(sheetAbajoDerecha, 6, 1);
        animacionAbajoIzquierda = crearAnimacion(sheetAbajoIzquierda, 6, 1);

        // Calcular tamaño
        TextureRegion frame = animacionDerecha.getKeyFrame(0);
        width = frame.getRegionWidth() * escala;
        height = frame.getRegionHeight() * escala;

        // Posición inicial
        x = 1;
        y = 1;
        stateTime = 0f;

        hitbox = new Rectangle(x, y, width, height);
    }

    // === Métodos privados auxiliares ===

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
        return direccionActual;
    }

    // === Métodos públicos ===

    public void update(float delta) {
        if (estaMoviendo) {
            stateTime += delta;
        }
    }

    // Esto lo llama el ManejadorInput
    public void moverDesdeInput(boolean arriba, boolean abajo, boolean izquierda, boolean derecha, float delta) {
        float move = VELOCIDAD * delta;
        estaMoviendo = izquierda || derecha || arriba || abajo;

        if (estaMoviendo) {
            direccionActual = calcularDireccion(izquierda, derecha, arriba, abajo);

            float dx = 0, dy = 0;
            if (izquierda) dx -= 1;
            if (derecha) dx += 1;
            if (arriba) dy += 1;
            if (abajo) dy -= 1;

            float len = (float) Math.sqrt(dx * dx + dy * dy);
            if (len != 0) {
                dx /= len;
                dy /= len;
            }

            x += dx * move;
            y += dy * move;
            hitbox.setPosition(x, y);
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion frameActual;

        if (estaMoviendo) {
            switch (direccionActual) {
                case DERECHA:
                    frameActual = animacionDerecha.getKeyFrame(stateTime, true); break;
                case IZQUIERDA:
                    frameActual = animacionIzquierda.getKeyFrame(stateTime, true); break;
                case ARRIBA:
                    frameActual = animacionArriba.getKeyFrame(stateTime, true); break;
                case ABAJO:
                    frameActual = animacionAbajo.getKeyFrame(stateTime, true); break;
                case ARRIBA_DERECHA:
                    frameActual = animacionArribaDerecha.getKeyFrame(stateTime, true); break;
                case ARRIBA_IZQUIERDA:
                    frameActual = animacionArribaIzquierda.getKeyFrame(stateTime, true); break;
                case ABAJO_DERECHA:
                    frameActual = animacionAbajoDerecha.getKeyFrame(stateTime, true); break;
                case ABAJO_IZQUIERDA:
                    frameActual = animacionAbajoIzquierda.getKeyFrame(stateTime, true); break;
                default:
                    frameActual = frameQuieto;
            }
        } else {
            frameActual = frameQuieto;
        }

        batch.draw(frameActual, x, y, width, height);
    }

    public void limitarMovimiento(float worldWidth, float worldHeight) {
        x = MathUtils.clamp(x, 0, worldWidth - width);
        y = MathUtils.clamp(y, 0, worldHeight - height);
    }

    public Direccion getDireccion() {
        return direccionActual;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setEspacioPresionado(boolean valor) {
        this.espacioPresionado = valor;
    }

    public boolean estaEspacioPresionado() {
        return espacioPresionado;
    }

    public void dispose() {
        textureQuieto.dispose();
    }
}
