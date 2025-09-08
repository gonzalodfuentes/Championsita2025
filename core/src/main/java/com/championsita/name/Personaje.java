package com.championsita.name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.EnumMap;
import java.util.Map;

public abstract class Personaje {

    // === HUD ===
    HudPersonaje hud;

    // === Constantes de stamina/sprint ===
    private static final float LIMITE_STAMINA_BLOQUEO = 0.9f;   // umbral a partir del cual recorta velocidad y bloquea recarga
    private static final float TIEMPO_BLOQUEO_RECARGA = 2f;     // segundos de bloqueo tras vaciarse

    // === Identidad / configuración dinámica ===
    private final String nombre;
    private final float escala;
    private final float velocidadBase;
    private final float velocidadSprint;
    private final float staminaMax;

    // === Estado de stamina/sprint ===
    private float stamina;
    private final float consumoSprint = 5f;    // por segundo
    private final float recargaStamina = 10f;  // por segundo
    private boolean bloqueoRecarga = false;
    private float tiempoDesdeShiftSoltado = 0f;
    private float move = 0f;

    // === Estado del personaje ===
    private float x, y, width, height, stateTime;
    private boolean estaMoviendo;
    private boolean espacioPresionado = false;
    private Direccion direccionActual = Direccion.ABAJO;
    private Rectangle hitbox;

    // === Texturas / animaciones ===
    private Texture textureQuieto;
    private TextureRegion frameQuieto;

    // guardo las sheets para poder hacer dispose exacto una sola vez
    private Texture sheetDerecha, sheetIzquierda, sheetArriba, sheetAbajo,
                    sheetArribaDerecha, sheetArribaIzquierda, sheetAbajoDerecha, sheetAbajoIzquierda;

    private final Map<Direccion, Animation<TextureRegion>> animaciones = new EnumMap<>(Direccion.class);

    // === Constructor ===
    // Mantiene PersonajeConfig (como tu versión A) + parámetros de sprint (como tu versión B)
    public Personaje(String nombre, PersonajeConfig config, float escala,
                     float velocidadBase, float velocidadSprint, float staminaMax) {

        this.nombre = nombre;
        this.escala = escala;
        this.velocidadBase = velocidadBase;
        this.velocidadSprint = velocidadSprint;
        this.staminaMax = staminaMax;
        this.stamina = staminaMax;

        hud = new HudPersonaje(this);

        // textura quieto
        textureQuieto = new Texture(config.texturaQuieto);
        frameQuieto = new TextureRegion(textureQuieto);

        // cargo sheets desde config (sin hardcodear rutas)
        sheetDerecha         = new Texture(config.sheetDerecha);
        sheetIzquierda       = new Texture(config.sheetIzquierda);
        sheetArriba          = new Texture(config.sheetArriba);
        sheetAbajo           = new Texture(config.sheetAbajo);
        sheetArribaDerecha   = new Texture(config.sheetArribaDerecha);
        sheetArribaIzquierda = new Texture(config.sheetArribaIzquierda);
        sheetAbajoDerecha    = new Texture(config.sheetAbajoDerecha);
        sheetAbajoIzquierda  = new Texture(config.sheetAbajoIzquierda);

        // creo animaciones (como en tu versión A pero con sheets ya cargadas)
        animaciones.put(Direccion.DERECHA,          crearAnimacion(sheetDerecha, 7, 1));
        animaciones.put(Direccion.IZQUIERDA,        crearAnimacion(sheetIzquierda, 7, 1));
        animaciones.put(Direccion.ARRIBA,           crearAnimacion(sheetArriba, 6, 1));
        animaciones.put(Direccion.ABAJO,            crearAnimacion(sheetAbajo, 6, 1));
        animaciones.put(Direccion.ARRIBA_DERECHA,   crearAnimacion(sheetArribaDerecha, 6, 1));
        animaciones.put(Direccion.ARRIBA_IZQUIERDA, crearAnimacion(sheetArribaIzquierda, 6, 1));
        animaciones.put(Direccion.ABAJO_DERECHA,    crearAnimacion(sheetAbajoDerecha, 6, 1));
        animaciones.put(Direccion.ABAJO_IZQUIERDA,  crearAnimacion(sheetAbajoIzquierda, 6, 1));

        // tamaño base (desde frame de derecha)
        TextureRegion frame0 = animaciones.get(Direccion.DERECHA).getKeyFrame(0);
        width  = frame0.getRegionWidth()  * escala;
        height = frame0.getRegionHeight() * escala;

        // posición inicial + hitbox "afinada" (60% ancho, 80% alto, centrada; mejora colisiones)
        x = 1f; y = 1f; stateTime = 0f;
        float hbW = width * 0.6f;
        float hbH = height * 0.8f;
        float hbOx = (width - hbW) / 2f;
        float hbOy = 0f;
        hitbox = new Rectangle(x + hbOx, y + hbOy, hbW, hbH);
    }

