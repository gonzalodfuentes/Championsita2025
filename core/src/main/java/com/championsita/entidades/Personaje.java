package com.championsita.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.EnumMap;
import java.util.Map;

// Ajustá este import a donde tengas el enum Direccion
// import com.championsita.modelo.Direccion;
// o: import com.championsita.name.Direccion;

public abstract class Personaje {

    // =========================
    // Identidad y configuración
    // =========================
    private final String nombre;
    private final float  escala;
    private final AtributosPersonaje atributos;
    private final ConfiguracionPersonaje sprites;

    // =========================
    // Estado de stamina/sprint
    // =========================
    // Regla: mostramos "velocidad actual" como distancia por frame (velocidad * delta).
    private float staminaActual;
    private boolean bloqueoRecarga = false;
    private float   tiempoBloqueoRecarga = 0f;
    private float   distanciaEsteFrame   = 0f; // move

    // =========================
    // Estado de movimiento / render
    // =========================
    private float x, y;                 // posición del sprite (esquina inferior-izquierda)
    private float ancho, alto;          // tamaño del sprite escalado
    private float tiempoAnimacion = 0f; // stateTime
    private boolean estaMoviendo = false;
    private boolean espacioPresionado = false;
    private Direccion direccionActual = Direccion.ABAJO;

    // Hitbox con offset (más angosta y “apoyada” abajo para mejor gamefeel)
    private final Rectangle hitbox = new Rectangle();
    private float hbAncho, hbAlto, hbOffsetX, hbOffsetY;

    // =========================
    // Texturas / Animaciones
    // =========================
    private HudPersonaje hud;
    private Texture texturaQuieto;
    private TextureRegion frameQuieto;

    private Texture sheetDerecha, sheetIzquierda, sheetArriba, sheetAbajo,
                    sheetArribaDerecha, sheetArribaIzquierda,
                    sheetAbajoDerecha,  sheetAbajoIzquierda;

    private final Map<Direccion, Animation<TextureRegion>> animaciones = new EnumMap<>(Direccion.class);

    // =========================
    // Constructor
    // =========================
    public Personaje(String nombre,
                     ConfiguracionPersonaje sprites,
                     AtributosPersonaje atributos,
                     float escala) {

        this.nombre   = nombre;
        this.sprites  = sprites;
        this.atributos = atributos;
        this.escala   = escala;

        this.staminaActual = atributos.getStaminaMaxima();

        // Cargar texturas base
        this.texturaQuieto = new Texture(sprites.texturaQuieto);
        this.frameQuieto   = new TextureRegion(texturaQuieto);

        // Cargar sheets desde la configuración (no hardcodeamos rutas)
        this.sheetDerecha         = new Texture(sprites.sheetDerecha);
        this.sheetIzquierda       = new Texture(sprites.sheetIzquierda);
        this.sheetArriba          = new Texture(sprites.sheetArriba);
        this.sheetAbajo           = new Texture(sprites.sheetAbajo);
        this.sheetArribaDerecha   = new Texture(sprites.sheetArribaDerecha);
        this.sheetArribaIzquierda = new Texture(sprites.sheetArribaIzquierda);
        this.sheetAbajoDerecha    = new Texture(sprites.sheetAbajoDerecha);
        this.sheetAbajoIzquierda  = new Texture(sprites.sheetAbajoIzquierda);

        // Crear animaciones
        animaciones.put(Direccion.DERECHA,          crearAnimacion(sheetDerecha, 7, 1));
        animaciones.put(Direccion.IZQUIERDA,        crearAnimacion(sheetIzquierda, 7, 1));
        animaciones.put(Direccion.ARRIBA,           crearAnimacion(sheetArriba, 6, 1));
        animaciones.put(Direccion.ABAJO,            crearAnimacion(sheetAbajo, 6, 1));
        animaciones.put(Direccion.ARRIBA_DERECHA,   crearAnimacion(sheetArribaDerecha, 6, 1));
        animaciones.put(Direccion.ARRIBA_IZQUIERDA, crearAnimacion(sheetArribaIzquierda, 6, 1));
        animaciones.put(Direccion.ABAJO_DERECHA,    crearAnimacion(sheetAbajoDerecha, 6, 1));
        animaciones.put(Direccion.ABAJO_IZQUIERDA,  crearAnimacion(sheetAbajoIzquierda, 6, 1));

        // Medir tamaño del sprite según un frame típico (derecha) y escalar
        TextureRegion f0 = animaciones.get(Direccion.DERECHA).getKeyFrame(0);
        this.ancho = f0.getRegionWidth()  * escala;
        this.alto  = f0.getRegionHeight() * escala;

        // Posición inicial por defecto
        this.x = 1f;
        this.y = 1f;

        // Configurar hitbox: 60% ancho, 80% alto y centrada horizontalmente
        this.hbAncho   = ancho * 0.60f;
        this.hbAlto    = alto  * 0.80f;
        this.hbOffsetX = (ancho - hbAncho) / 2f;
        this.hbOffsetY = 0f;

        hitbox.set(x + hbOffsetX, y + hbOffsetY, hbAncho, hbAlto);
    }

