package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.jugabilidad.modelo.Personaje;
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

    @Override
    public void iniciar(Contexto contexto) {

        this.ctx = contexto;

        // Posiciones iniciales simples en extremos opuestos
        if(ctx.jugadores.get(0) != null)
            ctx.jugadores.get(0).setPosicion(2.0f, ctx.viewport.getWorldHeight() / 2f);
        if(ctx.jugadores.get(1) != null)
            ctx.jugadores.get(1).setPosicion(5.5f, ctx.viewport.getWorldHeight() / 2f);

        // Posicion de la pelota en el medio
        if (ctx.pelota != null)   ctx.pelota.setPosicion(ctx.viewport.getWorldWidth() / 2f, ctx.viewport.getWorldHeight() / 2f);

        // Entradas: jugador1 (WASD + CTRL/SHIFT izq), jugador2 (IJKL + CTRL/SHIFT der)
        if (ctx.jugadores.get(0) != null) {
            ctx.controles.add(new EntradaJugador(
                    ctx.jugadores.get(0),
                    Keys.W, Keys.S, Keys.A, Keys.D,
                    Keys.CONTROL_LEFT, Keys.SHIFT_LEFT
            ));
        }
        if (ctx.jugadores.get(1) != null) {
            ctx.controles.add(new EntradaJugador(
                    ctx.jugadores.get(1),
                    Keys.UP, Keys.DOWN, Keys.LEFT, Keys.RIGHT,
                    Keys.CONTROL_RIGHT, Keys.SHIFT_RIGHT));
        }

        this.multiplexer = new InputMultiplexer();
        for(EntradaJugador controlesJugador : ctx.controles){
            if(controlesJugador != null){
                multiplexer.addProcessor(controlesJugador);
            }
        }
    }

    @Override
    public void actualizar(float delta) {
        // Entradas
        for(EntradaJugador controlesJugador : ctx.controles){
            if(controlesJugador != null){
                controlesJugador.actualizar(delta);
            }
        }

        // Físicas jugadores
        if(ctx.jugadores.get(0) != null && ctx.jugadores.get(1) != null)
            ctx.colisiones.separarJugadoresSiChocan(ctx.jugadores.get(0), ctx.jugadores.get(1));
            ctx.fisica.actualizarPersonaje(ctx.jugadores.get(0), delta);
            ctx.fisica.actualizarPersonaje(ctx.jugadores.get(1), delta);

        // Límites de mundo
        float W = ctx.viewport.getWorldWidth();
        float H = ctx.viewport.getWorldHeight();
        for(Personaje jugador : ctx.jugadores){
            if(jugador != null){
                ctx.fisica.limitarPersonajeAlMundo(jugador, W, H);
            }
        }

        // Colisiones: pelota con cada jugador
        for(Personaje jugador : ctx.jugadores){
            if(jugador != null && ctx.pelota != null){
                ctx.colisiones.procesarContactoPelotaConJugador(ctx.pelota, jugador);
            }
        }
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

    @Override
    public int getCantidadDeJugadores(){
        return this.cantidadDeJugadores;
    }
}

