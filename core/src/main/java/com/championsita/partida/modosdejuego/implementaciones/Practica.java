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
    private EntradaJugador entradaJugador1;
    private boolean terminado = false;

    @Override
    public void iniciar(Contexto contexto) {
        this.ctx = contexto;

        // Posicionamiento inicial recomendado para práctica
        if (ctx.jugador1 != null) ctx.jugador1.setPosicion(2.0f, 2.5f);

        // Ocultar jugador2 de la escena por ahora (no se usa en este modo)
        if (ctx.jugador2 != null) ctx.jugador2.setPosicion(-1000f, -1000f);

        // Entrada solo para jugador1 (WASD + CTRL/SHIFT izquierdos)
        this.entradaJugador1 = new EntradaJugador(
                ctx.jugador1,
                Keys.W, Keys.S, Keys.A, Keys.D,
                Keys.CONTROL_LEFT, Keys.SHIFT_LEFT
        );
    }

    @Override
    public void actualizar(float delta) {
        // Entrada
        if (entradaJugador1 != null) entradaJugador1.actualizar(delta);

        // Físicas jugador1
        ctx.fisica.actualizarPersonaje(ctx.jugador1, delta);

        // Límites de mundo
        float W = ctx.viewport.getWorldWidth();
        float H = ctx.viewport.getWorldHeight();
        ctx.fisica.limitarPersonajeAlMundo(ctx.jugador1, W, H);

        // Colisión jugador1 ↔ pelota
        ctx.colisiones.procesarContactoPelotaConJugador(ctx.pelota, ctx.jugador1);

        // Físicas de la pelota
        ctx.fisica.actualizarPelota(ctx.pelota, delta);

        // Este modo no finaliza automáticamente
        // terminado = false;
    }

    @Override
    public void renderizar(SpriteBatch batch) {
        // HUD opcional de práctica (por ahora vacío)
        // Ejemplo futuro: texto "Práctica" o ayuda de controles.
    }

    @Override
    public InputProcessor getProcesadorEntrada() {
        return this.entradaJugador1;
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