    // === Helpers ===
    private Animation<TextureRegion> crearAnimacion(Texture sheet, int columnas, int filas) {
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / columnas, sheet.getHeight() / filas);
        TextureRegion[] frames = new TextureRegion[columnas * filas];
        int k = 0;
        for (int i = 0; i < filas; i++) for (int j = 0; j < columnas; j++) frames[k++] = tmp[i][j];
        return new Animation<>(0.1f, frames);
    }

    private Direccion calcularDireccion(boolean izquierda, boolean derecha, boolean arriba, boolean abajo) {
        if (izquierda && arriba) return Direccion.ARRIBA_IZQUIERDA;
        if (derecha   && arriba) return Direccion.ARRIBA_DERECHA;
        if (izquierda && abajo)  return Direccion.ABAJO_IZQUIERDA;
        if (derecha   && abajo)  return Direccion.ABAJO_DERECHA;
        if (izquierda) return Direccion.IZQUIERDA;
        if (derecha)   return Direccion.DERECHA;
        if (arriba)    return Direccion.ARRIBA;
        if (abajo)     return Direccion.ABAJO;
        return direccionActual;
    }

    private void actualizarVelocidad(boolean sprint, float delta) {
        float v = velocidadBase;

        if (sprint && stamina > 0f) {
            v = velocidadSprint;
            stamina -= consumoSprint * delta;
            if (stamina < 0f) stamina = 0f;

            if (stamina < LIMITE_STAMINA_BLOQUEO) {
                v = velocidadBase;
                bloqueoRecarga = true;
                tiempoDesdeShiftSoltado = 0f;
            }
        } else {
            if (bloqueoRecarga) {
                // sólo contamos tiempo si ya no está sprintando
                if (!sprint) {
                    tiempoDesdeShiftSoltado += delta;
                    if (tiempoDesdeShiftSoltado >= TIEMPO_BLOQUEO_RECARGA) {
                        bloqueoRecarga = false;
                    }
                }
            } else {
                // recarga normal
                stamina += recargaStamina * delta;
                if (stamina > staminaMax) stamina = staminaMax;
                v = velocidadBase;
            }
        }
        move = v * delta;
    }

    private void actualizarDireccionYPos(boolean izquierda, boolean derecha, boolean arriba, boolean abajo) {
        estaMoviendo = izquierda || derecha || arriba || abajo;
        if (!estaMoviendo) return;

        direccionActual = calcularDireccion(izquierda, derecha, arriba, abajo);

        float dx = 0f, dy = 0f;
        if (izquierda) dx -= 1f;
        if (derecha)   dx += 1f;
        if (arriba)    dy += 1f;
        if (abajo)     dy -= 1f;

        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len != 0f) { dx /= len; dy /= len; }

        x += dx * move;
        y += dy * move;

        // mantener hitbox centrada abajo como definimos
        float hbW = hitbox.getWidth(), hbH = hitbox.getHeight();
        float hbOx = (width - hbW) / 2f;
        float hbOy = 0f;
        hitbox.setPosition(x + hbOx, y + hbOy);
    }

    // === API pública usada por ManejadorInput ===
    public void actualizarEstadojugador(boolean arriba, boolean abajo, boolean izquierda, boolean derecha,
                                        boolean sprint, float delta) {
        actualizarVelocidad(sprint, delta);
        actualizarDireccionYPos(izquierda, derecha, arriba, abajo);
    }

    // compatible con código antiguo si algo aún llama a esto
    public void moverDesdeInput(boolean arriba, boolean abajo, boolean izquierda, boolean derecha, float delta) {
        move = velocidadBase * delta; // sin sprint
        actualizarDireccionYPos(izquierda, derecha, arriba, abajo);
    }

    public void update(float delta) {
        if (estaMoviendo) stateTime += delta;
    }

    public void render(SpriteBatch batch) {
        TextureRegion frameActual = estaMoviendo
                ? animaciones.get(direccionActual).getKeyFrame(stateTime, true)
                : frameQuieto;
        batch.draw(frameActual, x, y, width, height);
    }

    public void limitarMovimiento(float worldWidth, float worldHeight) {
        x = MathUtils.clamp(x, 0, worldWidth - width);
        y = MathUtils.clamp(y, 0, worldHeight - height);

        // mantener hitbox alineada tras el clamp
        float hbW = hitbox.getWidth(), hbH = hitbox.getHeight();
        float hbOx = (width - hbW) / 2f;
        float hbOy = 0f;
        hitbox.setPosition(x + hbOx, y + hbOy);
    }

    // === Getters / setters útiles ===
    public void setEspacioPresionado(boolean valor) { this.espacioPresionado = valor; }
    public boolean estaEspacioPresionado() { return espacioPresionado; }
    public Direccion getDireccion() { return direccionActual; }
    public Rectangle getHitbox() { return hitbox; }

    public float getStamina()     { return stamina; }
    public float getStaminaMax()  { return staminaMax; }

    public float getX() { return x; }
    public float getY() { return y; }
    public void  setPosition(float nx, float ny) { this.x = nx; this.y = ny; }

    public float getWidth()  { return width; }
    public float getHeight() { return height; }

    // === Limpieza de recursos ===
    public void dispose() {
        if (textureQuieto != null) textureQuieto.dispose();
        // disposo cada sheet exactamente una vez
        if (sheetDerecha != null)         sheetDerecha.dispose();
        if (sheetIzquierda != null)       sheetIzquierda.dispose();
        if (sheetArriba != null)          sheetArriba.dispose();
        if (sheetAbajo != null)           sheetAbajo.dispose();
        if (sheetArribaDerecha != null)   sheetArribaDerecha.dispose();
        if (sheetArribaIzquierda != null) sheetArribaIzquierda.dispose();
        if (sheetAbajoDerecha != null)    sheetAbajoDerecha.dispose();
        if (sheetAbajoIzquierda != null)  sheetAbajoIzquierda.dispose();
    }
}