    // =========================
    // Helpers internos
    // =========================
    private Animation<TextureRegion> crearAnimacion(Texture sheet, int columnas, int filas) {
        TextureRegion[][] tmp   = TextureRegion.split(sheet, sheet.getWidth() / columnas, sheet.getHeight() / filas);
        TextureRegion[] frames  = new TextureRegion[columnas * filas];
        int k = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                frames[k++] = tmp[i][j];
            }
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

    private void actualizarVelocidadYStamina(boolean sprint, float delta) {
        float velocidad = atributos.getVelocidadBase();

        if (sprint && staminaActual > 0f) {
            velocidad = atributos.getVelocidadSprint();
            staminaActual -= atributos.getConsumoSprintPorSegundo() * delta;
            if (staminaActual < 0f) staminaActual = 0f;

            // Si cae por debajo del límite, reducir a base y bloquear recarga un rato
            if (staminaActual < atributos.getLimiteStaminaBloqueo()) {
                velocidad = atributos.getVelocidadBase();
                bloqueoRecarga       = true;
                tiempoBloqueoRecarga = 0f;
            }
        } else {
            // Si veníamos bloqueados, esperamos a que pase el tiempo de bloqueo
            if (bloqueoRecarga) {
                if (!sprint) {
                    tiempoBloqueoRecarga += delta;
                    if (tiempoBloqueoRecarga >= atributos.getTiempoBloqueoRecargaSegundos()) {
                        bloqueoRecarga = false;
                    }
                }
            } else {
                // Recarga normal
                staminaActual += atributos.getRecargaStaminaPorSegundo() * delta;
                if (staminaActual > atributos.getStaminaMaxima()) staminaActual = atributos.getStaminaMaxima();
            }
        }

        distanciaEsteFrame = velocidad * delta;
    }

    private void moverYActualizarDireccion(boolean izquierda, boolean derecha, boolean arriba, boolean abajo) {
        estaMoviendo = izquierda || derecha || arriba || abajo;
        if (!estaMoviendo) return;

        direccionActual = calcularDireccion(izquierda, derecha, arriba, abajo);

        float dx = 0f, dy = 0f;
        if (izquierda) dx -= 1f;
        if (derecha)   dx += 1f;
        if (arriba)    dy += 1f;
        if (abajo)     dy -= 1f;

        // Normalizar para que diagonal no sea más rápida
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len != 0f) { dx /= len; dy /= len; }

        x += dx * distanciaEsteFrame;
        y += dy * distanciaEsteFrame;

