package com.championsita.name.partidos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.championsita.name.controlables.Direccion;

public class Jugador {

    private static final float VELOCIDAD = 1f;
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
    private float x, y;
    private float width, height;
    private float stateTime;
    private boolean estaMoviendo;
    private boolean espacioPresionado = false;
    private Direccion direccionActual = Direccion.ABAJO;
    private Rectangle hitbox;
    private Texture[] animacion;
    private float escala;
    private int jugador;

    public Jugador(float escala, int jugador) {
        this.escala = escala;
        this.jugador = jugador;
    }

    private void prepararAnimacion() {
        this.textureQuieto = new Texture("jugador/amarillo/Jugador.png");
        this.frameQuieto = new TextureRegion(this.textureQuieto);

        String color = identificarAvatar();

        this.animacion = new Texture[] {
                new Texture("jugador/" + color + "/jugadorCorriendoDerecha.png"),
                new Texture("jugador/" + color + "/jugadorCorriendoIzquierda.png"),
                new Texture("jugador/" + color + "/jugadorCorriendoArriba.png"),
                new Texture("jugador/" + color + "/jugadorCorriendoAbajo.png"),
                new Texture("jugador/" + color + "/jugadorCorriendoArribaDerecha.png"),
                new Texture("jugador/" + color + "/jugadorCorriendoArribaIzquierda.png"),
                new Texture("jugador/" + color + "/jugadorCorriendoAbajoDerecha.png"),
                new Texture("jugador/" + color + "/jugadorCorriendoAbajoIzquierda.png")
        };

        // Crear animaciones
        this.animacionDerecha = crearAnimacion(this.animacion[0], 7, 1);
        this.animacionIzquierda = crearAnimacion(this.animacion[1], 7, 1);
        this.animacionArriba = crearAnimacion(this.animacion[2], 6, 1);
        this.animacionAbajo = crearAnimacion(this.animacion[3], 6, 1);
        this.animacionArribaDerecha = crearAnimacion(this.animacion[4], 6, 1);
        this.animacionArribaIzquierda = crearAnimacion(this.animacion[5], 6, 1);
        this.animacionAbajoDerecha = crearAnimacion(this.animacion[6], 6, 1);
        this.animacionAbajoIzquierda = crearAnimacion(this.animacion[7], 6, 1);

        // Calcular tamaño
        TextureRegion frame = this.animacionDerecha.getKeyFrame(0);
        this.width = frame.getRegionWidth() * this.escala;
        this.height = frame.getRegionHeight() * this.escala;

        // Posición inicial
        this.x = 1;
        this.y = 1;
        this.stateTime = 0f;

        this.hitbox = new Rectangle(this.x, this.y, this.width, this.height);
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

    private String identificarAvatar() {
        String retorno = "";
        if(this.jugador == 1) {
            retorno = "amarillo";
        }
        else if(this.jugador == 2) {
            retorno = "celeste";
        }
        else if(this.jugador == 3) {
            retorno = "rojo";
        }
        else if(this.jugador == 4) {
            retorno = "verde";
        }
        return retorno;
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

    private void update(float delta) {
        if (estaMoviendo) {
            stateTime += delta;
        }
    }

    // Esto lo llama el ManejadorInput
    private void moverDesdeInput(boolean arriba, boolean abajo, boolean izquierda, boolean derecha, float delta) {
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

    private void render(SpriteBatch batch) {
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

    private void limitarMovimiento(float worldWidth, float worldHeight) {
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
