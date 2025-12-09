package com.championsita.jugabilidad.modelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Pelota {

    // === Constantes ===
    private static final float FRICCION        = 0.95f;
    private static final float FUERZA_DISPARO  = 2.5f;
    private static final float FUERZA_EMPUJE   = 1f;   // si querés empuje continuo sosteniendo contacto, podés usarlo
    private static final float KICK_FORCE      = 0.35f; // “pateo” automático cortito en flanco de contacto
    private static final float UMBRAL_MOV      = 0.01f; // umbral de “velocidad ≈ 0”

    //
    private Personaje ultimoJugadorQueLaToco; // para los saques
    private Personaje jugadorTocandoPelota;



    // === Animación ===
    private Texture sheet;
    private Animation<TextureRegion> animacion;
    private float stateTime = 0f;
    private boolean animar = false;


    // === Estado ===
    private float x, y;
    private float width, height;
    private float velocidadX = 0f;
    private float velocidadY = 0f;
    private Rectangle hitbox;
    public boolean curvaActiva = false;
    public int curvaSigno = 0;


    // Flags de contacto (para detectar flanco)
    private boolean huboContactoEsteFrame = false;
    private boolean huboContactoPrevio    = false;



    public Pelota(float xInicial, float yInicial, float escala) {
        sheet = new Texture("pelota/pelotaAnimada.png");

        int columnas = 6, filas = 1;
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / columnas, sheet.getHeight() / filas);
        TextureRegion[] frames = new TextureRegion[columnas * filas];
        int idx = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                frames[idx++] = tmp[i][j];
            }
        }
        animacion = new Animation<>(0.08f, frames);

        width  = frames[0].getRegionWidth()  * escala;
        height = frames[0].getRegionHeight() * escala;

        this.x = xInicial;
        this.y = yInicial;
        hitbox = new Rectangle(x, y, width, height);
    }

    /**
     * Registrar contacto de un jugador este frame.
     * @param dx dirección X normalizada hacia donde empuja/dispara el jugador
     * @param dy dirección Y normalizada
     * @param disparo true si está presionando “disparo” (SPACE u otro)
     */
    public void registrarContacto(Personaje jugador, float dx, float dy, boolean disparo) {
        huboContactoEsteFrame = true;

        this.jugadorTocandoPelota = jugador;

        if (disparo) {
            // Disparo fuerte: setea velocidad y reinicia animación
            velocidadX = dx * FUERZA_DISPARO;
            velocidadY = dy * FUERZA_DISPARO;
            animar = true;
            stateTime = 0f;
        } else {
            // Empuje “automático” cortito SOLO en flanco de subida (primer frame de contacto)
            if (!huboContactoPrevio) {
                velocidadX += dx * KICK_FORCE;
                velocidadY += dy * KICK_FORCE;
                animar = true;
                stateTime = 0f;
            } else {
                // Si querés empuje continuo mientras se mantiene el contacto, descomenta:
                // velocidadX += dx * (FUERZA_EMPUJE * 0.0f); // 0.0f para no sobreacelerar; poné 0.05f si querés leve
            }
        }
    }



    public void actualizar(float delta) {
        // Animar si hay contacto o si seguimos en movimiento
        boolean enMovimiento = Math.abs(velocidadX) > UMBRAL_MOV || Math.abs(velocidadY) > UMBRAL_MOV;
        if (huboContactoEsteFrame || enMovimiento) {
            // opcional: escalar velocidad de animación según velocidad lineal
            float speed = (float) Math.hypot(velocidadX, velocidadY);
            float factor = 1.0f + 2.0f * speed; // tunear a gusto (1.0f = base)
            animar = true;
            stateTime += delta * factor;
        } else {
            animar = false;
        }

        // Integración simple + fricción
        x += velocidadX * delta;
        y += velocidadY * delta;

        velocidadX *= FRICCION;
        velocidadY *= FRICCION;

        if (Math.abs(velocidadX) < UMBRAL_MOV) velocidadX = 0f;
        if (Math.abs(velocidadY) < UMBRAL_MOV) velocidadY = 0f;

        hitbox.setPosition(x, y);

        // preparar flags para el próximo frame
        huboContactoPrevio    = huboContactoEsteFrame;
        huboContactoEsteFrame = false;

        //this.jugadorTocandoPelota = null;
    }

    /*
    public void render(SpriteBatch batch) {
        TextureRegion frame = animar ? animacion.getKeyFrame(stateTime, true)
                                     : animacion.getKeyFrame(0, true);
        batch.draw(frame, x, y, width, height);
    }
    */
    public void dispose() { sheet.dispose(); }

    // Getters
    public Rectangle getHitbox() { return hitbox; }
    public float getX() { return x; }
    public void setX(float x) { this.x = x;}
    public float getY() { return y; }
    public void setY(float y) { this.y = y;}
    public void setPosicion(float nuevaX, float nuevaY) { this.x = nuevaX; this.y = nuevaY; hitbox.setPosition(nuevaX, nuevaY); }

    public static float getFuerzaEmpuje()  { return FUERZA_EMPUJE; }
    public static float getFuerzaDisparo() { return FUERZA_DISPARO; }

    public float getWidth()  { return width; }
    public float getHeight() { return height; }


    public TextureRegion obtenerFrameActual() {
        // si querés que “quieta” muestre el primer frame:
        return animar ? animacion.getKeyFrame(stateTime, true)
                      : animacion.getKeyFrame(0, true);
    }

    public void detenerPelota() {
        this.velocidadX = 0.0F;
        this.velocidadY = 0.0F;
        this.animar = false;
    }

    public float getVelocidadX() {
        return velocidadX;
    }

    public float getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadX(float vx) {
        this.velocidadX = vx;
    }

    public void setVelocidadY(float vy) {
        this.velocidadY = vy;
    }


    public void limpiarContacto() {
        this.huboContactoEsteFrame = false;
        this.huboContactoPrevio = false;
    }

    public void setCurvaActiva(boolean activa, int signo) {
        this.curvaActiva = activa;
        this.curvaSigno = signo;
    }

    public void setVelocidad(float velocidadX, float velocidadY) {
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
    }

    public Personaje getUltimoJugadorQueLaToco() {
        return ultimoJugadorQueLaToco;
    }

    public void setUltimoJugadorQueLaToco(Personaje pj){
        ultimoJugadorQueLaToco = pj;
    }



    public Personaje getJugadorTocandoPelota() {
        return jugadorTocandoPelota;
    }

    public void setJugadorTocandoPelota(Personaje jugador) {
        this.jugadorTocandoPelota =  jugador;
    }

    public void resetJugadorTocando() {
        this.jugadorTocandoPelota = null;
    }


}