        // Mantener hitbox alineada con el sprite
        hitbox.setPosition(x + hbOffsetX, y + hbOffsetY);
    }

    // =========================
    // API pública (llamada por el Intérprete de Entrada)
    // =========================
    public void actualizarEstadoJugador(boolean arriba, boolean abajo, boolean izquierda, boolean derecha,
                                        boolean sprint, float delta) {
        actualizarVelocidadYStamina(sprint, delta);
        moverYActualizarDireccion(izquierda, derecha, arriba, abajo);
    }

    // Compatibilidad con código antiguo (sin sprint)
    public void moverDesdeEntrada(boolean arriba, boolean abajo, boolean izquierda, boolean derecha, float delta) {
        distanciaEsteFrame = atributos.getVelocidadBase() * delta;
        moverYActualizarDireccion(izquierda, derecha, arriba, abajo);
    }

    public void actualizar(float delta) {
        if (estaMoviendo) tiempoAnimacion += delta;
    }

    public void dibujar(SpriteBatch pincel) {
        TextureRegion cuadro = estaMoviendo
                ? animaciones.get(direccionActual).getKeyFrame(tiempoAnimacion, true)
                : frameQuieto;
        pincel.draw(cuadro, x, y, ancho, alto);
    }

    public void limitarMovimiento(float anchoMundo, float altoMundo) {
        x = MathUtils.clamp(x, 0, anchoMundo - ancho);
        y = MathUtils.clamp(y, 0, altoMundo - alto);
        hitbox.setPosition(x + hbOffsetX, y + hbOffsetY);
    }

    // =========================
    // Getters / Setters útiles
    // =========================
    public String getNombre() { return nombre; }

    public void setEspacioPresionado(boolean valor) { this.espacioPresionado = valor; }
    public boolean estaEspacioPresionado() { return espacioPresionado; }

    public Direccion getDireccion() { return direccionActual; }
    public Rectangle getHitbox()    { return hitbox; }

    public float getStaminaActual() { return staminaActual; }
    public float getStaminaMaxima() { return atributos.getStaminaMaxima(); }

    public float getX() { return x; }
    public float getY() { return y; }

    public void setPosicion(float nuevaX, float nuevaY) {
        this.x = nuevaX;
        this.y = nuevaY;
        hitbox.setPosition(x + hbOffsetX, y + hbOffsetY);
    }

    public float getAncho() { return ancho; }
    public float getAlto()  { return alto; }

    public AtributosPersonaje getAtributos() { return atributos; }
    public float getEscala() { return escala; }

    public HudPersonaje getHud() { return hud; }
    public TextureRegion obtenerFrameActual() {
        return estaMoviendo
                ? animaciones.get(direccionActual).getKeyFrame(tiempoAnimacion, true)
                : frameQuieto;
    }

    // =========================
    // Limpieza de recursos
    // =========================
    public void dispose() {
        if (texturaQuieto != null) texturaQuieto.dispose();

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

















































/*package com.championsita.modelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.championsita.visuales.DibujadorHud;

import java.util.EnumMap;
import java.util.Map;

public abstract class Personaje {

    // === HUD  === //
    public DibujadorHud hud;

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
    public Personaje(String nombre, ConfiguracionPersonaje config, float escala,
                     float velocidadBase, float velocidadSprint, float staminaMax) {

        this.nombre = nombre;
        this.escala = escala;
        this.velocidadBase = velocidadBase;
        this.velocidadSprint = velocidadSprint;
        this.staminaMax = staminaMax;
        this.stamina = staminaMax;

        hud = new DibujadorHud(this);

        // Textura quieto
        textureQuieto = new Texture(config.getTexturaQuieto());
        frameQuieto = new TextureRegion(textureQuieto);

        // Sheets desde config (no hardcodeamos rutas)
        sheetDerecha         = new Texture(config.getSheetDerecha());
        sheetIzquierda       = new Texture(config.getSheetIzquierda());
        sheetArriba          = new Texture(config.getSheetArriba());
        sheetAbajo           = new Texture(config.getSheetAbajo());
        sheetArribaDerecha   = new Texture(config.getSheetArribaDerecha());
        sheetArribaIzquierda = new Texture(config.getSheetArribaIzquierda());
        sheetAbajoDerecha    = new Texture(config.getSheetAbajoDerecha());
        sheetAbajoIzquierda  = new Texture(config.getSheetAbajoIzquierda());

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
*/