package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.championsita.partida.modosdejuego.ControladorPosicionesIniciales;

public class ModoFutsal extends ModoRebote{

    private InputMultiplexer multiplexer;


    @Override
    protected void onIniciar() {

        multiplexer = new InputMultiplexer();
        ControladorPosicionesIniciales.PosicionarJugadoresYPelota(ctx, multiplexer);
    }

    @Override
    public void actualizar(float delta) {

        super.actualizar(delta);
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

