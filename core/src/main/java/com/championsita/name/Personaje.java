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

    // === HUD (acceso package para usar desde Principal) ===
    HudPersonaje hud;

    // === Constantes sprint/stamina ===
    private static final float LIMITE_STAMINA_BLOQUEO = 0.9f; // cuando cae por debajo, bloquea recarga breve
    private static final float TIEMPO_BLOQUEO_RECARGA = 2f;

    // === Identidad / configuración ===
    private final String nombre;
    private final float escala;
    private final float velocidadBase;
    private final float velocidadSprint;
    private final float staminaMax;

    // === Estado sprint/stamina ===
    private float stamina;
    private final float consumoSprint = 5f;   // por segundo
    private final float recargaStamina = 10f; // por segundo
    private boolean bloqueoRecarga = false;
    private float tiempoDesdeShiftSoltado = 0f;
    private float move = 0f; // distancia por frame (velocidad * delta)

    // === Estado de render/movimiento ===
    private float x, y, width, height, stateTime;
    private boolean estaMoviendo;
    private boolean espacioPresionado = false;
    private Direccion direccionActual = Direccion.ABAJO;

    // Hitbox (más angosta/alta para colisiones agradables)
    private Rectangle hitbox;

    // Offset fijo para mantener la hitbox centrada respecto al sprite
    // (recalculado cuando cambian width/height; reutilizado en setPosition/clamp)
    private float hbW, hbH, hbOx, hbOy;

    // === Texturas / animaciones ===
    private Texture textureQuieto;
    private TextureRegion frameQuieto;

    private Texture sheetDerecha, sheetIzquierda, sheetArriba, sheetAbajo,
                    sheetArribaDerecha, sheetArribaIzquierda, sheetAbajoDerecha, sheetAbajoIzquierda;

    private final Map<Direccion, Animation<TextureRegion>> animaciones = new EnumMap<>(Direccion.class);

    // === Constructor ===
    public Personaje(String nombre, PersonajeConfig config, float escala,
                     float velocidadBase, float velocidadSprint, float staminaMax) {

        this.nombre = nombre;
        this.escala = escala;
        this.velocidadBase = velocidadBase;
        this.velocidadSprint = velocidadSprint;
        this.staminaMax = staminaMax;
        this.stamina = staminaMax;

        hud = new HudPersonaje(this);

        // Textura quieto
        textureQuieto = new Texture(config.texturaQuieto);
        frameQuieto = new TextureRegion(textureQuieto);

        // Sheets desde config (no hardcodeamos rutas)
        sheetDerecha         = new Texture(config.sheetDerecha);
        sheetIzquierda       = new Texture(config.sheetIzquierda);
        sheetArriba          = new Texture(config.sheetArriba);
        sheetAbajo           = new Texture(config.sheetAbajo);
        sheetArribaDerecha   = new Texture(config.sheetArribaDerecha);
        sheetArribaIzquierda = new Texture(config.sheetArribaIzquierda);
        sheetAbajoDerecha    = new Texture(config.sheetAbajoDerecha);
        sheetAbajoIzquierda  = new Texture(config.sheetAbajoIzquierda);

        // Animaciones
        animaciones.put(Direccion.DERECHA,          crearAnimacion(sheetDerecha, 7, 1));
        animaciones.put(Direccion.IZQUIERDA,        crearAnimacion(sheetIzquierda, 7, 1));
        animaciones.put(Direccion.ARRIBA,           crearAnimacion(sheetArriba, 6, 1));
        animaciones.put(Direccion.ABAJO,            crearAnimacion(sheetAbajo, 6, 1));
        animaciones.put(Direccion.ARRIBA_DERECHA,   crearAnimacion(sheetArribaDerecha, 6, 1));
        animaciones.put(Direccion.ARRIBA_IZQUIERDA, crearAnimacion(sheetArribaIzquierda, 6, 1));
        animaciones.put(Direccion.ABAJO_DERECHA,    crearAnimacion(sheetAbajoDerecha, 6, 1));
        animaciones.put(Direccion.ABAJO_IZQUIERDA,  crearAnimacion(sheetAbajoIzquierda, 6, 1));

        // Tamaño base (desde un frame de derecha)
        TextureRegion f0 = animaciones.get(Direccion.DERECHA).getKeyFrame(0);
        width  = f0.getRegionWidth()  * escala;
        height = f0.getRegionHeight() * escala;

        // Posición inicial
        x = 1f; y = 1f; stateTime = 0f;

        // Hitbox centrada (60% ancho, 80% alto; apoyada abajo)
        hbW = width  * 0.60f;
        hbH = height * 0.80f;
        hbOx = (width - hbW) / 2f;
        hbOy = 0f;

        hitbox = new Rectangle(x + hbOx, y + hbOy, hbW, hbH);
    }

    // === Helpers ===
    private Animation<TextureRegion> crearAnimacion(Texture sheet, int columnas, int filas) {
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / columnas, sheet.getHeight() / filas);
        TextureRegion[] frames = new TextureRegion[columnas * filas];
        int k = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) frames[k++] = tmp[i][j];
        }
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
                if (!sprint) {
                    tiempoDesdeShiftSoltado += delta;
                    if (tiempoDesdeShiftSoltado >= TIEMPO_BLOQUEO_RECARGA) {
                        bloqueoRecarga = false;
                    }
                }
            } else {
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

        // Sincronizar hitbox con offset
        hitbox.setPosition(x + hbOx, y + hbOy);
    }

    // === API pública usada por ManejadorInput ===
    public void actualizarEstadojugador(boolean arriba, boolean abajo, boolean izquierda, boolean derecha,
                                        boolean sprint, float delta) {
        actualizarVelocidad(sprint, delta);
        actualizarDireccionYPos(izquierda, derecha, arriba, abajo);
    }

    // Compatibilidad con código viejo (sin sprint)
    public void moverDesdeInput(boolean arriba, boolean abajo, boolean izquierda, boolean derecha, float delta) {
        move = velocidadBase * delta;
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

        // Re-sincronizar hitbox tras el clamp
        hitbox.setPosition(x + hbOx, y + hbOy);
    }

    // === Getters / setters ===
    public void setEspacioPresionado(boolean valor) { this.espacioPresionado = valor; }
    public boolean estaEspacioPresionado() { return espacioPresionado; }
    public Direccion getDireccion() { return direccionActual; }
    public Rectangle getHitbox() { return hitbox; }
    public float getStamina() { return stamina; }
    public float getStaminaMax() { return staminaMax; }
    public float getX() { return x; }
    public float getY() { return y; }

    // >>> FIX CLAVE: setPosition actualiza la hitbox con el MISMO offset <<<
    public void setPosition(float nx, float ny) {
        this.x = nx;
        this.y = ny;
        hitbox.setPosition(x + hbOx, y + hbOy);
    }

    public float getWidth()  { return width; }
    public float getHeight() { return height; }

    // === Limpieza de recursos ===
    public void dispose() {
        if (textureQuieto != null) textureQuieto.dispose();
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
