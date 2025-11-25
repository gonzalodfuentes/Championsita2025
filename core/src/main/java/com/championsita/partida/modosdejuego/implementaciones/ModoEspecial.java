package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.*;
import com.championsita.jugabilidad.modelo.Equipo;
import com.championsita.jugabilidad.modelo.HabilidadesEspeciales;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.partida.modosdejuego.ControladorPosicionesIniciales;

import java.util.ArrayList;


public class ModoEspecial extends ModoBase {

    private InputMultiplexer multiplexer;
    private final ArrayList<Personaje> ordenSeleccion = new ArrayList<>();
    private final ArrayList<HabilidadesEspeciales> habilidadesDisponibles = new ArrayList<>();
    private final ArrayList<HabilidadesEspeciales> habilidadesUsadas = new ArrayList<>();
    private int indice = 0;
    private boolean seleccionTerminada = false;

    @Override
    protected void onIniciar() {

        multiplexer = new InputMultiplexer();

        // asegurarse de que tengan equipo
        if (ctx.jugadores.get(0).getEquipo() == null) {
            ctx.jugadores.get(0).setEquipo(Equipo.ROJO);
            ctx.jugadores.get(1).setEquipo(Equipo.AZUL);
        }

        ordenSeleccion.addAll(ctx.controlador.getJugadoresDelEquipo(Equipo.ROJO));
        ordenSeleccion.addAll(ctx.controlador.getJugadoresDelEquipo(Equipo.AZUL));

        for (HabilidadesEspeciales h : HabilidadesEspeciales.values()) {
            if (h != HabilidadesEspeciales.NEUTRO)
                habilidadesDisponibles.add(h);
        }

        ControladorPosicionesIniciales.PosicionarJugadoresYPelota(ctx, multiplexer);
    }

    @Override
    public void actualizar(float delta) {

        if (!seleccionTerminada) {
            seleccionarHabilidadesEspeciales();
        }

        super.actualizar(delta); // el resto es igual para todos
    }

    private void seleccionarHabilidadesEspeciales() {
        // jugador actual que debe elegir
        Personaje pj = ordenSeleccion.get(indice);

        // lista de habilidades seleccionadas desde el menú
        ArrayList<HabilidadesEspeciales> elecciones = ctx.habilidadesEspeciales;

        if (!elecciones.isEmpty()) {

            HabilidadesEspeciales habilidadElegida = elecciones.get(indice);

            boolean usada = habilidadesUsadas.contains(habilidadElegida);

            // condición original pero corregida
            if (habilidadElegida == HabilidadesEspeciales.NEUTRO || !usada) {

                // asignar
                pj.asignarHabilidad(habilidadElegida);
                pj.aplicarEfectosPermanentesDeHabilidad();

                if (habilidadElegida != HabilidadesEspeciales.NEUTRO)
                    habilidadesUsadas.add(habilidadElegida);

                // siguiente jugador
                indice++;

                if (indice >= ordenSeleccion.size()) {
                    seleccionTerminada = true;
                }
            }
        }
    }

    @Override
    public InputProcessor getProcesadorEntrada() {
        return multiplexer;
    }
}

