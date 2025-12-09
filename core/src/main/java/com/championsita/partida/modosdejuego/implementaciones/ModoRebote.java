package com.championsita.partida.modosdejuego.implementaciones;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.championsita.jugabilidad.entrada.EntradaJugador;
import com.championsita.jugabilidad.modelo.Personaje;
import com.championsita.jugabilidad.sistemas.SistemaSaqueLateral;
import com.championsita.partida.modosdejuego.ModoDeJuego;
import com.championsita.partida.nucleo.ContextoModoDeJuego;

public abstract class ModoRebote implements ModoDeJuego {

    protected ContextoModoDeJuego ctx;
    protected boolean terminado = false;

    @Override
    public final void iniciar(ContextoModoDeJuego contextoModoDeJuego) {
        this.ctx = contextoModoDeJuego;

        onIniciar();
    }


    /**
     * Llamado al iniciar el modo, implementado por cada clase concreta.
     */
    protected abstract void onIniciar();

    @Override
    public void actualizar(float delta) {

        actualizarEntrada(delta);
        actualizarFisicas(delta);
        actualizarColisiones(delta);
        actualizarPelota(delta);
        ctx.partido.verificarSiHayGol(ctx.pelota, ctx.cancha);
    }

    protected void actualizarEntrada(float delta) {
        for (EntradaJugador entrada : ctx.controles) {
            if (entrada != null) entrada.actualizar(delta);
        }
    }

    protected void actualizarFisicas(float delta) {
        for (Personaje pj : ctx.jugadores) {
            if (pj != null) ctx.fisica.actualizarPersonaje(pj, delta);
        }

        float W = ctx.viewport.getWorldWidth();
        float H = ctx.viewport.getWorldHeight();

        for (Personaje pj : ctx.jugadores) {
            if (pj != null) ctx.fisica.limitarPersonajeAlMundo(pj, W, H);
        }
    }

    protected void actualizarColisiones(float delta) {
        // jugador ↔ jugador
        for (int i = 0; i < ctx.jugadores.size(); i++) {
            for (int j = i + 1; j < ctx.jugadores.size(); j++) {
                Personaje a = ctx.jugadores.get(i);
                Personaje b = ctx.jugadores.get(j);
                if (a != null && b != null) {
                    ctx.colisiones.separarJugadoresSiChocan(a, b);
                }
            }
        }

        // jugador ↔ pelota
        for (Personaje pj : ctx.jugadores) {
            if (pj != null && ctx.pelota != null) {
                ctx.colisiones.procesarContactoPelotaConJugador(ctx.pelota, pj);
            }
        }
    }

    protected void actualizarPelota(float delta) {
        ctx.fisica.rebotarLaPelotaEnLosBordes(ctx.pelota, ctx.cancha);
        ctx.fisica.actualizarPelota(ctx.pelota, delta);

    }

    @Override
    public void renderizar(SpriteBatch batch) {
        // Por defecto nada.
    }

    @Override
    public boolean finalizado() {
        return terminado;
    }

    @Override
    public void liberar() {
        // sin recursos propios por defecto
    }

    @Override
    public int getCantidadDeJugadores() {
        return 2; // valor por defecto para la mayoría de modos
    }
}