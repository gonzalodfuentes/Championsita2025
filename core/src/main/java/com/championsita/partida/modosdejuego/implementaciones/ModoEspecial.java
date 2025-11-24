package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.jugabilidad.modelo.Cancha;
import com.championsita.jugabilidad.modelo.Equipo;
import com.championsita.jugabilidad.modelo.HabilidadesEspeciales;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.jugabilidad.visuales.HudPartido;
import com.championsita.partida.ControladorDePartida;
import com.championsita.partida.modosdejuego.ControladorPosicionesIniciales;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.nucleo.Contexto;

import java.util.ArrayList;


public class ModoEspecial implements ModoDeJuego {
    private ArrayList<Personaje> ordenSeleccion;
    private int indiceSeleccionActual = 0;
    private InputMultiplexer multiplexer;

    private ArrayList<HabilidadesEspeciales> habilidadesDisponibles;
    private ArrayList<HabilidadesEspeciales> habilidadesYaUsadas;

    private boolean seleccionTerminada = false;
    private Contexto ctx;


    @Override
    public void iniciar(Contexto contexto) {
        this.ctx = contexto;
        // PARCHE: si ningún jugador tiene equipo, asignarlos acá sí o sí.
        if (ctx.jugadores.get(0).getEquipo() == null) {
            ctx.jugadores.get(0).setEquipo(Equipo.ROJO);
            if (ctx.jugadores.size() > 1)
                ctx.jugadores.get(1).setEquipo(Equipo.AZUL);
        }

        // Crear orden según equipo
        ordenSeleccion = new ArrayList<>();
        ordenSeleccion.addAll((this.ctx.controlador.getJugadoresDelEquipo(Equipo.ROJO)));
        ordenSeleccion.addAll(this.ctx.controlador.getJugadoresDelEquipo(Equipo.AZUL));

        //System.out.println("DEBUG — jugadores: " + ctx.jugadores.size());
        //System.out.println("DEBUG — ROJO: " + ctx.controlador.getJugadoresDelEquipo(Equipo.ROJO).size());
        //System.out.println("DEBUG — AZUL: " + ctx.controlador.getJugadoresDelEquipo(Equipo.AZUL).size());
        //System.out.println("DEBUG — ordenSeleccion: " + ordenSeleccion.size());

        habilidadesDisponibles = new ArrayList<>();
        habilidadesYaUsadas = new ArrayList<>();

        // Cargar todas las habilidades EXCEPTO NEUTRO
        for (HabilidadesEspeciales habilidades : HabilidadesEspeciales.values()) {
            if (habilidades != HabilidadesEspeciales.NEUTRO)
                habilidadesDisponibles.add(habilidades);
        }

        this.multiplexer = new InputMultiplexer();
        ControladorPosicionesIniciales.PosicionarJugadoresYPelota(contexto, this.multiplexer);
    }

    @Override
    public void actualizar(float delta) {
        if (!seleccionTerminada) {

            Personaje pj = ordenSeleccion.get(indiceSeleccionActual);

            ArrayList<HabilidadesEspeciales> elecciones = ctx.habilidadesEspeciales;

            if (!elecciones.isEmpty()) {

                if (elecciones.get(indiceSeleccionActual) == HabilidadesEspeciales.NEUTRO ||
                        !habilidadesYaUsadas.contains(elecciones.get(1))) {

                    pj.asignarHabilidad(elecciones.get(indiceSeleccionActual));
                    pj.aplicarEfectosPermanentesDeHabilidad();

                    if (elecciones.get(1) != HabilidadesEspeciales.NEUTRO)
                        habilidadesYaUsadas.add(elecciones.get(indiceSeleccionActual));

                    indiceSeleccionActual++;

                    if (indiceSeleccionActual >= ordenSeleccion.size()) {
                        seleccionTerminada = true;

                    }
                }
            }
        }
        actualizarPartidaNormal(delta);
        return;
    }

    private void actualizarPartidaNormal(float delta) {

        // 1. Entrada
        for(EntradaJugador controlesJugador : ctx.controles){
            if(controlesJugador != null){
                controlesJugador.actualizar(delta);
            }
        }

        // 2. Físicas de jugadores
        for (Personaje pj : ctx.jugadores) {
            ctx.fisica.actualizarPersonaje(pj, delta);
        }

        // 3. Límites del mundo
        float W = ctx.viewport.getWorldWidth();
        float H = ctx.viewport.getWorldHeight();

        for (Personaje pj : ctx.jugadores) {
            ctx.fisica.limitarPersonajeAlMundo(pj, W, H);
        }

        // 4. Colisiones jugador-jugador
        for (int i = 0; i < ctx.jugadores.size(); i++) {
            for (int j = i + 1; j < ctx.jugadores.size(); j++) {
                ctx.colisiones.separarJugadoresSiChocan(
                        ctx.jugadores.get(i),
                        ctx.jugadores.get(j)
                );
            }
        }

        // 5. Colisiones jugador-pelota
        for (Personaje pj : ctx.jugadores) {
            ctx.colisiones.procesarContactoPelotaConJugador(ctx.pelota, pj);
        }

        // 6. Físicas de pelota
        ctx.fisica.actualizarPelota(ctx.pelota, delta);
        ctx.fisica.rebotarLaPelotaEnLosBordes(ctx.pelota, W, H, ctx.cancha);

        // 7. Verificar gol y activar EXTREMISTA
        ctx.partido.verificarSiHayGol(ctx.pelota, ctx.cancha);

        // 8. Fin de modo (si querés)
        //terminado = false; // Por ahora, nunca termina automático
    }

    @Override
    public void renderizar(SpriteBatch batch) {
        //HudPartido dibujadorHudPartido =  new HudPartido(ctx.viewport);
        //dibujadorHudPartido.dibujarHud(batch, ctx.partido);
    }

    @Override
    public InputProcessor getProcesadorEntrada() {
        return multiplexer;
    }

    @Override
    public boolean finalizado() {
        return false;
    }

    @Override
    public void liberar() {

    }

    @Override
    public int getCantidadDeJugadores() {
        return 2;
    }
}
