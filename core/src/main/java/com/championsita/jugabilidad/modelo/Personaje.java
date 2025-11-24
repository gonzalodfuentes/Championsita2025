package com.championsita.jugabilidad.modelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.championsita.jugabilidad.sistemas.DiccionarioHabilidades;
import com.championsita.jugabilidad.sistemas.ModificadorHabilidad;

import java.util.EnumMap;
import java.util.HashMap;
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
    private Equipo equipo;



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
    private boolean sprintActivo = false;
    private Direccion direccionActual = Direccion.ABAJO;


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

    // =========================
// Habilidades especiales
// =========================
    private HabilidadesEspeciales habilidadActual = HabilidadesEspeciales.NEUTRO;

    // Modificador que contiene todos los cambios de esa habilidad
    private ModificadorHabilidad modificador = DiccionarioHabilidades.obtener(HabilidadesEspeciales.NEUTRO);

    // Timers de efectos especiales
    private float timerCongelado = 0f;   // PEQUEÑIN
    private float timerLentitud = 0f;    // ATLETA
    private float timerBuffVelocidad = 0f;  // EXTREMISTA (+25%)
    private float timerDebuffVelocidad = 0f; // EXTREMISTA (-25%)


    private final Map<Direccion, Animation<TextureRegion>> animaciones = new EnumMap<>(Direccion.class);

    // =========================
    // Constructor
    // =========================
    public Personaje(String nombre,
                     ConfiguracionPersonaje sprites,
                     AtributosPersonaje atributos,
                     float escala) {

        hud = new HudPersonaje(this);
        this.nombre   = nombre;
        this.sprites  = sprites;
        this.atributos = atributos;
        this.escala   = escala;
        this.hud = new HudPersonaje(this);
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

        // ============================================
        // 0. Reducir timers si están activos
        // ============================================
        if (timerCongelado > 0) {
            timerCongelado -= delta;
            if (timerCongelado < 0) timerCongelado = 0;
        }

        if (timerLentitud > 0) {
            timerLentitud -= delta;
            if (timerLentitud < 0) timerLentitud = 0;
        }

        if (timerBuffVelocidad > 0) {
            timerBuffVelocidad -= delta;
            if (timerBuffVelocidad < 0) timerBuffVelocidad = 0;
        }

        if (timerDebuffVelocidad > 0) {
            timerDebuffVelocidad -= delta;
            if (timerDebuffVelocidad < 0) timerDebuffVelocidad = 0;
        }

        // ============================================
        // 1. CONGELAMIENTO — PEQUEÑÍN
        // ============================================
        if (habilidadActual == HabilidadesEspeciales.PEQUEÑIN && staminaActual <= 0) {
            if (timerCongelado <= 0) {
                timerCongelado = 1.0f;   // 1 segundo congelado
            }
        }

        if (timerCongelado > 0) {
            distanciaEsteFrame = 0; // NO SE PUEDE MOVER
            return;
        }

        // ============================================
        // 2. Calcular velocidad BASE o SPRINT
        // ============================================
        float velocidad = atributos.getVelocidadBase();

//        System.out.println(staminaActual);

        if (sprint && staminaActual > 0f) {
            velocidad = atributos.getVelocidadSprint();
            staminaActual -= atributos.getConsumoSprintPorSegundo() * delta;
            if (staminaActual < 0f) staminaActual = 0f;

            // Si cayó bajo el límite, aplicar bloqueo
            if (staminaActual < atributos.getLimiteStaminaBloqueo()) {
                velocidad = atributos.getVelocidadBase();
                bloqueoRecarga = true;
                tiempoBloqueoRecarga = 0f;
            }
        } else {
            // Recarga o bloqueo
            if (bloqueoRecarga) {
                if (!sprint) {
                    tiempoBloqueoRecarga += delta;
                    if (tiempoBloqueoRecarga >= atributos.getTiempoBloqueoRecargaSegundos()) {
                        bloqueoRecarga = false;
                    }
                }
            } else {
                staminaActual += atributos.getRecargaStaminaPorSegundo() * delta;
                if (staminaActual > atributos.getStaminaMaxima()) staminaActual = atributos.getStaminaMaxima();
            }
        }

        // ============================================
        // 3. LENTITUD — ATLETA
        // ============================================
        if (habilidadActual == HabilidadesEspeciales.ATLETA && staminaActual <= 0) {
            if (timerLentitud <= 0) {
                timerLentitud = 2.0f;  // 2 segundos lento
            }
        }
        if (timerLentitud > 0) {
            velocidad *= 0.6f; // aplicar lentitud
        }

        // ============================================
        // 4. EXTREMISTA — Buff y Debuff
        // ============================================
        if (timerBuffVelocidad > 0) velocidad *= 1.25f;
        if (timerDebuffVelocidad > 0) velocidad *= 0.75f;

        // ============================================
        // 5. Guardar distancia final del frame
        // ============================================
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

    public void asignarHabilidad(HabilidadesEspeciales habilidad) {
        this.habilidadActual = habilidad;
        this.modificador = DiccionarioHabilidades.obtener(habilidad);

        // Resetear timers por si el personaje cambia de habilidad (modo especial)
        timerCongelado = 0f;
        timerLentitud = 0f;
        timerBuffVelocidad = 0f;
        timerDebuffVelocidad = 0f;
    }

    public void aplicarEfectosPermanentesDeHabilidad() {

        // === 1. Reescalar sprite (ancho / alto) ===
        this.ancho *= modificador.escalaSprite;
        this.alto  *= modificador.escalaSprite;

        // === 2. Reescalar hitbox ===
        this.hbAncho *= modificador.escalaSprite;
        this.hbAlto  *= modificador.escalaSprite;
        hitbox.setSize(hbAncho, hbAlto);

        // === 3. Actualizar atributos del personaje ===
        this.atributos.update(
                atributos.getVelocidadBase() * modificador.velocidadBase,
                atributos.getVelocidadSprint() * modificador.velocidadSprint,
                atributos.getStaminaMaxima() * modificador.staminaMax,
                atributos.getConsumoSprintPorSegundo() * modificador.consumoSprint,
                atributos.getRecargaStaminaPorSegundo() * modificador.recargaStamina,
                atributos.getLimiteStaminaBloqueo(),
                atributos.getTiempoBloqueoRecargaSegundos()
        );

        // === 4. Reset de stamina al nuevo máximo ===
        this.staminaActual = atributos.getStaminaMaxima();
    }

    public void activarBuffVelocidad(float duracion) {
        timerBuffVelocidad = duracion;
    }

    public void activarDebuffVelocidad(float duracion) {
        timerDebuffVelocidad = duracion;
    }


    // =========================
    // API pública (llamada por el Intérprete de Entrada)
    // =========================
    public void actualizarEstadoJugador(boolean arriba, boolean abajo, boolean izquierda, boolean derecha,
                                        boolean sprint, float delta) {
        actualizarVelocidadYStamina(sprint, delta);
        moverYActualizarDireccion(izquierda, derecha, arriba, abajo);
        this.sprintActivo = sprint;
    }

    // Compatibilidad con código antiguo (sin sprint)
    public void moverDesdeEntrada(boolean arriba, boolean abajo, boolean izquierda, boolean derecha, float delta) {
        distanciaEsteFrame = atributos.getVelocidadBase() * delta;
        moverYActualizarDireccion(izquierda, derecha, arriba, abajo);
    }

    public void actualizar(float delta) {
        if (estaMoviendo) tiempoAnimacion += delta;
        if (timerCongelado > 0) timerCongelado -= delta;
        if (timerLentitud > 0) timerLentitud -= delta;
        if (timerBuffVelocidad > 0) timerBuffVelocidad -= delta;
        if (timerDebuffVelocidad > 0) timerDebuffVelocidad -= delta;
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

    public HabilidadesEspeciales getHabilidadActual() { return habilidadActual; }

    public void setEquipo(Equipo eq){ this.equipo = eq;};
    public Equipo getEquipo(){
        return this.equipo;
    };


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

    public boolean estaSprintPresionado() {
        return sprintActivo;
    }
}
