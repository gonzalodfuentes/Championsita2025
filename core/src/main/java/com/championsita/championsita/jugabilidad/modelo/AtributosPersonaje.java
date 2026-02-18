package com.championsita.jugabilidad.modelo;

public class AtributosPersonaje {

    // =========================
    // Constantes de jugabilidad
    // =========================
    private float velocidadBase;              // velocidad normal
    private float velocidadSprint;            // velocidad corriendo
    private float staminaMaxima;              // stamina total
    private float consumoSprintPorSegundo;    // cuánto stamina gasta por segundo al sprintar
    private float recargaStaminaPorSegundo;   // cuánto stamina recupera por segundo
    private float limiteStaminaBloqueo;       // debajo de este % bloquea la recarga un tiempo
    private float tiempoBloqueoRecargaSegundos; // cuánto tiempo se bloquea la recarga

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

    // =========================
    // Setters
    // =========================
    public void update(float velocidadBase,
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
}
