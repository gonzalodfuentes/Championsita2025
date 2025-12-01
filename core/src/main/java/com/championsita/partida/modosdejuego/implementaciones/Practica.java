package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.championsita.jugabilidad.entrada.EntradaJugador;

/**
 * Modo de práctica con un solo jugador.
 * Controla entrada, físicas y colisiones solo para jugador1 y la pelota.
 */
public class Practica extends ModoBase {

    @Override
    protected void onIniciar() {
        ctx.jugadores.get(0).setPosicion(2f, 2.5f);

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
    public InputProcessor getProcesadorEntrada() {
        return ctx.controles.get(0);
    }

    @Override
    public int getCantidadDeJugadores() {
        return 1;
    }
}
