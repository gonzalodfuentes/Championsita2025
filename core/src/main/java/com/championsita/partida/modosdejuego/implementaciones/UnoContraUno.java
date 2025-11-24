package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.partida.modosdejuego.ControladorPosicionesIniciales;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.nucleo.Contexto;

/**
 * Modo 1 vs 1 local.
 * Controla entrada, físicas y colisiones para los dos jugadores y la pelota.
 */
public class UnoContraUno implements ModoDeJuego {

    private Contexto ctx;
    private InputMultiplexer multiplexer;
    private boolean terminado = false;
    private int cantidadDeJugadores = 2;
    ControladorPosicionesIniciales controladorPosicionesIniciales;

    @Override
    public void iniciar(Contexto contexto) {

        this.ctx = contexto;
        ControladorPosicionesIniciales.PosicionarJugadoresYPelota(this.ctx, this.multiplexer);

    }

    @Override
    public void actualizar(float delta) {
        // Entradas
        for (EntradaJugador controlesJugador : ctx.controles) {
            if (controlesJugador != null) {
                controlesJugador.actualizar(delta);
            }
        }

        // Físicas jugadores
        if (ctx.jugadores.get(0) != null && ctx.jugadores.get(1) != null)
            ctx.colisiones.separarJugadoresSiChocan(ctx.jugadores.get(0), ctx.jugadores.get(1));
        ctx.fisica.actualizarPersonaje(ctx.jugadores.get(0), delta);
        ctx.fisica.actualizarPersonaje(ctx.jugadores.get(1), delta);

        // Límites de mundo
        float W = ctx.viewport.getWorldWidth();
        float H = ctx.viewport.getWorldHeight();
        for (Personaje jugador : ctx.jugadores) {
            if (jugador != null) {
                ctx.fisica.limitarPersonajeAlMundo(jugador, W, H);
            }
        }

        // Colisiones: pelota con cada jugador
        for (Personaje jugador : ctx.jugadores) {
            if (jugador != null && ctx.pelota != null) {
                ctx.colisiones.procesarContactoPelotaConJugador(ctx.pelota, jugador);
            }
        }
        // Físicas pelota
        if (ctx.pelota != null) ctx.fisica.actualizarPelota(ctx.pelota, delta);

        ctx.partido.verificarSiHayGol(ctx.pelota, ctx.cancha);

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

    @Override
    public int getCantidadDeJugadores() {
        return this.cantidadDeJugadores;
    }
}

