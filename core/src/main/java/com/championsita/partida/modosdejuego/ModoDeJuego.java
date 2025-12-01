package com.championsita.partida.modosdejuego;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.partida.nucleo.Contexto;

/**
 * Define el contrato base para todos los modos de juego.
 * Cada modo controla su propia lógica, entrada y finalización.
 */
public interface ModoDeJuego {

    // Se llama al crear el modo. Inicializa todo lo necesario.
    void iniciar(Contexto contexto);

    // Actualiza la lógica del modo (entradas, físicas, reglas, etc).
    void actualizar(float delta);

    // Dibuja elementos adicionales del modo (HUD, textos, etc).
    void renderizar(SpriteBatch batch);

    // Devuelve el procesador de entrada activo del modo.
    InputProcessor getProcesadorEntrada();

    // Indica si el modo ya terminó (por ejemplo, fin del partido).
    boolean finalizado();

    // Libera los recursos propios del modo.
    void liberar();

    // Proporcionar cantidad de jugadores para el ControladorDePartida
    int getCantidadDeJugadores();
}
