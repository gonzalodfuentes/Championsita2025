package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;

import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.nucleo.Contexto;

import java.util.ArrayList;
import java.util.HashSet;

public class ModoEspecial implements ModoDeJuego {

    private Contexto ctx;
    private InputMultiplexer multiplexer;

    private boolean terminado = false;
    private int cantidadDeJugadores;

    // acá guardamos qué habilidades ya se eligieron
    private HashMap<HabilidadEspecial> habilidadesUsadas = new HashMap<>();

    // estado del proceso de selección
    private int jugadorActualSeleccionando = 0;
    private boolean todosSeleccionaron = false;

    public ModoEspecial(int cantidadDeJugadores) {
        this.cantidadDeJugadores = cantidadDeJugadores;
    }

    @Override
    public void iniciar(Contexto contexto) {
        this.ctx = contexto;

        // 1. posiciones iniciales (tentativas)
        posicionarJugadores();

        // 2. seleccionar habilidades ANTES de empezar el partido
        iniciarSeleccionDeHabilidades();

        // 3. procesadores de entrada
        multiplexer = new InputMultiplexer();
        for (EntradaJugador ej : ctx.controles) {
            if (ej != null) multiplexer.addProcessor(ej);
        }
    }

    private void posicionarJugadores() {
        float H = ctx.viewport.getWorldHeight();
        float W = ctx.viewport.getWorldWidth();

        for (int i = 0; i < ctx.jugadores.size(); i++) {
            Personaje pj = ctx.jugadores.get(i);
            if (pj == null) continue;

            // posicionamiento simple temporal
            float y = H / 2f;
            float x = (i < cantidadDeJugadores/2) ? 2f : W - 2f;
            pj.setPosicion(x, y);
        }
    }

    private void iniciarSeleccionDeHabilidades() {
        // Señor, acá deberías abrir una pantalla overlay o HUD
        // donde el jugador va eligiendo habilidad.
        //
        // Por ahora lo dejo como estado interno y vos después
        // dibujarás el menú en renderizar().
    }

    @Override
    public void actualizar(float delta) {

        // 1. si aún no seleccionaron habilidades → no hay partido
        if (!todosSeleccionaron) {
            actualizarSeleccion(delta);
            return;
        }

        // 2. Entradas
        for (EntradaJugador ej : ctx.controles) {
            if (ej != null) ej.actualizar(delta);
        }

        // 3. Físicas y límites
        float W = ctx.viewport.getWorldWidth();
        float H = ctx.viewport.getWorldHeight();

        // Todos rebotan en paredes → no hay “salida”
        for (Personaje j : ctx.jugadores) {
            ctx.fisica.actualizarPersonaje(j, delta);
            ctx.fisica.rebotarLaPelotaEnLosBordes(j, W, H);
        }

        // pelota también rebota
        ctx.fisica.actualizarPelota(ctx.pelota, delta);
        ctx.fisica.rebotarLaPelotaEnLosBordes(ctx.pelota, W, H);

        // 4. Colisiones especiales
        for (Personaje j : ctx.jugadores) {
            ctx.colisiones.procesarContactoPelotaConJugador(ctx.pelota, j);
        }

        // 5. Lógica de goles
        ctx.partido.verificarSiHayGol(ctx.pelota, ctx.cancha);

        terminado = false;
    }

    private void actualizarSeleccion(float delta) {
        // Señor, acá falta el HUD y la lógica:
        // mostrar habilidades, elegir una por turno,
        // evitar repetidas,
        // aplicar stats al personaje.
    }

    @Override
    public void renderizar(SpriteBatch batch) {
        if (!todosSeleccionaron) {
            // acá deberías dibujar el menú de selección de habilidades
        }
        // HUD especial opcional
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
    public void liberar() {}

    @Override
    public int getCantidadDeJugadores() {
        return cantidadDeJugadores;
    }
}
