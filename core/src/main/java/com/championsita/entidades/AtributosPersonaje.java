package com.championsita.entidades;

public class AtributosPersonaje {

    // =========================
    // Constantes de jugabilidad
    // =========================
    private final float velocidadBase;              // velocidad normal
    private final float velocidadSprint;            // velocidad corriendo
    private final float staminaMaxima;              // stamina total
    private final float consumoSprintPorSegundo;    // cuánto stamina gasta por segundo al sprintar
    private final float recargaStaminaPorSegundo;   // cuánto stamina recupera por segundo
    private final float limiteStaminaBloqueo;       // debajo de este % bloquea la recarga un tiempo
    private final float tiempoBloqueoRecargaSegundos; // cuánto tiempo se bloquea la recarga

    // =========================
    // Constructor
    // =========================
    public AtributosPersonaje(float velocidadBase,
                              float velocidadSprint,
                              float staminaMaxima,
                              float consumoSprintPorSegundo,
                              float recargaStaminaPorSegundo,
                              float limiteStaminaBloqueo,
                              float tiempoBloqueoRecargaSegundos) {
        this.velocidadBase = velocidadBase;
        this.velocidadSprint = velocidadSprint;
        this.staminaMaxima = staminaMaxima;
        this.consumoSprintPorSegundo = consumoSprintPorSegundo;
        this.recargaStaminaPorSegundo = recargaStaminaPorSegundo;
        this.limiteStaminaBloqueo = limiteStaminaBloqueo;
        this.tiempoBloqueoRecargaSegundos = tiempoBloqueoRecargaSegundos;
    }

    // =========================
    // Getters
    // =========================
    public float getVelocidadBase() { return velocidadBase; }
    public float getVelocidadSprint() { return velocidadSprint; }
    public float getStaminaMaxima() { return staminaMaxima; }
    public float getConsumoSprintPorSegundo() { return consumoSprintPorSegundo; }
    public float getRecargaStaminaPorSegundo() { return recargaStaminaPorSegundo; }
    public float getLimiteStaminaBloqueo() { return limiteStaminaBloqueo; }
    public float getTiempoBloqueoRecargaSegundos() { return tiempoBloqueoRecargaSegundos; }
}
