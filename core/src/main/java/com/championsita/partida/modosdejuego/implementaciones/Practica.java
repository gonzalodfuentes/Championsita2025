package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.partida.nucleo.Contexto;
import com.championsita.partida.modosdejuego.ModoDeJuego;

/**
 * Modo de práctica con un solo jugador.
 * Controla entrada, físicas y colisiones solo para jugador1 y la pelota.
 */
public class Practica implements ModoDeJuego {

    private Contexto ctx;
    private boolean terminado = false;
    private int cantidadDeJugadores = 1;

    @Override
    public void iniciar(Contexto contexto) {
        this.ctx = contexto;

        // Posicionamiento inicial recomendado para práctica
        if (ctx.jugadores.get(0) != null) ctx.jugadores.get(0).setPosicion(2.0f, 2.5f);

        // Entrada solo para jugador1 (WASD + CTRL/SHIFT izquierdos)
        ctx.controles.add(
                new EntradaJugador(
                        ctx.jugadores.get(0),
                        Keys.W, Keys.S, Keys.A, Keys.D,
                        Keys.CONTROL_LEFT,
                        Keys.SHIFT_LEFT
                )
        );
    }

    @Override
    public void actualizar(float delta) {
        // Entrada
        if (ctx.controles.get(0) != null) ctx.controles.get(0).actualizar(delta);

        // Físicas jugador1
        ctx.fisica.actualizarPersonaje(ctx.jugadores.get(0), delta);

        // Límites de mundo
        float W = ctx.viewport.getWorldWidth();
        float H = ctx.viewport.getWorldHeight();
        ctx.fisica.limitarPersonajeAlMundo(ctx.jugadores.get(0), W, H);

        // Colisión jugador1 ↔ pelota
        ctx.colisiones.procesarContactoPelotaConJugador(ctx.pelota, ctx.jugadores.get(0));

        // Físicas de la pelota
        ctx.fisica.actualizarPelota(ctx.pelota, delta);

        // Este modo no finaliza automáticamente
        terminado = false;
    }

    @Override
    public void renderizar(SpriteBatch batch) {
        // HUD opcional de práctica (por ahora vacío)
        // Ejemplo futuro: texto "Práctica" o ayuda de controles.
    }

    @Override
    public InputProcessor getProcesadorEntrada() {
        return ctx.controles.get(0);
    }

    @Override
    public boolean finalizado() {
        return terminado;
    }

    @Override
    public int getCantidadDeJugadores() {
        return cantidadDeJugadores;
    }

    @Override
    public void liberar() {
        // Sin recursos propios por ahora
    }
}
