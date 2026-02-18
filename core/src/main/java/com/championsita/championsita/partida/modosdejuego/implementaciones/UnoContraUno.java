package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.partida.modosdejuego.ControladorPosicionesIniciales;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.nucleo.ContextoModoDeJuego;

/**
 * Modo 1 vs 1 local.
 * Controla entrada, físicas y colisiones para los dos jugadores y la pelota.
 */
public class UnoContraUno extends ModoBase {

    private InputMultiplexer multiplexer;

    @Override
    protected void onIniciar() {
        multiplexer = new InputMultiplexer();
        ControladorPosicionesIniciales.PosicionarJugadoresYPelota(ctx, multiplexer);
    }

    @Override
    public InputProcessor getProcesadorEntrada() {
        return multiplexer;
    }

    @Override
    public int getCantidadDeJugadores() {
        return 2;
    }
}


