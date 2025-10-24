package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.nucleo.Contexto;

/**
 * Modo 1 vs 1 local.
 * Controla entrada, físicas y colisiones para los dos jugadores y la pelota.
 */
public class UnoContraUno implements ModoDeJuego {

    private Contexto ctx;
    private EntradaJugador entradaJugador1;
    private EntradaJugador entradaJugador2;
    private InputMultiplexer multiplexer;
    private boolean terminado = false;

    @Override
    public void iniciar(Contexto contexto) {
        this.ctx = contexto;

        // Posiciones iniciales simples en extremos opuestos
        if (ctx.jugador1 != null) ctx.jugador1.setPosicion(2.0f, ctx.viewport.getWorldHeight() / 2f);
        if (ctx.jugador2 != null) ctx.jugador2.setPosicion(ctx.viewport.getWorldWidth() - 2.0f, ctx.viewport.getWorldHeight() / 2f);
        if (ctx.pelota != null)   ctx.pelota.setPosicion(ctx.viewport.getWorldWidth() / 2f, ctx.viewport.getWorldHeight() / 2f);

        // Entradas: jugador1 (WASD + CTRL/SHIFT izq), jugador2 (IJKL + CTRL/SHIFT der)
        if (ctx.jugador1 != null) {
            this.entradaJugador1 = new EntradaJugador(
                    ctx.jugador1,
                    Keys.W, Keys.S, Keys.A, Keys.D,
                    Keys.CONTROL_LEFT, Keys.SHIFT_LEFT
            );
        }
        if (ctx.jugador2 != null) {
            this.entradaJugador2 = new EntradaJugador(
                    ctx.jugador2,
                    Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT,
                    Keys.CONTROL_RIGHT, Keys.SHIFT_RIGHT
            );
        }

        this.multiplexer = new InputMultiplexer();
        if (entradaJugador1 != null) multiplexer.addProcessor(entradaJugador1);
        if (entradaJugador2 != null) multiplexer.addProcessor(entradaJugador2);
    }

    @Override
    public void actualizar(float delta) {
        // Entradas
        if (entradaJugador1 != null) entradaJugador1.actualizar(delta);
        if (entradaJugador2 != null) entradaJugador2.actualizar(delta);

        // Físicas jugadores
        if (ctx.jugador1 != null) ctx.fisica.actualizarPersonaje(ctx.jugador1, delta);
        if (ctx.jugador2 != null) ctx.fisica.actualizarPersonaje(ctx.jugador2, delta);

        // Límites de mundo
        float W = ctx.viewport.getWorldWidth();
        float H = ctx.viewport.getWorldHeight();
        if (ctx.jugador1 != null) ctx.fisica.limitarPersonajeAlMundo(ctx.jugador1, W, H);
        if (ctx.jugador2 != null) ctx.fisica.limitarPersonajeAlMundo(ctx.jugador2, W, H);

        // Colisiones: pelota con cada jugador
        if (ctx.pelota != null && ctx.jugador1 != null)
            ctx.colisiones.procesarContactoPelotaConJugador(ctx.pelota, ctx.jugador1);
        if (ctx.pelota != null && ctx.jugador2 != null)
            ctx.colisiones.procesarContactoPelotaConJugador(ctx.pelota, ctx.jugador2);

        // Físicas pelota
        if (ctx.pelota != null) ctx.fisica.actualizarPelota(ctx.pelota, delta);

        // Criterio de fin: por ahora nunca (se manejará por marcador en futuro)
        terminado = false;
    }

    @Override
    public void renderizar(SpriteBatch batch) {
        // HUD opcional (por ahora vacío). Ej: marcador, tiempo, etc.
    }

    @Override
    public InputProcessor getProcesadorEntrada() {
        return multiplexer;
    }

    @Override
    public boolean finalizado() {
        return terminado;
    }

    @Override
    public void liberar() {
        // Sin recursos propios por ahora
    }
}
