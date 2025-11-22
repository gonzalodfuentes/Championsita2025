package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.jugabilidad.modelo.Equipo;
import com.championsita.jugabilidad.modelo.HabilidadesEspeciales;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.partida.ControladorDePartida;
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
    ControladorDePartida controlador;
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

        System.out.println("DEBUG — jugadores: " + ctx.jugadores.size());
        System.out.println("DEBUG — ROJO: " + ctx.controlador.getJugadoresDelEquipo(Equipo.ROJO).size());
        System.out.println("DEBUG — AZUL: " + ctx.controlador.getJugadoresDelEquipo(Equipo.AZUL).size());
        System.out.println("DEBUG — ordenSeleccion: " + ordenSeleccion.size());

        habilidadesDisponibles = new ArrayList<>();
        habilidadesYaUsadas = new ArrayList<>();

        // Cargar todas las habilidades EXCEPTO NEUTRO
        for (HabilidadesEspeciales habilidades : HabilidadesEspeciales.values()) {
            if (habilidades != HabilidadesEspeciales.NEUTRO)
                habilidadesDisponibles.add(habilidades);
        }

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

        this.multiplexer = new InputMultiplexer();
        for(EntradaJugador controlesJugador : ctx.controles){
            if(controlesJugador != null){
                multiplexer.addProcessor(controlesJugador);
            }
        }

    }

    @Override
    public void actualizar(float delta) {
        if (!seleccionTerminada) {

            Personaje pj = ordenSeleccion.get(indiceSeleccionActual);

            HabilidadesEspeciales eleccion = leerEleccionDelJugador();

            if (eleccion != null) {

                if (eleccion == HabilidadesEspeciales.NEUTRO ||
                        !habilidadesYaUsadas.contains(eleccion)) {

                    pj.asignarHabilidad(eleccion);
                    pj.aplicarEfectosPermanentesDeHabilidad();

                    if (eleccion != HabilidadesEspeciales.NEUTRO)
                        habilidadesYaUsadas.add(eleccion);

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

        // 7. Verificar gol y activar EXTREMISTA
        ctx.partido.verificarSiHayGol(ctx.pelota, ctx.cancha);

        // 8. Fin de modo (si querés)
        //terminado = false; // Por ahora, nunca termina automático
    }


    private HabilidadesEspeciales leerEleccionDelJugador() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) return HabilidadesEspeciales.PEQUEÑIN;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) return HabilidadesEspeciales.GRANDOTE;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) return HabilidadesEspeciales.ZURDO;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) return HabilidadesEspeciales.DIESTRO;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) return HabilidadesEspeciales.EMPUJON;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) return HabilidadesEspeciales.ATLETA;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) return HabilidadesEspeciales.EXTREMISTA;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) return HabilidadesEspeciales.NEUTRO;
        return null;

    }

    @Override
    public void renderizar(SpriteBatch batch) {

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
