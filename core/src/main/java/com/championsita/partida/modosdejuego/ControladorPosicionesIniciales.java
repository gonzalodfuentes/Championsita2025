package com.championsita.partida.modosdejuego;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.partida.nucleo.Contexto;

public abstract class ControladorPosicionesIniciales {

    public static void PosicionarJugadoresYPelota(Contexto ctx, InputMultiplexer multiplexer){
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
                    Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D,
                    Input.Keys.CONTROL_LEFT, Input.Keys.SHIFT_LEFT
            ));
        }
        if (ctx.jugadores.get(1) != null) {
            ctx.controles.add(new EntradaJugador(
                    ctx.jugadores.get(1),
                    Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT,
                    Input.Keys.CONTROL_RIGHT, Input.Keys.SHIFT_RIGHT));
        }


        for(EntradaJugador controlesJugador : ctx.controles){
            if(controlesJugador != null){
                multiplexer.addProcessor(controlesJugador);
            }
        }
    }
}

